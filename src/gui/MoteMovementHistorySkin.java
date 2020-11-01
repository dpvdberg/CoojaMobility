package gui;

import org.contikios.cooja.ClassDescription;
import org.contikios.cooja.Mote;
import org.contikios.cooja.Simulation;
import org.contikios.cooja.plugins.Visualizer;
import org.contikios.cooja.plugins.VisualizerSkin;
import utils.MobilityMote;
import utils.Vector;

import java.awt.*;
import java.util.List;
import java.util.Map;

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

    @Override
    public void paintAfterMotes(Graphics graphics) {
        if (active) {
            for (List<Vector> positions : mobilityHistoryMap.values()) {
                for (int i = 0; i < positions.size() - 1; i++) {
                    Point start = visualizer.transformPositionToPixel(positions.get(i).getX(), positions.get(i).getY(), 0);
                    Point stop = visualizer.transformPositionToPixel(positions.get(i+1).getX(), positions.get(i+1).getY(), 0);
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
