package model;

public class Trail {
    private Station start;
    private Station end;
    private int environmentalImpact;

    public Trail(Station start, Station end, int environmentalImpact) {
        this.start = start;
        this.end = end;
        this.environmentalImpact = environmentalImpact;
    }

    public Station getStart() {
        return start;
    }

    public Station getEnd() {
        return end;
    }

    public int getEnvironmentalImpact() {
        return environmentalImpact;
    }

    @Override
    public String toString() {
        return "Trail{start=" + start.getName() + ", end=" + end.getName() + ", impact=" + environmentalImpact + "}";
    }
}