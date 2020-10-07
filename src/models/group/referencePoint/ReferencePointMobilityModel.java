package models.group.referencePoint;

import models.individual.IndividualMobilityModel;
import org.contikios.cooja.Simulation;
import utils.MobilityMote;

import java.awt.*;

public class ReferencePointMobilityModel extends ReferencePointIMobilityModel{
    public ReferencePointMobilityModel(Simulation simulation) {
        super(simulation);
    }

    @Override
    public String getMobilityModelName() {
        return "Reference Point";
    }

    @Override
    public Component getModelSettingsComponent() {
        return null;
    }

    @Override
    protected IndividualMobilityModel buildReferencePointMobilityModel(MobilityMote mote) {
        return null;
    }

    @Override
    protected void moveMote(MobilityMote mote) {

    }
}
