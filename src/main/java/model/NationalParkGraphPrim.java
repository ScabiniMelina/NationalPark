package model;

import java.util.ArrayList;
import java.util.List;

public class NationalParkGraphPrim extends NationalParkGraph {
    List<Station> treeStations;
    List<Trail> treeTrails;


    public NationalParkGraphPrim(List<Station> stations, List<Trail> trails) {
        super(stations, trails);
        this.treeStations = new ArrayList<>();
        this.treeTrails = new ArrayList<>();
    }

    @Override
    public void calculateMinimumSpanningTree() {
        treeStations.clear();
        treeTrails.clear();

        treeStations.add(stations.getFirst());
        int stationsSize = stations.size();
        for (int i = 1; i <= stationsSize-1; i++){
            Trail minTrail = getMinimumTrail();
            if (minTrail == null) break;
            treeTrails.add(minTrail);

            if (!treeStations.contains(minTrail.getStart())) {
                treeStations.add(minTrail.getStart());
            } else {
                treeStations.add(minTrail.getEnd());
            }
        }
        super.notifyObservers();
    }

    private Trail getMinimumTrail() {
        Trail minTrail = null;
        for (Station station: treeStations){
            for (Trail trail: trails){
                boolean connectsToTree = validateConnection(trail, station);
                if (connectsToTree) {
                    if (minTrail == null || trail.getEnvironmentalImpact() < minTrail.getEnvironmentalImpact()) {
                        minTrail = trail;
                    }
                }
            }
        }

        return minTrail;
    }

    private boolean validateConnection(Trail trail, Station station){
        return ((trail.getStart().equals(station) && !treeStations.contains(trail.getEnd())) ||
                (trail.getEnd().equals(station) && !treeStations.contains(trail.getStart())));

    }

    @Override
    public List<Trail> getTrails(){
        return this.treeTrails;
    }

    @Override
    public List<Station> getStations(){
        return this.treeStations;
    }
}
