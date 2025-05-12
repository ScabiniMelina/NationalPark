package model;

import model.util.JsonReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NationalParkGraphKrustalTest {
    private String validFilePath;
    private List<Station> stations;
    private List<Trail> trails;
    private NationalParkGraph graph;

    @BeforeEach
    void setUp() throws IOException {
        validFilePath = "valid_file_with_national_park_stations.json";
        this.stations = JsonReader.readStationsFromJson(validFilePath);
        this.trails = JsonReader.readTrailsFromJson(validFilePath,stations);
        this.graph = new NationalParkGraphKruskal(stations, trails);
    }

    @Test
    void testShouldStartWithZeroExecutionTimeAndUpdateAfterCalculation() {
        // When
        long initialExecutionTime = graph.getExecutionTimeInNanoseconds();
        graph.calculateMinimumSpanningTree();
        long updatedExecutionTime = graph.getExecutionTimeInNanoseconds();

        // Then
        assertEquals(0L, initialExecutionTime, "Initial execution time should be 0");
        assertTrue(updatedExecutionTime > 0, "Execution time should be updated after calculation");
    }

    @Test
    void testShouldBuildMinimumSpanningTreeWithCorrectImpact() {
        // Given
        Station a = new Station(1, "A", 1, 1);
        Station b = new Station(2, "B", 2, 2);
        Station c = new Station(3, "C", 3, 4);

        Trail ab = new Trail(a, b, 3);
        Trail bc = new Trail(b, c, 1);
        Trail ac = new Trail(a, c, 2);

        List<Station> stations = List.of(a, b, c);
        List<Trail> trails = List.of(ab, bc, ac);
        NationalParkGraphKruskal graph = new NationalParkGraphKruskal(stations, trails);

        // When
        graph.calculateMinimumSpanningTree();
        List<Trail> mst = graph.getTrails();

        // Then
        assertEquals(2, mst.size(), "MST should have n-1 edges");
        assertTrue(mst.contains(ac) || mst.contains(ab), "Should include one of the lowest cost edges");
        assertEquals(3, graph.getTotalImpact(), "Should equal minimal total impact (1+2)");
        assertTrue(graph.getExecutionTimeInNanoseconds() > 0L, "Execution time should be measured");
    }

    @Test
    void testShouldNotBuildTreeWhenNoTrailsAvailable() {
        // Given
        Station a = new Station(1, "A", 1, 1);
        Station b = new Station(2, "B", 2, 2);
        List<Station> stations = List.of(a, b);
        List<Trail> trails = new ArrayList<>();
        NationalParkGraphKruskal graph = new NationalParkGraphKruskal(stations, trails);

        // When
        graph.calculateMinimumSpanningTree();

        // Then
        assertTrue(graph.getTrails().isEmpty(), "No tree can be formed");
        assertEquals(0, graph.getTotalImpact(), "Impact should be 0");
    }

    @Test
    void shouldAvoidCreatingCyclesInMST() {
        // Given
        Station a = new Station(1, "A", 1, 1);
        Station b = new Station(2, "B", 2, 2);
        Station c = new Station(3, "C", 3, 3);

        Trail ab = new Trail(a, b, 1);
        Trail bc = new Trail(b, c, 2);
        Trail ca = new Trail(c, a, 10);

        List<Station> stations = List.of(a, b, c);
        List<Trail> trails = List.of(ab, bc, ca);
        NationalParkGraphKruskal graph = new NationalParkGraphKruskal(stations, trails);

        // When
        graph.calculateMinimumSpanningTree();
        List<Trail> mst = graph.getTrails();

        // Then
        assertEquals(2, mst.size(), "MST should not form cycle");
        assertFalse(mst.contains(ca), "High-cost cycle trail should be excluded");
    }

}
