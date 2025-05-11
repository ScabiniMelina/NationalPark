package view;

import controller.NationalPark;
import model.NationalParkGraph;
import model.Station;
import model.Trail;
import org.openstreetmap.gui.jmapviewer.*;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NationalParkMap extends JFrame {

    private final JMapViewer mapViewer;
    private List<Station> stations;
    private List<Trail> trails;
    private JLabel timeLabel;

    private NationalPark nationalParkController;

    public NationalParkMap(NationalPark nationalParkController) {
        this.nationalParkController = nationalParkController;

        setTitle("Senderos - Mapa del Parque Nacional Nahuel Huapi");
        setSize(1200, 800);
        setPreferredSize(new Dimension(1000, 800));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(30, 42, 68)); // Fondo azul oscuro

        JLabel titleLabel = new JLabel("Parque Nacional Nahuel Huapi", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(230, 240, 250)); // Texto blanco suave
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(30, 42, 68)); // Fondo azul más claro
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);


        System.setProperty("http.agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
        mapViewer = new JMapViewer();
        mapViewer.setTileSource(new OsmTileSource.Mapnik());

        add(mapViewer, BorderLayout.CENTER);

        // Panel inferior para botones y tiempo
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(30, 42, 68)); // Fondo gris más oscuro
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        buttonPanel.setBackground(new Color(30, 42, 68));

        JButton showStationsButton = new JButton("Ver Todas las Estaciones");
        showStationsButton.setBackground(new Color(142, 68, 173));
        showStationsButton.setForeground(Color.WHITE);
        showStationsButton.setFont(new Font("Arial", Font.PLAIN, 14));
        showStationsButton.addActionListener(e -> showAllStations());
        buttonPanel.add(showStationsButton);

        JButton kruskalButton = new JButton("Calcular AGM (Kruskal)");
        kruskalButton.setBackground(new Color(77, 168, 218)); // Azul
        kruskalButton.setForeground(Color.WHITE);
        kruskalButton.setFont(new Font("Arial", Font.PLAIN, 14));
        //kruskalButton.addActionListener(e -> controller.showMinimumSpanningTree("Kruskal"));
        buttonPanel.add(kruskalButton);

        JButton primButton = new JButton("Calcular AGM (Prim)");
        primButton.setBackground(new Color(77, 168, 218));
        primButton.setForeground(Color.WHITE);
        primButton.setFont(new Font("Arial", Font.PLAIN, 14));
        //primButton.addActionListener(e -> controller.showMinimumSpanningTree("Prim"));
        buttonPanel.add(primButton);

        bottomPanel.add(buttonPanel, BorderLayout.CENTER);

        // Sección para mostrar el tiempo
        // Sección para mostrar el tiempo
        timeLabel = new JLabel("Tiempo: N/A");
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        timeLabel.setForeground(Color.white); // Texto gris oscuro
        timeLabel.setOpaque(true);
        timeLabel.setBackground(new Color(30, 42, 68)); // Fondo gris claro
        timeLabel.setPreferredSize(new Dimension(200, 30));
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        timePanel.setBackground(new Color(30, 42, 68));
        timePanel.add(timeLabel);
        bottomPanel.add(timePanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);


        add(bottomPanel, BorderLayout.SOUTH);

        // Dibujar estaciones y senderos iniciales
        drawStations();
        drawTrails();
        centerMap();
    }

    public void updateExecutionTime(long duration) {
        timeLabel.setText("Tiempo: " + duration + " ms");
    }

    private void showAllStations() {
        StringBuilder message = new StringBuilder("Estaciones:\n");
        for (Station station : stations) {
            message.append(station.getName())
                    .append(" (ID: ").append(station.getId())
                    .append(", Coordenadas: ").append(station.getX())
                    .append(", ").append(station.getY()).append(")\n");
        }
        JOptionPane.showMessageDialog(this, message.toString(), "Lista de Estaciones", JOptionPane.INFORMATION_MESSAGE);
    }

    private void drawStations() {
        NationalParkGraph graph = nationalParkController.getGraph();
        for (Station station : graph.getStations()) {
            Coordinate coordinate = new Coordinate(station.getX(), station.getY());
            MapMarker marker = new MapMarkerDot(station.getName(), coordinate);
            mapViewer.addMapMarker(marker);
        }
    }

    private void drawTrails() {
        NationalParkGraph graph = nationalParkController.getGraph();
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
        if (impact <= 3) return new Color(106, 191, 75);
        else if (impact <= 6) return new Color(212, 184, 60);
        else return new Color(230, 57, 70);
    }

    private void centerMap() {
        NationalParkGraph graph = nationalParkController.getGraph();
        List<Station> stations = graph.getStations();
        if (stations.isEmpty()) return;

        double avgLat = 0, avgLon = 0;
        for (Station station : stations) {
            avgLat += station.getX();
            avgLon += station.getY();
        }
        avgLat /= stations.size();
        avgLon /= stations.size();

        mapViewer.setDisplayPosition(new Coordinate(avgLat, avgLon), 11);
    }
    public void updateTrails(List<Trail> newTrails) {
        this.trails.clear();
        this.trails.addAll(newTrails);
        drawStations();
        drawTrails();
        repaint();
    }
}