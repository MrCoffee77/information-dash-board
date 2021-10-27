package de.paetz.feuerwehr.informationdashboard.model;

import java.time.LocalDateTime;

public record Raumbelegung(String raum, LocalDateTime start, String titel) {

}
