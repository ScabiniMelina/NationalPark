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

         public NationalPark(NationalParkGraph nationalParkGraph) {
             this.model = nationalParkGraph;
         }

         public NationalParkGraph getGraph() {
             return model;
         }


        // Opcional: Método para actualizar la vista con un MST
        public void showMinimumSpanningTree() {
            List<Trail> mstTrails = model.calculateMinimumSpanningTree(); // Implementar en Graph
            //view.updateTrails(mstTrails);
        }

    }

