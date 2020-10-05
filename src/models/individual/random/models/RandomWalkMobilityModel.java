package models.individual.random.models;

import models.individual.random.RandomIMobilityModel;
import org.contikios.cooja.Simulation;
import utils.MobilityMote;

import javax.swing.*;
import java.awt.*;

public class RandomWalkMobilityModel extends RandomIMobilityModel {
    private double maxOffset = 10.0;
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
        double dx = random.nextDouble() * maxOffset;
        double dy = random.nextDouble() * maxOffset;
        mote.translate(dx, dy);
    }
}
