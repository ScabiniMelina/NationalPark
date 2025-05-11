import controller.NationalPark;
import model.*;
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

            NationalParkGraph baseNationalParkModel = new NationalParkGraph(stations, trails);
            NationalParkGraph kruskalNationalParkModel = new NationalParkGraphKruskal(stations,trails);
            NationalParkGraph primNationalParkModel = new NationalParkGraphPrim(stations,trails);



            NationalPark nationalParkController = new NationalPark();

            NationalParkMap nationalParkMap = new NationalParkMap(
                    nationalParkController,
                    baseNationalParkModel,
                    kruskalNationalParkModel,
                    primNationalParkModel
            );

            nationalParkMap.setVisible(true);
            baseNationalParkModel.addObserver(nationalParkMap);
            kruskalNationalParkModel.addObserver(nationalParkMap);
            primNationalParkModel.addObserver(nationalParkMap);
    }
}