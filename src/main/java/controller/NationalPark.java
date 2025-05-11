package controller;


import model.NationalParkGraph;

public class NationalPark {

         public NationalPark() {
         }

        public void generateMinimumSpanningTree(NationalParkGraph graph){
            graph.calculateMinimumSpanningTree();
        }

        public void reDrawOrginalPark(NationalParkGraph graph) {
             graph.notifyObservers();
        }
}

