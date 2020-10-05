import gui.MobilityPluginPanel;
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

    public CoojaMobility(Simulation simulation, final Cooja cooja) {
        super(PLUGIN_NAME, cooja, false);

        Box mainBox = Box.createVerticalBox();

        // Add the panel to the box
        MobilityPluginPanel panel = new MobilityPluginPanel(simulation);
        mainBox.add(panel.getMainPanel());

        this.getContentPane().add(BorderLayout.CENTER, mainBox);
    }
}
