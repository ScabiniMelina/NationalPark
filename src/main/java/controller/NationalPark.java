package controller;


import model.NationalParkGraph;

public class NationalPark {

         public NationalPark() {
         }

        public void generateMinimumSpanningTree(NationalParkGraph graph){
            graph.calculateMinimumSpanningTree();
        }
 }

