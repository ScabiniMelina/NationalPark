package model;

public class Station {
    private int id;
    private String name;
    private double x;
    private double y;

    public Station(int id, String name, double x, double y) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("The station name cannot be null or empty");
        }
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Station{id=" + id + ", name='" + name + "', x=" + x + ", y=" + y + "}";
    }
}