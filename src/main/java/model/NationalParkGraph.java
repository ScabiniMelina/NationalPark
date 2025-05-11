package model;

import java.util.ArrayList;
import java.util.List;

public class NationalParkGraph {
    private List<Station> stations;
    private List<Trail> trails;

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

    // Opcional: Método para calcular el MST (implementar con Prim o Kruskal)
    public List<Trail> calculateMinimumSpanningTree() {
        // TODO: Implementar algoritmo MST (por ejemplo, Kruskal)
        // Por ahora, devuelve todos los senderos
        return new ArrayList<>(trails);
    }
}