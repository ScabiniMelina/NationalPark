import controller.NationalPark;
import model.NationalParkGraph;
import model.Station;
import model.Trail;
import model.util.JsonReader;
import view.NationalParkMap;

import java.util.List;

public class Main {
    public static void main(String[] args) {

            List<Station> stations;
            List<Trail> trails;
            try {
                stations = JsonReader.readStationsFromJson("nationalParkStations.json");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                trails = JsonReader.readTrailsFromJson("nationalParkStations.json", stations);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            NationalParkGraph nationalParkModel = new NationalParkGraph(stations, trails);
            NationalPark nationalParkController = new NationalPark(nationalParkModel);

            NationalParkMap nationalParkMap = new NationalParkMap(nationalParkController, nationalParkModel);
            nationalParkMap.setVisible(true);
    }
}