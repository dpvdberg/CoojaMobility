import mobility.MobilityModel;
import org.contikios.cooja.Simulation;

import javax.swing.*;
import java.awt.*;

public class MobilityPluginPanel {
    private final Simulation simulation;
    private JPanel mainPanel;
    private JComboBox<MobilityModel> modelComboBox;

    public MobilityPluginPanel(Simulation simulation) {
        this.simulation = simulation;

        modelComboBox.setModel(new DefaultComboBoxModel<>());
        for (MobilityModel model : MobilityModelFactory.buildModels(simulation)) {
            modelComboBox.addItem(model);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void createUIComponents() {

    }

}
