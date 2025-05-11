package model;

import observer.NationalParkObserver;

import java.util.ArrayList;
import java.util.List;

public class NationalParkGraph {
    protected List<Station> stations;
    protected List<Trail> trails;
    private List<NationalParkObserver> observers = new ArrayList<>();


    public NationalParkGraph(List<Station> stations, List<Trail> trails) {
        this.stations = new ArrayList<>(stations);
        this.trails = new ArrayList<>(trails);
    }
    public void addObserver(NationalParkObserver observer) {
        observers.add(observer);
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

    public long getExecutionTimeInMiliseconds() {
        throw new IllegalArgumentException("Excecution time is not provided for this type of graph");
    }
}