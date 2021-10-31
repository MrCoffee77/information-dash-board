package de.paetz.feuerwehr.informationdashboard.services.configuration.entities;

import de.paetz.feuerwehr.informationdashboard.model.CalendarConfig;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Objects;

@Entity
public class Calendar {
    @Id
    @NotNull
    private String calendarName;

    @NotNull
    private URI calendarUri;


    private String userName;

    private String password;

    public Calendar() {

    }

    public Calendar(CalendarConfig configuration) {

        this.calendarName=configuration.getName();
        this.calendarUri=configuration.getAddress();
        this.password=configuration.getPassword();
        this.userName=configuration.getUser();
    }


    public String getCalendarName() {
        return calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    public URI getCalendarUri() {
        return calendarUri;
    }

    public void setCalendarUri(URI calendarUri) {
        this.calendarUri = calendarUri;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Calendar calendar = (Calendar) o;
        return calendarName.equals(calendar.calendarName) && calendarUri.equals(calendar.calendarUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calendarName, calendarUri);
    }
}
