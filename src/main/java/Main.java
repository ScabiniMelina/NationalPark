import controller.NationalPark;
import model.NationalParkGraph;
import model.Station;
import model.Trail;
import model.util.JsonReader;
import view.NationalParkMap;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

            List<Station> stations;
            List<Trail> trails;

            stations = JsonReader.readStationsFromJson("nationalParkStations.json");
            trails = JsonReader.readTrailsFromJson("nationalParkStations.json", stations);

            NationalParkGraph nationalParkModel = new NationalParkGraph(stations, trails);
            NationalPark nationalParkController = new NationalPark(nationalParkModel);

            NationalParkMap nationalParkMap = new NationalParkMap(nationalParkController, nationalParkModel);
            nationalParkMap.setVisible(true);
    }
}