package models.group.referencePoint;

import gui.MobilityPluginPanel;
import gui.MoteGroupPanel;
import models.MobilityModel;
import models.group.GroupMobilityModel;
import models.individual.IndividualMobilityModel;
import org.contikios.cooja.Mote;
import org.contikios.cooja.Simulation;
import utils.BlankMote;
import utils.MobilityMote;

import java.awt.*;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public abstract class ReferencePointIMobilityModel extends GroupMobilityModel {
    private HashMap<MoteGroupPanel.MoteGroup, MobilityMote> groupReferencePoints = new HashMap<>();
    private HashMap<MobilityMote, IndividualMobilityModel> rpMobilityModel = new HashMap<>();


    public ReferencePointIMobilityModel(Simulation simulation) {
        super(simulation);

        createReferencePoints();
    }

    @Override
    protected void moveGroup(MoteGroupPanel.MoteGroup group) {
        MobilityMote referencePoint = groupReferencePoints.get(group);
        rpMobilityModel.get(referencePoint).step();

        for (MobilityMote mote : group.getMoteList()) {
            moveMote(mote);
        }
    }

    protected abstract void moveMote(MobilityMote mote);

    protected abstract IndividualMobilityModel buildReferencePointMobilityModel(MobilityMote mote);

    private void createReferencePoints() {
        List<MoteGroupPanel.MoteGroup> groups = MobilityPluginPanel.GroupPanel.getGroups();

        for (MoteGroupPanel.MoteGroup group : groups) {
            MobilityMote referencePoint = new MobilityMote(new BlankMote());
            IndividualMobilityModel model = buildReferencePointMobilityModel(referencePoint);

            groupReferencePoints.put(group, referencePoint);
            rpMobilityModel.put(referencePoint, model);
        }
    }
}
