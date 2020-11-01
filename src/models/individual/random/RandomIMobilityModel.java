package models.individual.random;

import models.individual.IndividualMobilityModel;
import org.contikios.cooja.Simulation;

import java.util.Random;

public abstract class RandomIMobilityModel extends IndividualMobilityModel {
    protected Random random = new Random();

    public RandomIMobilityModel(Simulation simulation) {
        super(simulation);
    }

    public RandomIMobilityModel() {
        super();
    }
}
