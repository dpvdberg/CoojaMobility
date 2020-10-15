package gui.models.group.referencePoint;

import gui.group.ReferencePointMobilityPanel;
import models.MobilityModel;
import models.individual.IndividualMobilityModel;
import org.contikios.cooja.Simulation;

import javax.swing.*;

public class ReferencePointMobilityModelGUI {
    private JPanel mainPanel;
    private JPanel referencePointMobilityJPanel;
    private JSpinner minDeviationSpinner;
    private JSpinner maxDeviationSpinner;

    private ReferencePointMobilityPanel referencePointMobilityPanel;

    public ReferencePointMobilityModelGUI(double minDeviation, double maxDeviation) {
        referencePointMobilityPanel = new ReferencePointMobilityPanel();

        referencePointMobilityJPanel.add(referencePointMobilityPanel.getMainPanel());
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

    public IndividualMobilityModel getReferencePointMobilityModel() {
        return referencePointMobilityPanel.getActiveModel();
    }
}
