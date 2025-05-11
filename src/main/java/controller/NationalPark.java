package controller;


import model.NationalParkGraph;

 public class NationalPark {
        private NationalParkGraph model;

         public NationalPark(NationalParkGraph nationalParkGraph) {
             this.model = nationalParkGraph;
         }

        public void generateMinimumSpanningTree(){
            model.calculateMinimumSpanningTree();
        }


     public NationalParkGraph getGraph() {
         return model;
     }

 }

