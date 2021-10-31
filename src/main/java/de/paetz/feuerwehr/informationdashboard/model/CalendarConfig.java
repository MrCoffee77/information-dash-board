package de.paetz.feuerwehr.informationdashboard.model;

import java.net.URI;

public class CalendarConfig {
    private String name;
    private URI address;
    private String user;
    private String password;


    public CalendarConfig(String name, URI adress, String user, String password) {
        this.name=name;
        this.address=adress;
        this.user=user;
        this.password=password;
    }

    public String getName() {
        return name;
    }

    public URI getAddress() {
        return address;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(URI address) {
        this.address = address;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
