package models.individual.random;

import gui.models.individual.random.RandomWalkMobilityModelGUI;
import gui.models.individual.random.RandomWaypointMobilityModelGui;
import org.contikios.cooja.Positioner;
import org.contikios.cooja.Simulation;
import org.contikios.cooja.interfaces.Position;
import utils.MobilityMote;

import java.awt.*;
import java.util.HashMap;
import java.util.Vector;

public class RandomWaypointMobilityModel extends RandomIMobilityModel {
    private final RandomWaypointMobilityModelGui ui = new RandomWaypointMobilityModelGui(100, 100, 1.0, 10, 5);
    private final HashMap<MobilityMote, Double> moteSpeed = new HashMap<>();
    private final HashMap<MobilityMote, double[]> moteDist = new HashMap<>();
    private final HashMap<MobilityMote, Integer> moteUpdates = new HashMap<>();

    private static final double SECONDS = Math.pow(10, 6);

    public RandomWaypointMobilityModel(Simulation simulation) {
        super(simulation);

        for (MobilityMote mote : getMotes()) {
            chooseNewDirection(mote);
            moteUpdates.put(mote, 0);
        }
    }

    @Override
    public String getMobilityModelName() {
        return "Random Waypoint";
    }

    @Override
    public Component getModelSettingsComponent() {
        return ui.getMainPanel();
    }

    @Override
    protected void moveMote(MobilityMote mote) {
        int updates = moteUpdates.get(mote);
        //if travelled distance is >= distance between previous and next position
        if (updates * getPeriod() / SECONDS * moteSpeed.get(mote) >= moteDist.get(mote)[0]) {
            //update speed and choose a new position
            chooseNewDirection(mote);

            updates = 0;
        }

        //distance to be travelled
        double dist = moteSpeed.get(mote) * getPeriod() / SECONDS;
        // {total distance, total dx, total dy}
        double[] moteDistInfo = moteDist.get(mote);

        //calculate new dx, dy to be travelled
        double dx = dist/moteDistInfo[0] * moteDistInfo[1];
        double dy =  dist/moteDistInfo[0] * moteDistInfo[2];

        mote.translate(dx, dy);
        moteUpdates.put(mote, updates + 1);
    }

    private void chooseNewDirection(MobilityMote mote) {
        //Save a new random speed
        double speed = random.nextDouble() * (ui.getSpeedMax() - ui.getSpeedMin()) + ui.getSpeedMin();
        moteSpeed.put(mote, speed);

        //Generate random position in simulation area
        double[] pos = {random.nextDouble() * ui.getArea().getAreaLength(), random.nextDouble() * ui.getArea().getAreaWidth()};
        Position current = mote.getInterfaces().getPosition();

        //Calculate total dx, dy and distance to be travelled
        double dx = pos[0] - current.getXCoordinate();
        double dy = pos[1] - current.getYCoordinate();
        double dist = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

        double[] directionInfo = {dist, dx, dy};

        moteDist.put(mote, directionInfo);
    }
}
