package gui.group;

import models.MobilityModel;
import models.MobilityModelFactory;
import models.individual.IndividualMobilityModel;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ReferencePointMobilityPanel {
    private IndividualMobilityModel activeModel;
    private JComboBox RPModelComboBox;
    private JPanel mainPanel;
    private JPanel mobilityModelSettings;
    private List<ReferencePointMobilityModelUpdateListener> listeners = new ArrayList<>();

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

                listeners.forEach(ReferencePointMobilityModelUpdateListener::referencePointMobilityModelUpdated);
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

    public void addListener(ReferencePointMobilityModelUpdateListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ReferencePointMobilityModelUpdateListener listener) {
        listeners.remove(listener);
    }
}
