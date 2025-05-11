package Model;

import model.Station;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StationTest {

    @Test
    public void testStationCreation() {
        // Test creation with valid data
        Station station = new Station(1, "Refugio Frey", -41.1833, -71.4167);

        // Verify that the getters return the correct values as strings
        assertEquals(1, station.getId(), "The ID should be '1' as a string");
        assertEquals("Refugio Frey", station.getName(),"The name should be 'Refugio Frey'");
        assertEquals("The X coordinate (latitude) should be '-41.1833' as a string", "-41.1833", String.valueOf(station.getX()));
        assertEquals("The Y coordinate (longitude) should be '-71.4167' as a string", "-71.4167", String.valueOf(station.getY()));
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