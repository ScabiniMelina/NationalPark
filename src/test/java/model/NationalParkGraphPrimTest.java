package model;

import observer.NationalParkObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NationalParkGraphPrimTest {

    private List<Station> stations;
    private List<Trail> trails;

    @BeforeEach
    void setUp() {
        stations = new ArrayList<>();
        stations.add(new Station(1, "Refugio Frey", -41.1833, -71.4167));
        stations.add(new Station(2, "Refugio Jakob", -41.2000, -71.4667));
        stations.add(new Station(3, "Cerro Catedral Base", -41.1667, -71.4333));
        stations.add(new Station(4, "Lago Gutiérrez", -41.1833, -71.3833));

        trails = new ArrayList<>();
        trails.add(new Trail(stations.get(0), stations.get(2), 3)); // 1->3, impact 3
        trails.add(new Trail(stations.get(0), stations.get(1), 5)); // 1->2, impact 5
        trails.add(new Trail(stations.get(2), stations.get(3), 2)); // 3->4, impact 2
        trails.add(new Trail(stations.get(1), stations.get(3), 4)); // 2->4, impact 4
    }

    @Test
    void testMinimumSpanningTreeHasCorrectTrailCount() {
        NationalParkGraphPrim graph = new NationalParkGraphPrim(stations, trails);
        graph.calculateMinimumSpanningTree();
        List<Trail> mstTrails = graph.getTrails();
        assertEquals(3, mstTrails.size(),
                "MST should have 3 trails for a graph with 4 stations");
    }

    @Test
    void testMinimumSpanningTreeHasCorrectTotalImpact() {
        NationalParkGraphPrim graph = new NationalParkGraphPrim(stations, trails);
        graph.calculateMinimumSpanningTree();
        List<Trail> mstTrails = graph.getTrails();
        int totalImpact = mstTrails.stream().mapToInt(Trail::getEnvironmentalImpact).sum();
        assertEquals(9, totalImpact,
                "MST total environmental impact should be 9 (2 + 3 + 4)");
    }

    @Test
    void testMinimumSpanningTreeCoversAllStations() {
        NationalParkGraphPrim graph = new NationalParkGraphPrim(stations, trails);
        graph.calculateMinimumSpanningTree();
        List<Trail> mstTrails = graph.getTrails();
        Set<Station> mstStations = new HashSet<>();
        for (Trail trail : mstTrails) {
            mstStations.add(trail.getStart());
            mstStations.add(trail.getEnd());
        }
        assertEquals(4, mstStations.size(),
                "MST should connect all 4 stations");
    }

    @Test
    void testInitialExecutionTimeIsZero() {
        NationalParkGraphPrim graph = new NationalParkGraphPrim(stations, trails);
        assertEquals(0, graph.getExecutionTimeInNanoseconds(),
                "Execution time should be zero before calculating MST");
    }

    @Test
    void testExecutionTimeIsPositiveAfterCalculatingMST() {
        NationalParkGraphPrim graph = new NationalParkGraphPrim(stations, trails);
        graph.calculateMinimumSpanningTree();
        assertTrue(graph.getExecutionTimeInNanoseconds() > 0,
                "Execution time should be positive after calculating MST");
    }


    @Test
    void testCalculateTotalImpactUsesTreeTrails() {
        NationalParkGraphPrim graph = new NationalParkGraphPrim(stations, trails);
        graph.calculateMinimumSpanningTree();
        assertEquals(9, graph.getTotalImpact()); // MST impact: 2 + 3 + 4
    }

    @Test
    void testCalculateTotalImpactEmptyTreeTrails() {
        NationalParkGraphPrim graph = new NationalParkGraphPrim(stations, new ArrayList<>());
        graph.calculateTotalImpact();
        assertEquals(0, graph.getTotalImpact());
    }


    @Test
    void testCalculateMinimumSpanningTreeSingleStation() {
        List<Station> singleStation = List.of(new Station(1, "Refugio Frey", -41.1833, -71.4167));
        NationalParkGraphPrim graph = new NationalParkGraphPrim(singleStation, new ArrayList<>());
        graph.calculateMinimumSpanningTree();
        assertTrue(graph.getTrails().isEmpty());
        assertEquals(0, graph.getTotalImpact());
    }

    //TODO: COMO VALIDAR SI EL GRAFO ESTA DISCONEXO?

}