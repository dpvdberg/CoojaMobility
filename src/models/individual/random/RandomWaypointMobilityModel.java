package models.individual.random;

import gui.models.individual.random.RandomWalkMobilityModelGUI;
import org.contikios.cooja.Simulation;
import utils.MobilityMote;

import java.awt.*;
import java.util.HashMap;
import java.util.Vector;

public class RandomWaypointMobilityModel extends RandomIMobilityModel {
    private final RandomWalkMobilityModelGUI ui = new RandomWalkMobilityModelGUI(1.0, 10.0, 1.0, true);
    private final HashMap<MobilityMote, Double> moteSpeed = new HashMap<>();
    private final HashMap<MobilityMote, Vector<Double>> moteDest = new HashMap<>();
    private int updates = 0;

    public RandomWaypointMobilityModel(Simulation simulation) {
        super(simulation);
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
//        if (ui.getTimeBased()) {
//            //If amount of time passed (in nanoseconds) is the same as update interval in nanoseconds
//            if (updates * getPeriod() == ui.getUpdateInterval() * Math.pow(10, 9)) {
//                //update speed and direction of mote
//               chooseNewDirection(mote);
//
//                updates = 0;
//            }
//        } else {
//            //if travelled distance is >= update interval distance
//            if (updates * getPeriod() * moteSpeed.get(mote) >= ui.getUpdateInterval()) {
//                chooseNewDirection(mote);
//
//                updates = 0;
//            }
//        }
//
//        //Convert period from nanoseconds to seconds
//        double dist = moteSpeed.get(mote) * getPeriod()/(Math.pow(10, 9));
//
//        double dx = dist * Math.sin(moteDirec.get(mote));
//        double dy = dist * Math.sin(moteDirec.get(mote));
//        mote.translate(dx, dy);
//        updates++;
//    }
//
//    private void chooseNewDirection(MobilityMote mote) {
//        double speed = random.nextDouble() * (ui.getSpeedMax() - ui.getSpeedMin()) + ui.getSpeedMin();
//        moteSpeed.put(mote, speed);
//        double direction = random.nextDouble() * 2 * Math.PI;
//        moteDirec.put(mote, direction);
    }
}
