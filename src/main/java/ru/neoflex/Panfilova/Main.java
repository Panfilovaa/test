package ru.neoflex.Panfilova;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        User user = new User("Anastasia", "Saratov", new Date());
        Print.print(user);
    }
}
