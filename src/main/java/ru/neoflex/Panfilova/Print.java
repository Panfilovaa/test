package ru.neoflex.Panfilova;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Print {
    public static void print(User user) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        System.out.println(user.getUsername() + " " + user.getCity() + " " + formatter.format(user.getDate()));
    }
}
