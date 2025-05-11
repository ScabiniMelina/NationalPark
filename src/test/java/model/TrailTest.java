package model;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrailTest {

    @Test
    public void testTrailCreation() {
        // Test creation with valid data
        Station start = new Station(1, "Station A", 0.0, 0.0);
        Station end = new Station(2, "Station B", 1.0, 1.0);
        Trail trail = new Trail(start, end, 3);

        // Verify that the getters return the correct values
        assertEquals(start, trail.getStart(), "The start station should be Station A");
        assertEquals(end, trail.getEnd(), "The end station should be Station B");
        assertEquals(3, trail.getEnvironmentalImpact(), "The environmental impact should be 3");
    }

    @Test
    public void testTrailWithMaxImpact() {
        Station start = new Station(1, "Station A", 0.0, 0.0);
        Station end = new Station(2, "Station B", 1.0, 1.0);
        Trail trail = new Trail(start, end, 10);

        assertEquals(10, trail.getEnvironmentalImpact(), "The environmental impact should be 10");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTrailWithInvalidImpactAboveRange() {
        Station start = new Station(1, "Station A", 0.0, 0.0);
        Station end = new Station(2, "Station B", 1.0, 1.0);
        new Trail(start, end, 11);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTrailWithInvalidImpactBelowRange() {
        Station start = new Station(1, "Station A", 0.0, 0.0);
        Station end = new Station(2, "Station B", 1.0, 1.0);
        new Trail(start, end, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTrailWithNullStart() {
        Station end = new Station(2, "Station B", 1.0, 1.0);
        new Trail(null, end, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTrailWithNullEnd() {
        Station start = new Station(1, "Station A", 0.0, 0.0);
        new Trail(start, null, 3);
    }

}