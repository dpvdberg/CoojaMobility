package models;

import models.group.referencePoint.ReferencePointMobilityModel;
import models.individual.IndividualMobilityModel;
import models.individual.random.RandomDirectionMobilityModel;
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
                add(new RandomDirectionMobilityModel(simulation));
                add(new ReferencePointMobilityModel(simulation));
            }
        };
    }

    public static List<IndividualMobilityModel> buildEmptyIndividualModels() {
        return new ArrayList<IndividualMobilityModel>() {
            {
                add(new RandomWalkMobilityModel());
                add(new RandomWaypointMobilityModel());
                add(new RandomDirectionMobilityModel());
            }
        };
    }
}
