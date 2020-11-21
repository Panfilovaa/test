package ru.neoflex.Panfilova;

import java.util.Date;

public class User {

    private String username;
    private String city;
    private Date date;

    public User(String username, String city, Date date){
        this.username = username;
        this.city = city;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public String getCity() {
        return city;
    }

    public Date getDate() {
        return date;
    }
}
