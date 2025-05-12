package model;

import observer.NationalParkObserver;

import java.util.ArrayList;
import java.util.List;

public class NationalParkGraph {
    long executionTimeInNanoseconds; //TODO: ESTA BIEN QUE ESTO ESTE ACA? DELEGACION?
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
        this.stations = new ArrayList<>(stations);
        this.trails = new ArrayList<>(trails);
        calculateTotalImpact();
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
        return 0;
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
}