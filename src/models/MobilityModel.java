package models;

import org.apache.log4j.Logger;
import org.contikios.cooja.Simulation;
import org.contikios.cooja.TimeEvent;
import utils.MobilityMote;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class MobilityModel {
    private static final Logger logger = Logger.getLogger(MobilityModel.class);
    private final Simulation simulation;
    private long period;
    private final List<MobilityMote> motes;
    private boolean registered = false;

    public MobilityModel(Simulation simulation) {
        this.simulation = simulation;
        motes = Arrays.stream(simulation.getMotes()).map(MobilityMote::new).collect(Collectors.toList());
        logger.info("Created instance of model: "  + getMobilityModelName());
    }

    public abstract void step();

    public abstract String getMobilityModelName();

    public void register() {
        registered = true;
        simulation.invokeSimulationThread(() -> simulation.scheduleEvent(stepEvent, simulation.getSimulationTime() + period));
    }

    public void unregister() {
        registered = false;
        simulation.clearEvents();
    }

    private final TimeEvent stepEvent = new TimeEvent(0) {
        public void execute(long t) {
            if (!registered) {
                return;
            }
            simulation.scheduleEvent(this, simulation.getSimulationTime() + period);

            step();
        }
    };

    public Simulation getSimulation() {
        return simulation;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public long getPeriod() {
        return period;
    }

    public List<MobilityMote> getMotes() {
        return motes;
    }

    public abstract Component getModelSettingsComponent();

    public String toString() {
        return getMobilityModelName();
    }
}
