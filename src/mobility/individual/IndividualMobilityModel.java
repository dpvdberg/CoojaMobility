package mobility.individual;

import mobility.MobilityModel;
import org.contikios.cooja.Mote;
import org.contikios.cooja.Simulation;
import utils.MobilityMote;

public abstract class IndividualMobilityModel extends MobilityModel {
    public IndividualMobilityModel(Simulation simulation) {
        super(simulation);
    }

    protected abstract void moveMote(MobilityMote mote);

    @Override
    public void step() {
        for (MobilityMote mote : getMotes()) {
            moveMote(mote);
        }
    }
}
