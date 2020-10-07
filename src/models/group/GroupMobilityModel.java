package models.group;

import gui.MobilityPluginPanel;
import gui.MoteGroupPanel;
import models.MobilityModel;
import org.contikios.cooja.Simulation;

import javax.swing.*;

public abstract class GroupMobilityModel extends MobilityModel {
    public GroupMobilityModel(Simulation simulation) {
        super(simulation);
    }

    protected abstract void moveGroup(MoteGroupPanel.MoteGroup group);

    @Override
    public void step() {
        for (MoteGroupPanel.MoteGroup group : MobilityPluginPanel.GroupPanel.getGroups()) {
            moveGroup(group);
        }
    }
}
