package mobility.individual.random;

import mobility.individual.IndividualMobilityModel;
import org.contikios.cooja.Simulation;

import java.util.Random;

public abstract class RandomIMobilityModel extends IndividualMobilityModel {
    protected Random random;

    public RandomIMobilityModel(Simulation simulation) {
        super(simulation);

        random = new Random();
    }
}
