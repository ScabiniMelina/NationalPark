package view;

import controller.NationalPark;
import model.NationalParkGraph;
import model.Station;
import model.Trail;
import observer.NationalParkObserver;
import org.openstreetmap.gui.jmapviewer.*;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource;
import view.util.ColorPalette;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class NationalParkMap extends JFrame implements NationalParkObserver {


    private JMapViewer mapViewer;
    private JLabel timeLabel;
    private JLabel impactLabel;
    private final NationalPark nationalParkController;
    private final NationalParkGraph baseNationalParkGraph;
    private final NationalParkGraph krustalNationalParkGraph;
    private final NationalParkGraph primNationalParkGraph;

    public NationalParkMap(
            NationalPark nationalParkController,
            NationalParkGraph baseNationalParkGraph,
            NationalParkGraph krustalNationalParkGraph,
            NationalParkGraph primNationalParkGraph
    ) {
        this.nationalParkController = nationalParkController;
        this.baseNationalParkGraph = baseNationalParkGraph;
        this.krustalNationalParkGraph = krustalNationalParkGraph;
        this.primNationalParkGraph = primNationalParkGraph;

        setTitle("Senderos - Mapa del Parque Nacional Nahuel Huapi");
        setSize(1200, 800);
        setPreferredSize(new Dimension(1000, 800));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(ColorPalette.BACKGROUND_DARK_BLUE.getColor());
        mapViewer = createMap();

        add(createTitleLabel("Parque Nacional Nahuel Huapi"), BorderLayout.NORTH);
        add(mapViewer,BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);

        drawStations(baseNationalParkGraph);
        drawTrails(baseNationalParkGraph);
        centerMap(baseNationalParkGraph);
    }

    private void drawTrails(NationalParkGraph graph) {
        List<Station> stations = graph.getStations();
        System.out.println("Número de estaciones en stationMap: " + stations.size());

        mapViewer.removeAllMapPolygons();
        int drawnTrails = 0;
        for (Trail trail :  graph.getTrails()) {
            Station start = graph.getStationById(trail.getStart().getId());
            Station end = graph.getStationById(trail.getEnd().getId());
            if (start != null && end != null) {
                Coordinate startCoord = new Coordinate(start.getX(), start.getY());
                Coordinate endCoord = new Coordinate(end.getX(), end.getY());
                MapPolygonImpl trailLine = new MapPolygonImpl(startCoord, startCoord, endCoord);
                trailLine.setColor(getColorForImpact(trail.getEnvironmentalImpact()));
                trailLine.setBackColor(getColorForImpact(trail.getEnvironmentalImpact()));
                trailLine.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                mapViewer.addMapPolygon(trailLine);
                drawnTrails++;
                System.out.println("Sendero dibujado: " + start.getName() + " -> " + end.getName() +
                        ", Coordenadas: (" + startCoord.getLat() + "," + startCoord.getLon() + ") -> (" +
                        endCoord.getLat() + "," + endCoord.getLon() + ")");
            } else {
                System.out.println("Sendero omitido: startId=" + (trail.getStart() != null ? trail.getStart().getId() : "null") +
                        ", endId=" + (trail.getEnd() != null ? trail.getEnd().getId() : "null"));
            }
        }
        System.out.println("Polígonos en el mapa: " + mapViewer.getMapPolygonList().size());
        mapViewer.repaint();

    }

    private Color getColorForImpact(int environmentalImpact) {
        if (environmentalImpact <= 3) return ColorPalette.TRAIL_LOW_IMPACT.getColor();
        else if (environmentalImpact <= 6) return ColorPalette.TRAIL_MEDIUM_IMPACT.getColor();
        else return ColorPalette.TRAIL_HIGH_IMPACT.getColor();

    }

    private void drawStations(NationalParkGraph graph) {
        for (Station station : graph.getStations()) {
            Coordinate coordinate = new Coordinate(station.getX(), station.getY());
            MapMarker marker = new MapMarkerDot(station.getName(), coordinate);
            mapViewer.addMapMarker(marker);
        }
    }

    private JMapViewer createMap (){
        System.setProperty("http.agent", "Mozilla/5.0");
        JMapViewer mapViewer = new JMapViewer();
        add(mapViewer, BorderLayout.CENTER);
        return mapViewer;
    }


    private JLabel createTitleLabel(String title) {
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(ColorPalette.TEXT_WHITE_SOFT.getColor());
        titleLabel.setOpaque(true);
        titleLabel.setBackground(ColorPalette.BACKGROUND_DARK_BLUE.getColor());
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        return titleLabel;
    }

    private JButton createCustomButton(String title, Color backgroundColor, ActionListener actionListener) {
        JButton button = new JButton(title);
        button.setBackground(backgroundColor);
        button.setForeground(ColorPalette.TEXT_WHITE.getColor());
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.addActionListener(actionListener);
        return button;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        buttonPanel.setBackground(ColorPalette.BACKGROUND_DARK_BLUE.getColor());

        buttonPanel.add(createCustomButton(
                "Listar Todas las Estaciones",
                ColorPalette.BUTTON_STATIONS_PURPLE.getColor(),
                e -> getAllStations()
        ));

        buttonPanel.add(createCustomButton(
                "Dibujar Todas las Estaciones",
                ColorPalette.BUTTON_STATIONS_PURPLE.getColor(),
                e -> nationalParkController.reDrawOrginalPark(baseNationalParkGraph)
        ));

        buttonPanel.add(createCustomButton(
                "Calcular AGM (Kruskal)",
                ColorPalette.BUTTON_KRUSKAL_BLUE.getColor(),
                e -> nationalParkController.generateMinimumSpanningTree(krustalNationalParkGraph)
        ));

        buttonPanel.add(createCustomButton(
                "Calcular AGM (Prim)",
                ColorPalette.BUTTON_KRUSKAL_BLUE.getColor(),
                e -> nationalParkController.generateMinimumSpanningTree(primNationalParkGraph)
        ));

        return buttonPanel;
    }

    private JPanel createTimePanel() {
        timeLabel = new JLabel("Tiempo:");
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        timeLabel.setForeground(ColorPalette.TEXT_WHITE.getColor());
        timeLabel.setOpaque(true);
        timeLabel.setBackground(ColorPalette.BACKGROUND_DARK_BLUE.getColor());
        timeLabel.setPreferredSize(new Dimension(200, 30));

        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        timePanel.setBackground(ColorPalette.BACKGROUND_DARK_BLUE.getColor());
        timePanel.add(timeLabel);
        return timePanel;
    }
    private JPanel createImpactPanel() {
        impactLabel = new JLabel("Total impact:");
        impactLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        impactLabel.setForeground(ColorPalette.TEXT_WHITE.getColor());
        impactLabel.setOpaque(true);
        impactLabel.setBackground(ColorPalette.BACKGROUND_DARK_BLUE.getColor());
        impactLabel.setPreferredSize(new Dimension(200, 30));

        JPanel impactPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        impactPanel.setBackground(ColorPalette.BACKGROUND_DARK_BLUE.getColor());
        impactPanel.add(impactLabel);
        return impactPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(ColorPalette.BACKGROUND_DARK_BLUE.getColor());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        bottomPanel.add(createButtonPanel(), BorderLayout.CENTER);

        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
        eastPanel.setOpaque(false);
        eastPanel.add(createTimePanel());
        eastPanel.add(Box.createVerticalStrut(10));
        eastPanel.add(createImpactPanel());

        bottomPanel.add(eastPanel, BorderLayout.EAST);

        return bottomPanel;
    }

    public void updateExecutionTime(long duration) {
        timeLabel.setText("Tiempo: " + duration + " ms");
    }

    public void updateTotalImpact(int totalImpact) {
        impactLabel.setText("Total Impact: " + totalImpact);
    }

    private void getAllStations() {
        StringBuilder message = new StringBuilder("Estaciones:\n");
        for (Station station : baseNationalParkGraph.getStations()) {
            message.append(station.getName())
                    .append(" (ID: ").append(station.getId())
                    .append(", Coordenadas: ").append(station.getX())
                    .append(", ").append(station.getY()).append(")\n");
        }
        JOptionPane.showMessageDialog(this, message.toString(), "Lista de Estaciones", JOptionPane.INFORMATION_MESSAGE);
    }


    private void centerMap(NationalParkGraph graph) {
        java.util.List<Station> stations = graph.getStations();
        if (stations.size() == 0) return;

        double avgLat = 0, avgLon = 0;
        for (Station station : stations) {
            avgLat += station.getX();
            avgLon += station.getY();
        }
        avgLat /= stations.size();
        avgLon /= stations.size();

        mapViewer.setDisplayPosition(new Coordinate(avgLat, avgLon), 11);
    }

    public void updateTrails(NationalParkGraph graph) {
        drawTrails(graph);
        updateExecutionTime(graph.getExecutionTimeInNanoseconds());
        updateTotalImpact(graph.getTotalImpact());
        drawStations(graph);
        repaint();
    }

    @Override
    public void onModelChanged(NationalParkGraph graph) {
        updateTrails(graph);
    }
}