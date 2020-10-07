package models.group;

import gui.group.MoteGroupPanel;
import models.MobilityModel;
import org.contikios.cooja.Simulation;
import utils.MoteGroup;

public abstract class GroupMobilityModel extends MobilityModel {
    public GroupMobilityModel(Simulation simulation) {
        super(simulation);
    }

    protected abstract void moveGroup(MoteGroup group);

    @Override
    public void step() {
        for (MoteGroup group : MoteGroupPanel.getInstance().getGroups()) {
            moveGroup(group);
        }
    }
}
