package ru.neoflex.panfilova;

import org.testng.annotations.Test;
import ru.neoflex.Panfilova.GetPropertyValues;
import ru.neoflex.Panfilova.Print;
import ru.neoflex.Panfilova.User;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class ReadConfigMain {
    @Test
    public void test2() throws IOException {
        Properties values = new GetPropertyValues().getPropValues();
        User user = new User(values.getProperty("user"), values.getProperty("city"), new Date());
        Print.print(user);
    }

}