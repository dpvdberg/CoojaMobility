package models;

import gui.group.MoteGroupPanel;
import org.apache.log4j.Logger;
import org.contikios.cooja.Cooja;
import org.contikios.cooja.Simulation;
import org.contikios.cooja.TimeEvent;
import utils.MobilityMote;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class MobilityModel {
    private static final Logger logger = Logger.getLogger(MobilityModel.class);
    private final Simulation simulation;
    private long period;
    private Collection<MobilityMote> motes;
    private boolean registered = false;

    public MobilityModel(Simulation simulation) {
        this.simulation = simulation;
        motes = Arrays.stream(simulation.getMotes()).map(MobilityMote::new).collect(Collectors.toList());
        logger.info("Created instance of model: " + getMobilityModelName());
    }

    public MobilityModel() {
        logger.info("Created empty instance of model: " + getMobilityModelName());
        simulation = null;
    }

    public void setMotes(Collection<MobilityMote> motes) {
        this.motes = motes;
    }

    public abstract void step();

    public abstract String getMobilityModelName();

    public void register() {
        if (simulation == null) {
            System.out.println("Cannot register a mobility model without simulation");
        }
        if (registered) {
            JOptionPane.showMessageDialog(Cooja.getTopParentContainer(), "Mobility model already running.");
            return;
        }
        registered = true;
        simulation.invokeSimulationThread(() -> simulation.scheduleEvent(stepEvent, simulation.getSimulationTime() + period));
    }

    public void unregister() {
        registered = false;
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

    public Collection<MobilityMote> getMotes() {
        return motes;
    }

    public abstract Component getModelSettingsComponent();

    public String toString() {
        return getMobilityModelName();
    }
}
