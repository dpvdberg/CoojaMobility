package gui;

import models.MobilityModel;
import models.MobilityModelFactory;
import org.contikios.cooja.Simulation;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

public class MobilityPluginPanel {
    private final Simulation simulation;
    private JPanel mainPanel;
    private JComboBox<MobilityModel> modelComboBox;
    private JButton btnStart;
    private JButton btnStop;
    private JPanel mobilityModelSettings;
    private JSlider updateIntervalSlider;
    private MobilityModel activeModel;

    private TitledBorder titledBorder = BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

    public long getPeriod() {
        // period in mobility models is in microseconds
        return updateIntervalSlider.getValue() * Simulation.MILLISECOND;
    }

    private static MobilityPluginPanel instance;
    public static MobilityPluginPanel getInstance() {
        return instance;
    }

    public static void buildInstance(Simulation simulation) {
        if (instance == null) {
            instance = new MobilityPluginPanel(simulation);
        }
    }

    private MobilityPluginPanel(Simulation simulation) {
        this.simulation = simulation;

        modelComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                MobilityModel selectedModel = (MobilityModel) modelComboBox.getSelectedItem();
                assert selectedModel != null;
                selectedModel.setPeriod(getPeriod());
                activeModel = selectedModel;

                String title = activeModel.getMobilityModelName() + " settings";
                titledBorder.setTitle(title);
                mobilityModelSettings.setBorder(titledBorder);

                // Set mobility model settings panel
                mobilityModelSettings.removeAll();
                mobilityModelSettings.add(activeModel.getModelSettingsComponent());
            }
        });

        updateIntervalSlider.addChangeListener(e -> activeModel.setPeriod(getPeriod()));

        modelComboBox.setModel(new DefaultComboBoxModel<>());
        for (MobilityModel model : MobilityModelFactory.buildModels(simulation)) {
            modelComboBox.addItem(model);
        }
        btnStart.addActionListener(e -> start());
        btnStop.addActionListener(e -> stop());
    }

    private void start() {
        activeModel.register();
    }

    private void stop() {
        activeModel.unregister();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
