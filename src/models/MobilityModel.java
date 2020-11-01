package models;

import org.apache.log4j.Logger;
import org.contikios.cooja.Cooja;
import org.contikios.cooja.Simulation;
import org.contikios.cooja.TimeEvent;
import utils.MobilityMote;
import utils.Vector;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public abstract class MobilityModel {
    private static final Logger logger = Logger.getLogger(MobilityModel.class);
    private final Simulation simulation;
    private long period;
    private Collection<MobilityMote> motes;
    private boolean registered = false;
    private boolean scheduled = false;
    private boolean storePositionHistory = false;
    private HashMap<MobilityMote, List<Vector>> positionHistory;

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
            return;
        }
        if (registered) {
            JOptionPane.showMessageDialog(Cooja.getTopParentContainer(), "Mobility model already running.");
            return;
        }
        registered = true;
        simulation.invokeSimulationThread(() -> {
            if (!scheduled) {
                simulation.scheduleEvent(stepEvent, simulation.getSimulationTime() + period);
            }
        });
    }

    public void unregister() {
        registered = false;
    }

    private final TimeEvent stepEvent = new TimeEvent(0) {
        public void execute(long t) {
            if (!registered) {
                scheduled = false;
                return;
            }
            simulation.scheduleEvent(this, simulation.getSimulationTime() + period);
            scheduled = true;

            if (storePositionHistory) {
                motes.forEach(m -> {
                    if (!positionHistory.containsKey(m)) {
                        positionHistory.put(m, new ArrayList<>());
                    }
                    positionHistory.get(m).add(new Vector(m.getPosition()));
                });
            }
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

    public Map<MobilityMote, List<Vector>> setStorePositionHistory(boolean storePositionHistory) {
        this.positionHistory = new HashMap<>();
        this.storePositionHistory = storePositionHistory;

        return this.positionHistory;
    }
}
