package util;

import model.Station;
import model.Trail;

import com.google.gson.JsonParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonReaderTest {

    private String validFilePath;
    private String invalidFilePath;
    private String malformedJsonFilePath;

    @BeforeEach
    void setUp() {
        invalidFilePath = "non_existent.json";
        validFilePath = "valid_file_with_national_park_stations.json";
        malformedJsonFilePath = "malformed_file_with_national_park_stations.json";
    }


    @Test
    void testReadStationsSuccess() throws IOException {
        List<Station> stations = JsonReader.readStationsFromJson(validFilePath);

        assertEquals(15, stations.size(), "Should load 15 stations");

        // Verify the first station
        Station station1 = stations.get(0);
        assertEquals(1, station1.getId(), "Station 1 ID should be 1");
        assertEquals("Refugio Frey", station1.getName(), "Station 1 name should be Refugio Frey");
        assertEquals(-41.1833, station1.getX(), 0.0001, "Station 1 x coordinate should be -41.1833");
        assertEquals(-71.4167, station1.getY(), 0.0001, "Station 1 y coordinate should be -71.4167");

    }

    @Test
    void testFileNotFound() {
        Exception exception = assertThrows(IOException.class, () -> {
            JsonReader.readStationsFromJson(invalidFilePath);
        });
        assertTrue(exception.getMessage().contains("JSON file not found"), "Should throw IOException for missing file");
    }

    @Test
    void testMalformedJson() {
        Exception exception = assertThrows(JsonParseException.class, () -> {
            JsonReader.readStationsFromJson(malformedJsonFilePath);
        });
        assertNotNull(exception, "Should throw JsonParseException for malformed JSON");
    }

    @Test
    void testReadTrailsSuccess() throws IOException {
        List<Station> stations = JsonReader.readStationsFromJson(validFilePath);
        List<Trail> trails = JsonReader.readTrailsFromJson(validFilePath, stations);

        assertEquals(15, trails.size(), "Should load 15 trails");


        Trail trail1 = trails.get(0);
        assertEquals("Refugio Frey", trail1.getStart().getName(), "Trail 1 start should be Refugio Frey");
        assertEquals("Cerro Catedral Base", trail1.getEnd().getName(), "Trail 1 end should be Cerro Catedral Base");
        assertEquals(3, trail1.getEnvironmentalImpact(), "Trail 1 environmental impact should be 3");

    }


}