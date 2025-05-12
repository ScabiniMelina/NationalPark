package model;


import model.util.JsonReader;
import observer.NationalParkObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class NationalParkGraphTest {

    private String validFilePath;
    private List<Station> stations;
    private List<Trail> trails;
    private NationalParkGraph graph;

    @BeforeEach
    void setUp() throws IOException {
        validFilePath = "valid_file_with_national_park_stations.json";
        this.stations = JsonReader.readStationsFromJson(validFilePath);
        this.trails = JsonReader.readTrailsFromJson(validFilePath,stations);
        this.graph = new NationalParkGraph(stations, trails);
    }

    @Test
    void testGetStationByIdFound() {
        Station found = graph.getStationById(1);
        assertEquals("Refugio Frey", found.getName());
    }

    @Test
    void testCalculateTotalImpact() {
        NationalParkGraph graph = new NationalParkGraph(stations, trails);
        assertEquals(214, graph.getTotalImpact());
    }

    @Test
    void testConstructorNullStationsThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new NationalParkGraph(null, null);
        });
        assertEquals("Stations cannot be null", exception.getMessage());
    }


    @Test
    void testConstructorNullTrailsThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new NationalParkGraph(this.stations, null);
        });
        assertEquals("Trails cannot be null", exception.getMessage());
    }


}
