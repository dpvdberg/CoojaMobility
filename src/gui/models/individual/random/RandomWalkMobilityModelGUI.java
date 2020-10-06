package gui.models.individual.random;

import gui.MobilityPluginPanel;
import models.individual.random.RandomWalkMobilityModel;

import javax.swing.*;

public class RandomWalkMobilityModelGUI {
    private JPanel mainPanel;
    private JSpinner speedMinSpinner;
    private JSpinner speedMaxSpinner;
    private JSpinner updateIntervalSpinner;
    private JRadioButton distanceBased;
    private JRadioButton timeBased;
    private JButton btnTestShowGroups;

    private static final double updateModelMaxTime = 10.0;
    private static final double updateModelMaxDistance = 500.0;

    public RandomWalkMobilityModelGUI(double speedMin, double speedMax, double updateInterval, boolean timeBasedUpdates) {
        SpinnerNumberModel minModel = new SpinnerNumberModel(speedMin, 1.0, speedMax, 1);
        speedMinSpinner.setModel(minModel);
        speedMaxSpinner.setModel(new SpinnerNumberModel(speedMax, 1.0, 100.0, 1));

        speedMaxSpinner.addChangeListener(e -> {
            minModel.setValue(Math.min((double) speedMaxSpinner.getValue(), (double) minModel.getValue()));
            minModel.setMaximum((double) speedMaxSpinner.getValue());
        });

        SpinnerNumberModel updateModel = new SpinnerNumberModel(updateInterval, 0.1, updateModelMaxTime, 0.1);
        updateIntervalSpinner.setModel(updateModel);

        ButtonGroup group = new ButtonGroup();
        group.add(timeBased);
        group.add(distanceBased);

        timeBased.addActionListener(e -> {
            updateModel.setValue(Math.min((double) updateIntervalSpinner.getValue(), updateModelMaxTime));
            updateModel.setMaximum(updateModelMaxTime);
        });

        distanceBased.addActionListener(e -> {
            updateModel.setValue(Math.min((double) updateIntervalSpinner.getValue(), updateModelMaxDistance));
            updateModel.setMaximum(updateModelMaxDistance);
        });

        timeBased.setSelected(timeBasedUpdates);
        distanceBased.setSelected(!timeBasedUpdates);

        btnTestShowGroups.addActionListener(e -> {
            MobilityPluginPanel.showGroupPanel();
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
