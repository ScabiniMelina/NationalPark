package model;

import model.util.UnionFind;
import observer.NationalParkObserver;

import java.util.ArrayList;
import java.util.List;

public class NationalParkGraph {
    long executionTimeInNanoseconds = 0;
    protected List<Station> stations;
    protected List<Trail> trails;
    int totalImpact;
    private List<NationalParkObserver> observers = new ArrayList<>();

    public NationalParkGraph(List<Station> stations, List<Trail> trails) {
        if (stations == null ) {
            throw new IllegalArgumentException("Stations cannot be null");
        }
        if (trails == null ) {
            throw new IllegalArgumentException("Trails cannot be null");
        }
        if (isConnectedUnionFind(stations, trails)){
            this.stations = new ArrayList<>(stations);
            this.trails = new ArrayList<>(trails);
            calculateTotalImpact();
        } else {
            throw new RuntimeException("The graph is not connected. Minimum spanning tree cannot be fully constructed.");
        }
    }

    public void addObserver(NationalParkObserver observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    public void notifyObservers() {
        for (NationalParkObserver o : observers) {
            o.onModelChanged(this);
        }
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

    public void calculateMinimumSpanningTree() {
        throw new IllegalArgumentException("Calculate MST is not provided for this type of graph");
    }

    public long getExecutionTimeInNanoseconds() {
        return executionTimeInNanoseconds;
    }

    public int getTotalImpact() {
        return totalImpact;
    }

    public void calculateTotalImpact() {
        int sum = 0;
        for (Trail trail : trails) {
            sum += trail.getEnvironmentalImpact();
        }
        totalImpact =  sum;
    }

    public boolean isConnectedUnionFind(List<Station> stations, List<Trail> trails) {
        UnionFind<Station> uf = new UnionFind<>();
        for (Station s : stations) uf.add(s);

        for (Trail t : trails) {
            uf.union(t.getStart(), t.getEnd());
        }

        Station root = uf.find(stations.get(0));
        for (Station s : stations) {
            if (!uf.find(s).equals(root)) return false;
        }
        return true;
    }
}