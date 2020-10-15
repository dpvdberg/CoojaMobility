package models.individual;

import models.MobilityModel;
import org.contikios.cooja.Simulation;
import utils.MobilityMote;

public abstract class IndividualMobilityModel extends MobilityModel {
    public IndividualMobilityModel(Simulation simulation) {
        super(simulation);
    }

    public IndividualMobilityModel() {
        super();
    }

    protected abstract void moveMote(MobilityMote mote);

    @Override
    public void step() {
        for (MobilityMote mote : getMotes()) {
            moveMote(mote);
        }
    }
}
