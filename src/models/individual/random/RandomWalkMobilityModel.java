package models.individual.random;

import gui.models.individual.random.RandomWalkMobilityModelGUI;
import models.individual.random.RandomIMobilityModel;
import org.contikios.cooja.Simulation;
import utils.MobilityMote;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class RandomWalkMobilityModel extends RandomIMobilityModel {
    private final RandomWalkMobilityModelGUI ui = new RandomWalkMobilityModelGUI(1.0, 10.0, 1.0, true);
    private final HashMap<MobilityMote, Double> moteSpeed = new HashMap<>();
    private final HashMap<MobilityMote, Double> moteDirec = new HashMap<>();
    private final HashMap<MobilityMote, Integer> moteUpdates = new HashMap<>();

    private static final double SECONDS = Math.pow(10, 6);

    public RandomWalkMobilityModel(Simulation simulation) {
        super(simulation);

        for (MobilityMote mote : getMotes()) {
            chooseNewDirection(mote);
            moteUpdates.put(mote, 0);
        }
    }

    @Override
    public String getMobilityModelName() {
        return "Random walk";
    }

    @Override
    public Component getModelSettingsComponent() {
        return ui.getMainPanel();
    }

    @Override
    protected void moveMote(MobilityMote mote) {
        int updates = moteUpdates.get(mote);

        if (ui.getTimeBased()) {
            //If amount of time passed (in microseconds) is the same as update interval in microseconds
            if (updates * getPeriod() >= ui.getUpdateInterval() * SECONDS) {
                //update speed and direction of mote
                chooseNewDirection(mote);

                updates = 0;
            }
        } else {
            //if travelled distance is >= update interval distance
            if (updates * getPeriod() / SECONDS * moteSpeed.get(mote) >= ui.getUpdateInterval()) {
                chooseNewDirection(mote);

                updates = 0;
            }
        }

        //Convert period from microseconds to seconds
        double dist = moteSpeed.get(mote) * getPeriod() / SECONDS;

        double dx = dist * Math.cos(moteDirec.get(mote));
        double dy = dist * Math.sin(moteDirec.get(mote));
        mote.translate(dx, dy);
        moteUpdates.put(mote, updates + 1);
    }

    private void chooseNewDirection(MobilityMote mote) {
        double speed = random.nextDouble() * (ui.getSpeedMax() - ui.getSpeedMin()) + ui.getSpeedMin();
        moteSpeed.put(mote, speed);
        double direction = random.nextDouble() * 2 * Math.PI;
        moteDirec.put(mote, direction);
    }
}
