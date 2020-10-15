package gui.group;

import models.MobilityModel;
import models.MobilityModelFactory;
import models.individual.IndividualMobilityModel;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.List;

public class ReferencePointMobilityPanel {
    private IndividualMobilityModel activeModel;
    private JComboBox RPModelComboBox;
    private JPanel mainPanel;
    private JPanel mobilityModelSettings;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public ReferencePointMobilityPanel() {
        this(MobilityModelFactory.buildEmptyIndividualModels());
    }

    public ReferencePointMobilityPanel(List<IndividualMobilityModel> models) {
        RPModelComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                IndividualMobilityModel selectedModel = (IndividualMobilityModel) RPModelComboBox.getSelectedItem();
                assert selectedModel != null;
                activeModel = selectedModel;

                // Set mobility model settings panel
                mobilityModelSettings.removeAll();
                mobilityModelSettings.add(activeModel.getModelSettingsComponent());
            }
        });

        RPModelComboBox.setModel(new DefaultComboBoxModel<>());
        for (MobilityModel model : models) {
            RPModelComboBox.addItem(model);
        }
    }

    public IndividualMobilityModel getActiveModel() {
        return activeModel;
    }
}
