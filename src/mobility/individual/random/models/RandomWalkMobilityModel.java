package mobility.individual.random.models;

import mobility.individual.random.RandomIMobilityModel;
import org.contikios.cooja.Simulation;
import utils.MobilityMote;

public class RandomWalkMobilityModel extends RandomIMobilityModel {
    private double maxOffset;

    public RandomWalkMobilityModel(Simulation simulation) {
        super(simulation);
    }

    @Override
    public String getMobilityModelName() {
        return "Random walk";
    }

    @Override
    protected void moveMote(MobilityMote mote) {
        double dx = random.nextDouble() * maxOffset;
        double dy = random.nextDouble() * maxOffset;
        mote.translate(dx, dy);
    }
}
