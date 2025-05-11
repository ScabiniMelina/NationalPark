import controller.NationalPark;
import model.NationalParkGraph;
import model.Station;
import model.Trail;
import utils.JsonReader;
import view.NationalParkView;

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

            NationalParkView nationalParkView = new NationalParkView(nationalParkModel, nationalParkController);
            nationalParkView.setVisible(true);

    }
}