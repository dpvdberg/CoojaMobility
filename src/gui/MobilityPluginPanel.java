package gui;

import models.MobilityModel;
import models.MobilityModelFactory;
import org.contikios.cooja.Cooja;
import org.contikios.cooja.Simulation;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import java.awt.event.ItemEvent;
import java.beans.PropertyVetoException;

public class MobilityPluginPanel {
    private final Simulation simulation;
    private static Cooja cooja;
    private JPanel mainPanel;
    private JComboBox<MobilityModel> modelComboBox;
    private JButton btnStart;
    private JButton btnStop;
    private JPanel mobilityModelSettings;
    private JSlider updateIntervalSlider;
    private MobilityModel activeModel;
    public static MoteGroupPanel GroupPanel;

    private TitledBorder titledBorder = BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

    private long getPeriod() {
        // period in mobility models is in microseconds
        return updateIntervalSlider.getValue() * Simulation.MILLISECOND;
    }

    public MobilityPluginPanel(Simulation simulation) {
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

        // Create group panel for further use
        cooja = simulation.getCooja();
        cooja.registerPlugin(MoteGroupPanel.class);
        cooja.tryStartPlugin(MoteGroupPanel.class, cooja, simulation, null);

        GroupPanel = (MoteGroupPanel) cooja.getPlugin(MoteGroupPanel.class.getName());
        GroupPanel.hide();
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

    public static void showGroupPanel() {
        GroupPanel.show();
        try {
            GroupPanel.setSelected(true);
        } catch (PropertyVetoException ignored) {
        }
    }
}
