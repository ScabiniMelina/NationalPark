package model;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StationTest {

    @Test
    public void testStationCreation() {
        // Test creation with valid data
        Station station = new Station(1, "Refugio Frey", -41.1833, -71.4167);

        // Verify that the getters return the correct values as strings
        assertEquals(1, station.getId(), "The ID should be 1");
        assertEquals("Refugio Frey", station.getName(), "The name should be 'Refugio Frey'");
        assertEquals(-41.1833, station.getX(), "The x coordinate (latitude) should be - 41.1833");
        assertEquals( -71.4167, station.getY(), "The Y coordinate (longitude) should be -71.4167");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStationWithNullName() {
        // Test creation with null name should throw exception
        new Station(2, null, -41.1667, -71.4333);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStationWithEmptyName() {
        // Test creation with empty name should throw exception
        new Station(3, "", -41.1667, -71.4333);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStationWithWhitespaceName() {
        // Test creation with whitespace-only name should throw exception
        new Station(4, "   ", -41.1667, -71.4333);
    }

}