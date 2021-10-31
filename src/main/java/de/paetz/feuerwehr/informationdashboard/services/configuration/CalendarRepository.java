package de.paetz.feuerwehr.informationdashboard.services.configuration;

import de.paetz.feuerwehr.informationdashboard.services.configuration.entities.Calendar;
import org.springframework.data.repository.CrudRepository;

public interface CalendarRepository extends CrudRepository<Calendar,String> {
}
