package gui.models;

import javax.swing.*;

public class SimulationArea {

    private JSpinner areaLength;
    private JSpinner areaWidth;
    private JPanel mainPanel;

    public SimulationArea(double length, double width) {
        areaLength.setModel(new SpinnerNumberModel(length, 1.0, 1000.0, 1));
        areaWidth.setModel(new SpinnerNumberModel(width, 1.0, 1000.0, 1));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public double getAreaLength() {
        return (double) areaLength.getValue();
    }

    public double getAreaWidth() {
        return (double) areaWidth.getValue();
    }
}
