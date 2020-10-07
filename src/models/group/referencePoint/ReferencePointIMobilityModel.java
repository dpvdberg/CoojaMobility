package models.group.referencePoint;

import gui.MobilityPluginPanel;
import gui.group.MoteGroupPanel;
import models.group.GroupMobilityModel;
import models.individual.IndividualMobilityModel;
import org.contikios.cooja.Simulation;
import utils.BlankMote;
import utils.MobilityMote;
import utils.MoteGroup;

import java.util.HashMap;
import java.util.List;

public abstract class ReferencePointIMobilityModel extends GroupMobilityModel {
    private HashMap<MoteGroup, MobilityMote> groupReferencePoints = new HashMap<>();
    private HashMap<MobilityMote, IndividualMobilityModel> rpMobilityModel = new HashMap<>();


    public ReferencePointIMobilityModel(Simulation simulation) {
        super(simulation);

        createReferencePoints();
    }

    @Override
    protected void moveGroup(MoteGroup group) {
        MobilityMote referencePoint = groupReferencePoints.get(group);
        rpMobilityModel.get(referencePoint).step();

        for (MobilityMote mote : group.getMoteList()) {
            moveMote(mote);
        }
    }

    protected abstract void moveMote(MobilityMote mote);

    protected abstract IndividualMobilityModel buildReferencePointMobilityModel(MobilityMote mote);

    private void createReferencePoints() {
        List<MoteGroup> groups = MoteGroupPanel.getInstance().getGroups();

        for (MoteGroup group : groups) {
            MobilityMote referencePoint = new MobilityMote(new BlankMote());
            IndividualMobilityModel model = buildReferencePointMobilityModel(referencePoint);

            groupReferencePoints.put(group, referencePoint);
            rpMobilityModel.put(referencePoint, model);
        }
    }
}
