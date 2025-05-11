package view;

import controller.NationalPark;
import model.NationalParkGraph;
import model.Station;
import model.Trail;
import observer.NationalParkObserver;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;


import javax.swing.*;
import java.awt.*;
import java.util.List;

public class NationalParkView extends JFrame implements NationalParkObserver {

    private final JMapViewer mapViewer;
    private NationalParkGraph graph;
    private NationalPark controller; //TODO: Usarlo para llamar a generateMinimumSpanningTree() cuando se hace click

    public NationalParkView(NationalParkGraph graph, NationalPark controller) {
        this.graph = graph;
        this.graph.addObserver(this);
        this.controller = controller;

        setTitle("Senderos - Mapa del Parque Nacional Nahuel Huapi");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        // Configurar User-Agent para cumplir con la política de OpenStreetMap para solucionar error de carga de imagenes
       //System.setProperty("http.agent", "NahuelHuapiMapViewer/1.0 )");
        System.setProperty("http.agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
        // Inicializar JMapViewer
        mapViewer = new JMapViewer();
        //mapViewer.setTileSource(new OsmTileSource.Mapnik()); // Usar mapa de OpenStreetMap
        add(mapViewer,BorderLayout.CENTER);

        // Dibujar estaciones y senderos
        drawStations();
        drawTrails();

        // Centrar el mapa en el parque (promedio de coordenadas)
        centerMap();
    }

    private void drawStations() {
        for (Station station : graph.getStations()) {
            Coordinate coordinate = new Coordinate(station.getX(), station.getY());
            MapMarker marker = new MapMarkerDot(station.getName(), coordinate);
            mapViewer.addMapMarker(marker);
        }
    }

    private void drawTrails() {
        List<Station> stations = graph.getStations();
        List<Trail> trails = graph.getTrails();
        System.out.println("Número de estaciones en stationMap: " + stations.size());
        System.out.println("Número de senderos a dibujar: " + trails.size());

        mapViewer.removeAllMapPolygons(); // Limpiar polígonos previos
        int drawnTrails = 0;
        for (Trail trail : trails) {
            Station start = graph.getStationById(trail.getStart().getId());
            Station end = graph.getStationById(trail.getEnd().getId());
            if (start != null && end != null) {
                // Crear coordenadas para el polígono (inicio, intermedio, fin)
                Coordinate startCoord = new Coordinate(start.getX(), start.getY());
                Coordinate midCoord = new Coordinate(
                        (start.getX() + end.getX()) / 2,
                        (start.getY() + end.getY()) / 2
                );
                Coordinate endCoord = new Coordinate(end.getX(), end.getY());
                // Crear MapPolygonImpl con tres puntos
                MapPolygonImpl trailLine = new MapPolygonImpl(
                        startCoord,
                        startCoord,
                        endCoord
                );
                trailLine.setColor(getColorForImpact(trail.getEnvironmentalImpact()));
                trailLine.setBackColor(getColorForImpact(trail.getEnvironmentalImpact()));
                trailLine.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                mapViewer.addMapPolygon(trailLine);
                drawnTrails++;
                System.out.println("Sendero dibujado: " + start.getName() + " -> " + end.getName() +
                        ", Coordenadas: (" + startCoord.getLat() + "," + startCoord.getLon() + ") -> (" +
                        midCoord.getLat() + "," + midCoord.getLon() + ") -> (" +
                        endCoord.getLat() + "," + endCoord.getLon() + ")");
            } else {
                System.out.println("Sendero omitido: startId=" + (trail.getStart() != null ? trail.getStart().getId() : "null") +
                        ", endId=" + (trail.getEnd() != null ? trail.getEnd().getId() : "null"));
            }
        }
        System.out.println("Senderos dibujados: " + drawnTrails + " de " + trails.size());
        System.out.println("Polígonos en el mapa: " + mapViewer.getMapPolygonList().size());
        mapViewer.repaint();
    }

    private Color getColorForImpact(int impact) {
        // Escalar colores según impacto ambiental (1 a 10)
        if (impact <= 3) return Color.GREEN;
        else if (impact <= 6) return Color.YELLOW;
        else return Color.RED;
    }

    private void centerMap() {
        List<Station> stations = graph.getStations();
        if (stations.isEmpty()) return;

        // Calcular el centro promedio de las coordenadas
        double avgLat = 0, avgLon = 0;
        for (Station station : stations) {
            avgLat += station.getX();
            avgLon += station.getY();
        }
        avgLat /= stations.size();
        avgLon /= stations.size();

        // Centrar el mapa y ajustar el zoom
        mapViewer.setDisplayPosition(new Coordinate(avgLat, avgLon), 11);
    }

    @Override
    public void onModelChanged() {
        //mapViewer.removeAllMapPolygons(); // Limpiar senderos existentes
        drawStations(); // Redibujar estaciones
        //this.trails.clear();
        //this.trails.addAll(newTrails);
        drawTrails(); // Dibujar nuevos senderos
        repaint();
    }
}
