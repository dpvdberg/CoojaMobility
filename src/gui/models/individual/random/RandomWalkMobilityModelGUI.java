package gui.models.individual.random;

import models.individual.random.RandomWalkMobilityModel;

import javax.swing.*;

public class RandomWalkMobilityModelGUI {
    private JPanel mainPanel;
    private JSpinner speedMinSpinner;
    private JSpinner speedMaxSpinner;
    private JSpinner updateIntervalSpinner;
    private JRadioButton distanceBased;
    private JRadioButton timeBased;

    public RandomWalkMobilityModelGUI(double speedMin, double speedMax, double updateInterval, boolean timeBasedUpdates) {
        SpinnerNumberModel minModel = new SpinnerNumberModel(speedMin, 1.0, 100.0, 1);
        speedMinSpinner.setModel(minModel);
        speedMaxSpinner.setModel(new SpinnerNumberModel(speedMax, 1.0, 100.0, 1));

        speedMaxSpinner.addChangeListener(e -> {
            minModel.setValue(Math.min((double) speedMaxSpinner.getValue(), (double) minModel.getValue()));
            minModel.setMaximum((double) speedMaxSpinner.getValue());
        });

        SpinnerNumberModel updateModel = new SpinnerNumberModel(updateInterval, 0.1, 3.0, 0.1);
        updateIntervalSpinner.setModel(updateModel);

        ButtonGroup group = new ButtonGroup();
        group.add(timeBased);
        group.add(distanceBased);

        timeBased.setSelected(timeBasedUpdates);

        timeBased.addChangeListener(e -> {
            updateModel.setValue(Math.min((double) updateIntervalSpinner.getValue(), 3.0));
            updateModel.setMaximum(2.0);
        });

        distanceBased.addChangeListener(e -> {
            updateModel.setValue(Math.min((double) updateIntervalSpinner.getValue(), 100.0));
            updateModel.setMaximum(100.0);
        });

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
        return timeBased.isSelected();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
