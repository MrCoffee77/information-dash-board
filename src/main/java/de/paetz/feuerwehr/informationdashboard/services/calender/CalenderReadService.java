package de.paetz.feuerwehr.informationdashboard.services.calender;

import de.paetz.feuerwehr.informationdashboard.model.CalendarConfig;
import de.paetz.feuerwehr.informationdashboard.model.Raumbelegung;
import de.paetz.feuerwehr.informationdashboard.services.configuration.CalendarService;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.filter.Filter;
import net.fortuna.ical4j.filter.predicate.PeriodRule;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class CalenderReadService {
    @Autowired
    CalendarService calendarService;

    public Set<Raumbelegung> getBlockedTimes() {
        List<CalendarConfig> calendars = calendarService.getAllCalendars();
        Set<Raumbelegung> ret=new TreeSet<>(Comparator.comparing(Raumbelegung::start));
        calendars.forEach(calendarConfig -> {
            Calendar calendar=readCalenderFromConfig(calendarConfig);
            List<VEvent> events=calendar.getComponents(Component.VEVENT);
            ret.addAll(filterAndMapToRaumBelegung(calendarConfig.getName(),events));
        });

        return ret;
    }

    private LocalDateTime convert(DateTime dt) {
        return dt.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();

    }

    private List<Raumbelegung> filterAndMapToRaumBelegung(String raum, List<VEvent> events) {
        java.util.Calendar today = java.util.Calendar.getInstance();
        today.set(java.util.Calendar.HOUR_OF_DAY, 0);
        today.clear(java.util.Calendar.MINUTE);
        today.clear(java.util.Calendar.SECOND);

        Period period = new Period(new DateTime(today.getTime()), Duration.ofDays(getDaysToCatch()));
        PeriodRule periodRule = new PeriodRule(period);

        events.stream().filter(periodRule).toList();
        List<VEvent> eventsToday = events.stream().filter(periodRule).toList();
        DateFormat df = SimpleDateFormat.getDateTimeInstance();
        return eventsToday.stream().map(event -> {
            PeriodList list = event.calculateRecurrenceSet(period);
            return list.stream().map(periodObj -> new Raumbelegung(raum, convert(periodObj.getStart()),convert(periodObj.getEnd()),event.getSummary().getValue() )).toList();
        }).collect(ArrayList::new,ArrayList::addAll,ArrayList::addAll);

    }

    private long getDaysToCatch() {
        return 7;
    }

    private Calendar readCalenderFromConfig(CalendarConfig config) {
        HttpClient.Builder clientBuilder = HttpClient.newBuilder();
        if (config.getUser()!=null && !"".equals(config.getUser())) {
            clientBuilder.authenticator(new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(config.getUser(),config.getPassword().toCharArray());
                }
            });
        }
        HttpClient httpClient= clientBuilder.build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(config.getAddress())
                .build();
        try {
            HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());

            try (InputStream is = response.body(); InputStreamReader isr = new InputStreamReader(is); BufferedReader br = new BufferedReader(isr); Reader reader = new VCardTagFilteringReader(br);) {

                CalendarBuilder builder = new CalendarBuilder();
                return builder.build(reader);
            }
        } catch (IOException | ParserException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    //Hack um Nextcloud Calender zu lesen
    private static class VCardTagFilteringReader extends Reader {
        private final BufferedReader br;
        List<Character> buffer;

        public VCardTagFilteringReader(BufferedReader br) {
            this.br = br;
            buffer = new ArrayList<>();
        }

        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
            while (buffer.size()<len) {
                String line=readLine();
                if (line==null) {
                    break;
                }
                line.chars().forEach(c-> buffer.add((char)c));
                buffer.add('\r');
                buffer.add('\n');
            }
            int lOff=off;
            while (lOff<off+len && !buffer.isEmpty()) {
                cbuf[lOff++]=buffer.remove(0);
            }
            if (buffer.isEmpty()) {
                return -1;
            }
            return lOff-off;
        }

        @Override
        public void close() throws IOException {

        }

        private String readLine() throws IOException {
            String line= br.readLine();
            if (line!=null && line.startsWith("REFRESH-INTERVAL")) {
                return readLine();
            }
            return line;
        }
    }
}
