package models.individual.random.models;

import models.individual.random.RandomIMobilityModel;
import org.contikios.cooja.Simulation;
import utils.MobilityMote;

import javax.swing.*;
import java.awt.*;

public class RandomWalkMobilityModel extends RandomIMobilityModel {
    private double speedMin = 10.0;
    private double speedMax = 1.0;
    private double interval = 1.0;
    private boolean timeBased = true;
    private JSpinner maxOffsetSpinner = new JSpinner();

    public RandomWalkMobilityModel(Simulation simulation) {
        super(simulation);
    }

    @Override
    public String getMobilityModelName() {
        return "Random walk";
    }

    @Override
    public Component getModelSettingsComponent() {
        JPanel panel = new JPanel();

//        SpinnerNumberModel model = new SpinnerNumberModel(maxOffset, 0, 1000.0, 1);
//        maxOffsetSpinner.setModel(model);
//        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(maxOffsetSpinner);
//        maxOffsetSpinner.setEditor(editor);

        return panel;
    }

    @Override
    protected void moveMote(MobilityMote mote) {
        double speed = random.nextDouble() * (speedMax - speedMin) + speedMin;
        double direction = random.nextDouble() * 2 * Math.PI;

        if (timeBased) {
            double dist = speed * interval;
            double dx = dist * Math.sin(direction);
            double dy = dist * Math.sin(direction);
            mote.translate(dx, dy);
        } else {

        }

    }
}
