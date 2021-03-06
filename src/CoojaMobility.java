import gui.MobilityPluginPanel;
import gui.group.MoteGroupPanel;
import org.apache.log4j.Logger;
import org.contikios.cooja.*;

import javax.swing.*;
import java.awt.*;

@ClassDescription("CoojaMobility")
@PluginType(PluginType.SIM_PLUGIN)
public class CoojaMobility extends VisPlugin {
    private static final long serialVersionUID = 113684732283878019L;
    private static final Logger logger = Logger.getLogger(CoojaMobility.class);

    private static final String PLUGIN_NAME = "Mobility Plugin";
    private final MobilityPluginPanel panel;
    private static CoojaMobility instance;

    public CoojaMobility(Simulation simulation, final Cooja cooja) {
        super(PLUGIN_NAME, cooja, false);

        Box mainBox = Box.createVerticalBox();

        // Build the group panel
        MoteGroupPanel.build(simulation);

        // Add the panel to the box
        MobilityPluginPanel.buildInstance(simulation);
        panel = MobilityPluginPanel.getInstance();
        mainBox.add(panel.getMainPanel());

        this.getContentPane().add(BorderLayout.CENTER, mainBox);

        instance = this;

        this.setSize(new Dimension(600, 400));
    }
}
