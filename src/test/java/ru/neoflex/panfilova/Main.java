package ru.neoflex.panfilova;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.neoflex.Panfilova.Print;
import ru.neoflex.Panfilova.User;

import java.util.Date;

public class Main {

    @Parameters({"username", "city"})
    @Test
    public void test1(String username, String city) {
       User user = new User(username, city, new Date());
        Print.print(user);
    }
}
