package gui.models.group.referencePoint;

import models.individual.IndividualMobilityModel;

import javax.swing.*;

public class ReferencePointMobilityModelGUI {
    private JPanel mainPanel;
    private JPanel referencePointMobilityModelPanel;
    private JSpinner minDeviationSpinner;
    private JSpinner maxDeviationSpinner;
    private IndividualMobilityModel referencePointModel;

    public ReferencePointMobilityModelGUI(IndividualMobilityModel referencePointMobilityModel, double minDeviation, double maxDeviation) {
        referencePointMobilityModel.getModelSettingsComponent();
    }
}
