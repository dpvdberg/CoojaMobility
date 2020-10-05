package gui.models.individual.random;

import models.individual.random.RandomWalkMobilityModel;

import javax.swing.*;

public class RandomWalkMobilityModelGUI {
    private JPanel mainPanel;
    private JSpinner speedMinSpinner;
    private JSpinner speedMaxSpinner;
    private JSpinner updateIntervalSpinner;
    private JCheckBox chkBoxTimeBased;

    public RandomWalkMobilityModelGUI(double speedMin, double speedMax, double updateInterval, boolean timeBased) {
        SpinnerNumberModel minModel = new SpinnerNumberModel(speedMin, 1.0, 100.0, 1);
        speedMinSpinner.setModel(minModel);
        speedMaxSpinner.setModel(new SpinnerNumberModel(speedMax, 1.0, 100.0, 1));

        speedMaxSpinner.addChangeListener(e -> {
            minModel.setValue(Math.min((double) speedMaxSpinner.getValue(), (double) minModel.getValue()));
            minModel.setMaximum((double) speedMaxSpinner.getValue());
        });

        updateIntervalSpinner.setModel(new SpinnerNumberModel(updateInterval, 1.0, 10.0, 0.5));

        chkBoxTimeBased.setSelected(timeBased);
    }

    public double getSpeedMin() {
        return (double) speedMinSpinner.getValue();
    }

    public double getSpeedMax() {
        return (double) speedMaxSpinner.getValue();
    }

    public double getUpdateInterval() {
        return (double) updateIntervalSpinner.getValue();
    }

    public boolean getTimeBased() {
        return chkBoxTimeBased.isSelected();
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }
}
