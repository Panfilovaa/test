package ru.neoflex.panfilova;

import com.google.gson.*;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ru.neoflex.Panfilova.GetPropertyValues;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class Swapi {

    private Logger logger = LogManager.getLogger(Swapi.class);
    private Properties values;

    @BeforeTest
    public void before() throws IOException {
        this.values = new GetPropertyValues().getPropValues();
    }


    @Test //получить список сущностей
    public void test3() {
        logger.info("Список всех сущностей:\n");
        logger.info(get(""));
    }

    @Test //получить список фильмов. Получить информацию по одному из фильмов
    public void test4() {
        String jsonFile = get("films");
        logger.info(String.format("Список всех фильмов: \n%s%n", jsonFile));

        Object film = path(jsonFile, "$..[?(@.title == 'A New Hope')]");
        logger.info(String.format("Выбранный фильм: \n%s%n", film));
    }

    @Test //получить список планет. Получить информацию по выбранной планете из выбранного фильма
    public void test5() {
        String jsonFile = get("planets");
        logger.info(String.format("Список всех планет: \n%s%n", jsonFile));

        jsonFile = get("films");
        String film = path(jsonFile, "$..[?(@.title == 'A New Hope')]");
        String planets = path(film, "$.[0].planets");

        JsonArray planetsArray = (JsonArray) JsonParser.parseString(planets);
        JsonElement planetUrl = planetsArray.get(0);
        String replaceUrl = planetUrl.getAsString().replace(this.values.getProperty("url"), "");
        jsonFile = get(replaceUrl);

        logger.info(String.format("Одна из планет из фильма A New Hope: \n%s%n", jsonFile));
    }

    @Test //Получить список рас из выбранного фильма, с выбранной планеты
    public void test6() {
        String jsonFile = get("films");
        String film = path(jsonFile, "$..[?(@.title == 'A New Hope')]");

        String planets = path(film, "$.[0].planets");
        JsonArray planetsArray = (JsonArray) JsonParser.parseString(planets);
        String planetUrl = planetsArray.get(0).getAsString();

        String species = path(film, "$.[0].species");

        ArrayList allSpecies = new ArrayList();

        JsonArray speciesArray = (JsonArray) JsonParser.parseString(species);
        for (int i = 0; i < speciesArray.size(); i++) {
            JsonElement speciesUrl = speciesArray.get(i);
            String replaceUrl = speciesUrl.getAsString().replace(this.values.getProperty("url"), "");
            jsonFile = get(replaceUrl);

            String homeworld = path(jsonFile, "$.homeworld");
            if (homeworld.equals(planetUrl)) {
                allSpecies.add(jsonFile);
            }
        }
        if (allSpecies.isEmpty()) {
            allSpecies.add("На данной планете нет рас");
        }
        logger.info(String.format("Все расы из фильма A New Hope, с планеты Tatooine: \n%s%n", String.join(", ", allSpecies)));
    }

    @Test //Получить список пилотов корабля из выбранного фильма
    public void test7() {
        String jsonFile = get("films");
        String film = path(jsonFile, "$..[?(@.title == 'A New Hope')]");

        String starships = path(film, "$.[0].starships");
        JsonArray starshipsArray = (JsonArray) JsonParser.parseString(starships);

        for (int i = 0; i < starshipsArray.size(); i++) {
            String starshipsUrl = starshipsArray.get(i).getAsString().replace(this.values.getProperty("url"), "");

            String starshipsget = get(starshipsUrl);
            String pilots = path(starshipsget, "$.pilots");
            String starshipName = path(starshipsget, "$.name");

            String toLog = starshipName + " - ";
            if (pilots.equals("[]")) {
                toLog += "На данном корабле нет пилотов";
            } else {
                toLog += String.format("Cписок пилотов корабля: \n%s", pilots);
            }
            logger.info(toLog);
        }
    }

    public String get(String s) {
        Response response = given()
                .baseUri(this.values.getProperty("url"))
                .when()
                .get(s);
        return response.getBody().asPrettyString();
    }

    public String path(String json, String jsonPath) {
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Object jsonString = JsonPath.read(document, jsonPath);

        return gson.toJson(jsonString);
    }
}
