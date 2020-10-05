package gui;

import models.MobilityModel;
import models.MobilityModelFactory;
import org.contikios.cooja.Simulation;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import java.awt.event.ItemEvent;

public class MobilityPluginPanel {
    private final Simulation simulation;
    private JPanel mainPanel;
    private JComboBox<MobilityModel> modelComboBox;
    private JButton btnStart;
    private JButton btnStop;
    private JPanel mobilityModelSettings;
    private MobilityModel activeModel;

    private TitledBorder titledBorder = BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

    public MobilityPluginPanel(Simulation simulation) {
        this.simulation = simulation;

        modelComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                activeModel = (MobilityModel) modelComboBox.getSelectedItem();
                assert activeModel != null;
                String title = activeModel.getMobilityModelName() + " settings";
                titledBorder.setTitle(title);
                mobilityModelSettings.setBorder(titledBorder);

                // Set mobility model settings panel
                mobilityModelSettings.removeAll();
                mobilityModelSettings.add(activeModel.getModelSettingsComponent());
            }
        });

        modelComboBox.setModel(new DefaultComboBoxModel<>());
        for (MobilityModel model : MobilityModelFactory.buildModels(simulation)) {
            modelComboBox.addItem(model);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
