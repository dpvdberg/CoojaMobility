package models.individual.random;

import gui.models.individual.random.RandomWalkMobilityModelGUI;
import models.individual.random.RandomIMobilityModel;
import org.contikios.cooja.Simulation;
import utils.MobilityMote;

import javax.swing.*;
import java.awt.*;

public class RandomWalkMobilityModel extends RandomIMobilityModel {
    private final RandomWalkMobilityModelGUI ui = new RandomWalkMobilityModelGUI(1.0, 10.0, 1.0, true);

    public RandomWalkMobilityModel(Simulation simulation) {
        super(simulation);
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
        double speed = random.nextDouble() * (ui.getSpeedMax() - ui.getSpeedMin()) + ui.getSpeedMin();
        double direction = random.nextDouble() * 2 * Math.PI;

        if (ui.getTimeBased()) {
            double dist = speed * ui.getUpdateInterval();
            double dx = dist * Math.sin(direction);
            double dy = dist * Math.sin(direction);
            mote.translate(dx, dy);
        } else {

        }

    }
}
