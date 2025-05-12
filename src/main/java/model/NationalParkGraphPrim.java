package model;

import java.util.ArrayList;
import java.util.List;

public class NationalParkGraphPrim extends NationalParkGraph {
    List<Trail> minimumSpanningTreeTrails;

    public NationalParkGraphPrim(List<Station> stations, List<Trail> trails) {
        super(stations, trails);
        this.minimumSpanningTreeTrails = new ArrayList<>();
    }

    @Override
    public List<Trail> getTrails(){
        return this.minimumSpanningTreeTrails;
    }

    @Override
    public void calculateMinimumSpanningTree() {
        long startTime = System.nanoTime();
        List<Station> treeStations = new ArrayList<>();
        minimumSpanningTreeTrails.clear();

        treeStations.add(stations.getFirst());
        int stationsSize = stations.size();
        for (int i = 1; i <= stationsSize-1; i++){
            Trail minTrail = getMinimumTrail(treeStations);
            if (minTrail == null) break;
            minimumSpanningTreeTrails.add(minTrail);
            if (!treeStations.contains(minTrail.getStart())) {
                treeStations.add(minTrail.getStart());
            } else {
                treeStations.add(minTrail.getEnd());
            }
        }
        long endTime = System.nanoTime();
        executionTimeInNanoseconds = endTime - startTime;
        calculateTotalImpact();
        super.notifyObservers();

    }

    private Trail getMinimumTrail(List<Station> treeStations) {
        Trail minTrail = null;
        for (Station station: treeStations){
            for (Trail trail: trails){
                boolean connectsToTree = validateConnection(treeStations, trail, station);
                if (connectsToTree) {
                    if (minTrail == null || trail.getEnvironmentalImpact() < minTrail.getEnvironmentalImpact()) {
                        minTrail = trail;
                    }
                }
            }
        }

        return minTrail;
    }

    private boolean validateConnection(List<Station> treeStations, Trail trail, Station station){
        return ((trail.getStart().equals(station) && !treeStations.contains(trail.getEnd())) ||
                (trail.getEnd().equals(station) && !treeStations.contains(trail.getStart())));
    }

    @Override
    public void calculateTotalImpact() {
        if (minimumSpanningTreeTrails == null || minimumSpanningTreeTrails.isEmpty()) {
            totalImpact = 0;
            return;
        }

        int sum = 0;
        for (Trail trail : minimumSpanningTreeTrails) {
            sum += trail.getEnvironmentalImpact();
        }
        totalImpact =  sum;
    }
}
