package gui.models.group.referencePoint;

import models.individual.IndividualMobilityModel;

import javax.swing.*;

public class ReferencePointMobilityModelGUI {
    private JPanel mainPanel;
    private JPanel referencePointMobilityModelPanel;
    private JSpinner minDeviationSpinner;
    private JSpinner maxDeviationSpinner;
    private IndividualMobilityModel referencePointModel;

    public ReferencePointMobilityModelGUI(double minDeviation, double maxDeviation) {
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }

    public double getMinDeviationSpinner() {
        return (double) minDeviationSpinner.getValue();
    }

    public double getMaxDeviationSpinner() {
        return (double) maxDeviationSpinner.getValue();
    }
}
