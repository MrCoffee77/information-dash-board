package de.paetz.feuerwehr.informationdashboard.services.configuration;

import de.paetz.feuerwehr.informationdashboard.model.CalendarConfig;
import de.paetz.feuerwehr.informationdashboard.services.configuration.entities.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CalendarService {
    @Autowired
    CalendarRepository repository;

    @Transactional
    public void addCalendar(CalendarConfig config) {

        repository.save(new Calendar(config));
    }

    @Transactional
    public void deleteCalendar(String config) {

        Optional<Calendar> cal=repository.findById(config);
        cal.ifPresent(repository::delete);
    }
    @Transactional
    public void updateCalendar(String oldName,CalendarConfig config) {

        Optional<Calendar> calOpt=repository.findById(oldName);
        Calendar cal=calOpt.orElseGet(Calendar::new);
        cal.setCalendarUri(config.getAddress());
        cal.setCalendarName(config.getName());
        cal.setUserName(config.getUser());
        cal.setPassword(config.getPassword());
        repository.save(cal);
    }
    public List<CalendarConfig> getAllCalendars() {
        List<CalendarConfig> ret=new ArrayList<>();
        repository.findAll().forEach(obj -> ret.add(new CalendarConfig(obj.getCalendarName(),obj.getCalendarUri(),obj.getUserName(),obj.getPassword())));
        return ret;
    }

}
