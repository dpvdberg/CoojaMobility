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

        SpinnerNumberModel minModel = new SpinnerNumberModel(minDeviation, 1.0, 100.0, 1);
        minDeviationSpinner.setModel(minModel);
        maxDeviationSpinner.setModel(new SpinnerNumberModel(maxDeviation, 1.0, 100.0, 1));

        maxDeviationSpinner.addChangeListener(e -> {
            minModel.setValue(Math.min((double) maxDeviationSpinner.getValue(), (double) minModel.getValue()));
            minModel.setMaximum((double) maxDeviationSpinner.getValue());
        });
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

    public ReferencePointMobilityPanel getReferencePointMobilityPanel() {
        return referencePointMobilityPanel;
    }
}
