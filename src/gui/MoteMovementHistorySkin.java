package gui;

import org.contikios.cooja.ClassDescription;
import org.contikios.cooja.Mote;
import org.contikios.cooja.Simulation;
import org.contikios.cooja.plugins.Visualizer;
import org.contikios.cooja.plugins.VisualizerSkin;
import utils.MobilityMote;
import utils.Vector;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ClassDescription("Position history")
public class MoteMovementHistorySkin implements VisualizerSkin {
    private static Map<MobilityMote, List<Vector>> mobilityHistoryMap;
    private Simulation simulation = null;
    private Visualizer visualizer = null;
    private static boolean active = false;

    @Override
    public void setActive(Simulation simulation, Visualizer visualizer) {
        System.out.print("Mote history skin activated");
        this.simulation = simulation;
        this.visualizer = visualizer;
    }

    @Override
    public void setInactive() {
        System.out.print("Mote history skin set to inactive");
    }

    @Override
    public Color[] getColorOf(Mote mote) {
        return null;
    }

    @Override
    public void paintBeforeMotes(Graphics graphics) {

    }

    public Color[] generateColors(int n)
    {
        Color[] cols = new Color[n];
        for(int i = 0; i < n; i++)
        {
            cols[i] = Color.getHSBColor((float) i / (float) n, 0.85f, 0.8f);
        }
        return cols;
    }

    @Override
    public void paintAfterMotes(Graphics graphics) {
        if (active) {
            Color[] colors = generateColors(mobilityHistoryMap.size());
            List<List<Vector>> positionHistory = mobilityHistoryMap.keySet()
                    .stream().sorted(Comparator.comparingInt(MobilityMote::getID))
                    .map(m -> mobilityHistoryMap.get(m))
                    .collect(Collectors.toList());

            for (int i = 0; i < positionHistory.size(); i++) {
                List<Vector> positions = positionHistory.get(i);

                for (int j = 0; j < positions.size() - 1; j++) {
                    Point start = visualizer.transformPositionToPixel(positions.get(j).getX(), positions.get(j).getY(), 0);
                    Point stop = visualizer.transformPositionToPixel(positions.get(j+1).getX(), positions.get(j+1).getY(), 0);
                    graphics.setColor(colors[i]);
                    graphics.drawLine(start.x, start.y, stop.x, stop.y);
                }
            }
        }
    }

    @Override
    public Visualizer getVisualizer() {
        return visualizer;
    }

    public static void setMobilityHistoryMap(Map<MobilityMote, List<Vector>> mobilityHistoryMap) {
        MoteMovementHistorySkin.mobilityHistoryMap = mobilityHistoryMap;
    }

    public static void setActive(boolean active) {
        MoteMovementHistorySkin.active = active;
    }
}
