package models.group.referencePoint;

import gui.MobilityPluginPanel;
import gui.group.MoteGroupPanel;
import models.group.GroupMobilityModel;
import models.individual.IndividualMobilityModel;
import org.contikios.cooja.Simulation;
import utils.BlankMote;
import utils.MobilityMote;
import utils.MoteGroup;

import java.sql.Ref;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public abstract class ReferencePointIMobilityModel extends GroupMobilityModel {
    private HashMap<MoteGroup, ReferencePoint> groupReferencePoints = new HashMap<>();
    Random random;

    protected static class ReferencePoint {
        private final MobilityMote referencePoint;
        private final IndividualMobilityModel mobilityModel;

        public ReferencePoint(MobilityMote mote, IndividualMobilityModel model) {
            this.referencePoint = mote;
            this.mobilityModel = model;
        }

        public MobilityMote getReferencePoint() {
            return referencePoint;
        }

        public IndividualMobilityModel getMobilityModel() {
            return mobilityModel;
        }
    }


    public ReferencePointIMobilityModel(Simulation simulation) {
        super(simulation);

        random = new Random();
        createReferencePoints();
    }

    @Override
    protected void moveGroup(MoteGroup group) {
        ReferencePoint referencePoint = groupReferencePoints.get(group);
        referencePoint.getMobilityModel().step();

        for (MobilityMote mote : group.getMoteList()) {
            moveMote(mote, groupReferencePoints.get(group));
        }
    }

    protected abstract void moveMote(MobilityMote mote, ReferencePoint point);

    protected abstract IndividualMobilityModel buildReferencePointMobilityModel(MobilityMote mote);

    private void createReferencePoints() {
        List<MoteGroup> groups = MoteGroupPanel.getInstance().getGroups();

        for (MoteGroup group : groups) {
            MobilityMote referencePoint = new MobilityMote(new BlankMote());
            IndividualMobilityModel model = buildReferencePointMobilityModel(referencePoint);

            ReferencePoint point = new ReferencePoint(referencePoint, model);

            groupReferencePoints.put(group, point);
        }
    }
}
