package models.group.referencePoint;

import gui.MobilityPluginPanel;
import gui.group.MoteGroupPanel;
import gui.group.MoteGroupUpdateListener;
import gui.group.ReferencePointMobilityModelUpdateListener;
import models.group.GroupMobilityModel;
import models.individual.IndividualMobilityModel;
import org.contikios.cooja.Simulation;
import utils.BlankMote;
import utils.MobilityMote;
import utils.MoteGroup;

import java.sql.Ref;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public abstract class ReferencePointIMobilityModel extends GroupMobilityModel implements MoteGroupUpdateListener, ReferencePointMobilityModelUpdateListener {
    private HashMap<MoteGroup, MobilityMote> groupReferencePoints = new HashMap<>();
    private IndividualMobilityModel referencePointMobilityModel;
    Random random;

    public ReferencePointIMobilityModel(Simulation simulation) {
        super(simulation);

        random = new Random();
        createReferencePoints();
    }

    @Override
    protected void moveGroup(MoteGroup group) {
        referencePointMobilityModel.step();

        for (MobilityMote mote : group.getMoteList()) {
            moveMote(mote, groupReferencePoints.get(group));
        }
    }

    protected abstract void moveMote(MobilityMote mote, MobilityMote point);

    protected abstract IndividualMobilityModel buildReferencePointMobilityModel(Collection<MobilityMote> mote);

    @Override
    public void moteGroupsUpdated() {
        createReferencePoints();
    }

    @Override
    public void referencePointMobilityModelUpdated() {
        buildReferencePointMobilityModel(groupReferencePoints.values());
    }

    private void createReferencePoints() {
        List<MoteGroup> groups = MoteGroupPanel.getInstance().getGroups();

        for (MoteGroup group : groups) {
            MobilityMote referencePoint = new MobilityMote(new BlankMote());
            referencePoint.moveTo(random.nextDouble() * 100, random.nextDouble()*100);

            groupReferencePoints.put(group, referencePoint);
        }

        referencePointMobilityModel = buildReferencePointMobilityModel(groupReferencePoints.values());
    }
}
