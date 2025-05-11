package model;

import model.util.UnionFind;

import java.util.ArrayList;
import java.util.List;

public class NationalParkGraphKruskal extends NationalParkGraph {
    List<Trail> treeTrails;
    long executionTimeInNanoseconds;
    int totalImpact;

    public NationalParkGraphKruskal(List<Station> stations, List<Trail> trails) {
        super(stations, trails);
        this.treeTrails = new ArrayList<>();
    }

    @Override
    public long getExecutionTimeInNanoseconds() {
        return executionTimeInNanoseconds;
    }

    @Override
    public List<Trail> getTrails() {
        return this.treeTrails;
    }

    @Override
    public int getTotalImpact(){
        return totalImpact;
    }

    @Override
    public void calculateMinimumSpanningTree() {
        long startTime = System.nanoTime();

        treeTrails.clear();

        UnionFind<Station> unionFind = new UnionFind<>();
        for (Station s : stations) {
            unionFind.add(s);
        }

        for (int i = 1; i <= stations.size(); i++){
            Trail minTrail = getMinimunTrail(unionFind);
            if (minTrail == null) break;
            treeTrails.add(minTrail);

            Station start = minTrail.getStart();
            Station end = minTrail.getEnd();
            unionFind.union(start, end);
        }
        long endTime = System.nanoTime();
        executionTimeInNanoseconds = endTime - startTime;
        fillTotalImpact();
        super.notifyObservers();
    }

    private Trail getMinimunTrail(UnionFind uf) {
        Trail minTrail = null;
        for (Trail trail: super.trails){
            if (!treeTrails.contains(trail)) {
                Station start = trail.getStart();
                Station end = trail.getEnd();

                if (!uf.connected(start, end)) {
                    if (minTrail == null || trail.getEnvironmentalImpact() < minTrail.getEnvironmentalImpact()) {
                        minTrail = trail;
                    }
                }
            }
        }
        return minTrail;
    }

    private void fillTotalImpact(){
        totalImpact = 0;
        for (Trail trail: treeTrails){
            totalImpact += trail.getEnvironmentalImpact();
        }
    }
}
