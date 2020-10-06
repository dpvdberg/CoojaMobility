package models;

import models.individual.random.RandomWalkMobilityModel;
import models.individual.random.RandomWaypointMobilityModel;
import org.contikios.cooja.Simulation;

import java.util.ArrayList;
import java.util.List;

public class MobilityModelFactory {
    public static List<MobilityModel> buildModels(Simulation simulation) {
        return new ArrayList<MobilityModel>() {
            {
                add(new RandomWalkMobilityModel(simulation));
                add(new RandomWaypointMobilityModel(simulation));
            }
        };
    }
}
