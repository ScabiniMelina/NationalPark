package model;

import model.utils.UnionFind;
import observer.NationalParkObserver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NationalParkGraph {
    private List<Station> stations;
    private List<Trail> trails;
    private List<NationalParkObserver> observers = new ArrayList<>();

    public NationalParkGraph(List<Station> stations, List<Trail> trails) {
        this.stations = new ArrayList<>(stations);
        this.trails = new ArrayList<>(trails);
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<Trail> getTrails() {
        return trails;
    }

    public Station getStationById(int id) {
        for (Station s : this.stations) {
            if (s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    public void addObserver(NationalParkObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for (NationalParkObserver o : observers) {
            o.onModelChanged();
        }
    }

    public void calculateMinimumSpanningTree() {
        List<Trail> mst = new ArrayList<>();
        trails.sort(Comparator.comparingInt(Trail::getEnvironmentalImpact));
        UnionFind uf = new UnionFind(stations.size());

        for (Trail trail : trails) {
            int startId = trail.getStart().getId();
            int endId = trail.getEnd().getId();
            if (uf.find(startId) != uf.find(endId)) {
                mst.add(trail);
                uf.union(startId, endId);
            }
        }
        this.trails = mst;
        notifyObservers();
    }
}