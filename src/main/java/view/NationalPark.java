package view;

import model.Station;
import model.Trail;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource;
/*
import org.openstreetmap.gui.jmapviewer.*;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource;
*/

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NationalPark extends JFrame {

    private final JMapViewer mapViewer;
    private final List<Station> stations;
    private final List<Trail> trails;

    public NationalPark(List<Station> stations, List<Trail> trails) {
        this.stations = stations;
        this.trails = trails;

        // Configurar la ventana
        setTitle("Mapa del Parque Nacional Nahuel Huapi");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configurar User-Agent para cumplir con la política de OpenStreetMap
        System.setProperty("http.agent", "NahuelHuapiMapViewer/1.0 (contacto@tudominio.com)");

        // Inicializar JMapViewer
        mapViewer = new JMapViewer();
        mapViewer.setTileSource(new OsmTileSource.Mapnik()); // Usar mapa de OpenStreetMap
        add(mapViewer);

        // Dibujar estaciones y senderos
        drawStations();
        drawTrails();

        // Centrar el mapa en el parque (promedio de coordenadas)
        centerMap();
    }

    private void drawStations() {
        for (Station station : stations) {
            // Crear un marcador para cada estación
            Coordinate coord = new Coordinate(station.getX(), station.getY());
            MapMarker marker = new MapMarkerDot(station.getName(), coord);
            mapViewer.addMapMarker(marker);
        }
    }

    private void drawTrails() {
        // Crear un mapa para buscar estaciones por ID
        java.util.Map<Integer, Station> stationMap = new java.util.HashMap<>();
        for (Station station : stations) {
            stationMap.put(station.getId(), station);
        }

        for (Trail trail : trails) {
            Station start = stationMap.get(trail.getStart().getId());
            Station end = stationMap.get(trail.getEnd().getId());
            if (start != null && end != null) {
                Coordinate startCoord = new Coordinate(start.getX(), start.getY());
                Coordinate endCoord = new Coordinate(end.getX(), end.getY());
                // Crear una línea (polígono simple) para el sendero
                List<Coordinate> points = new ArrayList<>();
                points.add(startCoord);
                points.add(endCoord);
                MapPolygonImpl trailLine = new MapPolygonImpl(points);
                // Opcional: Colorear según impacto ambiental
                trailLine.setColor(getColorForImpact(trail.getEnvironmentalImpact()));
                trailLine.setStroke(new BasicStroke(2)); // Grosor de la línea
                mapViewer.addMapPolygon(trailLine);
            }
        }
    }

    private Color getColorForImpact(int impact) {
        // Escalar colores según impacto ambiental (1 a 10)
        if (impact <= 3) return Color.GREEN; // Bajo impacto
        else if (impact <= 6) return Color.YELLOW; // Impacto medio
        else return Color.RED; // Alto impacto
    }

    private void centerMap() {
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
        mapViewer.setDisplayPosition(new Coordinate(avgLat, avgLon), 12);
    }

    // Método para actualizar la vista (por ejemplo, para mostrar un MST)
    public void updateTrails(List<Trail> newTrails) {
        mapViewer.removeAllMapPolygons(); // Limpiar senderos existentes
        drawStations(); // Redibujar estaciones
        this.trails.clear();
        this.trails.addAll(newTrails);
        drawTrails(); // Dibujar nuevos senderos
        repaint();
    }
}
