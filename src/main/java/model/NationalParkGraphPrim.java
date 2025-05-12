package model;

import java.util.ArrayList;
import java.util.List;

public class NationalParkGraphPrim extends NationalParkGraph {
    List<Trail> treeTrails;

    public NationalParkGraphPrim(List<Station> stations, List<Trail> trails) {
        super(stations, trails);
        this.treeTrails = new ArrayList<>();
    }

    @Override
    public List<Trail> getTrails(){
        return this.treeTrails;
    }

    @Override
    public void calculateMinimumSpanningTree() {
        long startTime = System.nanoTime();
        List<Station> treeStations = new ArrayList<>();
        treeTrails.clear();

        treeStations.add(stations.getFirst());
        int stationsSize = stations.size();
        for (int i = 1; i <= stationsSize-1; i++){
            Trail minTrail = getMinimumTrail(treeStations);
            if (minTrail == null) break;
            treeTrails.add(minTrail);
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
        if (treeTrails == null || treeTrails.isEmpty()) {
            totalImpact = 0;
            return;
        }

        int sum = 0;
        for (Trail trail : treeTrails) {
            sum += trail.getEnvironmentalImpact();
        }
        totalImpact =  sum;
    }
}
