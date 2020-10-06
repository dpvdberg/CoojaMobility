package gui.models.individual.random;

import gui.models.SimulationArea;

import javax.swing.*;

public class RandomWaypointMobilityModelGui {
    private JPanel mainPanel;
    private JPanel simulationArea;
    private JSpinner speedMinSpinner;
    private JSpinner speedMaxSpinner;
    private JSpinner pauseTimeSpinner;
    private SimulationArea area;

    public RandomWaypointMobilityModelGui(double areaLength, double areaWidth, double speedMin, double speedMax, double pauseTime) {
        area = new SimulationArea(areaLength, areaWidth);
        simulationArea.add(area.getMainPanel());

        SpinnerNumberModel minModel = new SpinnerNumberModel(speedMin, 1.0, speedMax, 1);
        speedMinSpinner.setModel(minModel);
        speedMaxSpinner.setModel(new SpinnerNumberModel(speedMax, 1.0, 100.0, 1));

        speedMaxSpinner.addChangeListener(e -> {
            minModel.setValue(Math.min((double) speedMaxSpinner.getValue(), (double) minModel.getValue()));
            minModel.setMaximum((double) speedMaxSpinner.getValue());
        });

        pauseTimeSpinner.setModel(new SpinnerNumberModel(pauseTime, 0, 5, 1));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
    public SimulationArea getArea() {
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
