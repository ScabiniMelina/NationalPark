package controller;


import model.NationalParkGraph;
import model.Station;
import model.Trail;
import util.JsonReader;
import view.NationalParkMap;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

 public class NationalPark {
        private NationalParkGraph model;
        private NationalParkMap view;

        public NationalPark() throws IOException {

            List<Station> stations = JsonReader.readStationsFromJson("nationalParkStations.json");
            List<Trail> trails = JsonReader.readTrailsFromJson("nationalParkStations.json", stations);

            model = new NationalParkGraph(stations, trails);
            view = new NationalParkMap(stations, trails);
        }

        public void showView() {
            view.setVisible(true);
        }

        // Opcional: Método para actualizar la vista con un MST
        public void showMinimumSpanningTree() {
            List<Trail> mstTrails = model.calculateMinimumSpanningTree(); // Implementar en Graph
            view.updateTrails(mstTrails);
        }

        public static void main(String[] args) {
            try {
                NationalPark controller = new NationalPark();
                controller.showView();
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al cargar los datos: " + e.getMessage());
            }
        }
    }

