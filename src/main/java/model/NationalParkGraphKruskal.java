package model;

import model.util.algorithms.UnionFind;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NationalParkGraphKruskal extends NationalParkGraph {

    public NationalParkGraphKruskal(List<Station> stations, List<Trail> trails) {
        super(stations, trails);
    }

    @Override
    public void calculateMinimumSpanningTree() {
        List<Trail> mst = new ArrayList<>();

        super.trails.sort(Comparator.comparingInt(Trail::getEnvironmentalImpact));
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
        super.notifyObservers();
    }
}
