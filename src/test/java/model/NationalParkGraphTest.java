package model;

import model.util.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        //Given
        int id = 1;

        //When
        Station found = graph.getStationById(id);

        //Then
        assertEquals("Refugio Frey", found.getName());
    }

    @Test
    void testCalculateTotalImpact() {
        //When
        NationalParkGraph graph = new NationalParkGraph(stations, trails);
        //Then
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

    @Test
    void testGetExecutionTimeInNanoseconds(){
        //When
        long executionTime = graph.getExecutionTimeInNanoseconds();
        //Then
        assertEquals( 0L, executionTime, "The execution time should be 0");
    }
}
