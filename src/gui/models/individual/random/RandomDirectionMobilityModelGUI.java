package gui.models.individual.random;

import gui.models.SimulationArea;

import javax.swing.*;

public class RandomDirectionMobilityModelGUI {
    private JPanel mainPanel;
    private JSpinner speedMinSpinner;
    private JSpinner speedMaxSpinner;
    private JSpinner pauseTimeSpinner;
    private JPanel simulationArea;
    private final SimulationArea area;

    public RandomDirectionMobilityModelGUI(double speedMin, double speedMax, double maxPauseTime) {
        area = new SimulationArea(100, 100);
        simulationArea.add(area.getMainPanel());

        SpinnerNumberModel minModel = new SpinnerNumberModel(speedMin, 1.0, speedMax, 1);
        speedMinSpinner.setModel(minModel);
        speedMaxSpinner.setModel(new SpinnerNumberModel(speedMax, 1.0, 100.0, 1));

        speedMaxSpinner.addChangeListener(e -> {
            minModel.setValue(Math.min((double) speedMaxSpinner.getValue(), (double) minModel.getValue()));
            minModel.setMaximum((double) speedMaxSpinner.getValue());
        });

        pauseTimeSpinner.setModel(new SpinnerNumberModel(maxPauseTime, 0, 5, 0.1));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public SimulationArea getSimulationArea() {
        return area;
    }

    public double getSpeedMin() {
        return (double) speedMinSpinner.getValue();
    }

    public double getSpeedMax() {
        return (double) speedMaxSpinner.getValue();
    }

    public double getMaxPauseTime() {
        return (double) pauseTimeSpinner.getValue();
    }
}
