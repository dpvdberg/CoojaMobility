package models.individual.random;

import gui.MobilityPluginPanel;
import gui.models.individual.random.RandomDirectionMobilityModelGUI;
import gui.models.individual.random.RandomWaypointMobilityModelGUI;
import org.contikios.cooja.Simulation;
import org.contikios.cooja.interfaces.Position;
import utils.MathUtils;
import utils.MobilityMote;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class RandomDirectionMobilityModel extends RandomIMobilityModel {
    private final RandomDirectionMobilityModelGUI ui = new RandomDirectionMobilityModelGUI(1, 10, 5);
    private final HashMap<MobilityMote, RandomDirectionInfo> moteInfo = new HashMap<>();
    private static final double SECONDS = Math.pow(10, 6);

    private static class RandomDirectionInfo {
        private double moteSpeed;
        private double moteDir;
        private int moteUpdates = 0;
        private boolean moving = true;
        private double pauseTime;
    }

    public RandomDirectionMobilityModel(Simulation simulation) {
        super(simulation);
        initialize();
    }

    public RandomDirectionMobilityModel() {
        super();
    }

    @Override
    public void initialize() {
        for (MobilityMote mote : getMotes()) {
            RandomDirectionInfo info = new RandomDirectionInfo();
            moteInfo.put(mote, info);
            chooseNewDirection(mote);
            info.moteDir = random.nextDouble() * 2 * Math.PI;
        }
    }

    @Override
    public String getMobilityModelName() {
        return "Random Direction";
    }

    @Override
    public Component getModelSettingsComponent() {
        return ui.getMainPanel();
    }

    private boolean isOutsideArea(MobilityMote mote) {
        Position pos = mote.getPosition();
        return pos.getXCoordinate() >= ui.getSimulationArea().getAreaLength() ||
                pos.getYCoordinate() >= ui.getSimulationArea().getAreaWidth() ||
                pos.getXCoordinate() <= 0 ||
                pos.getYCoordinate() <= 0;
    }

    private void snapToArea(MobilityMote mote) {
        double x = mote.getPosition().getXCoordinate();
        double y = mote.getPosition().getYCoordinate();
        double newX = x <= 0 ? 0 : Math.min(x, ui.getSimulationArea().getAreaLength());
        double newY = y <= 0 ? 0 : Math.min(y, ui.getSimulationArea().getAreaWidth());
        mote.moveTo(newX, newY);
    }

    @Override
    protected void moveMote(MobilityMote mote) {
        RandomDirectionInfo info = moteInfo.get(mote);
        //if travelled distance is >= distance between previous and next position
        if (info.moving) {
            if (isOutsideArea(mote)) {
                snapToArea(mote);
                //wait until pause time is over
                info.moving = false;
                info.moteUpdates = 0;
            } else {
                double dist = info.moteSpeed * getPeriod() / SECONDS;

                double dx = dist * Math.cos(info.moteDir);
                double dy = dist * Math.sin(info.moteDir);
                mote.translate(dx, dy);

                info.moteUpdates++;
            }
        } else {
            if ((info.moteUpdates + 1) * getPeriod() >= info.pauseTime * SECONDS) {
                chooseNewDirection(mote);
                info.moteUpdates = 0;
                info.moving = true;

                double dist = info.moteSpeed * getPeriod() / SECONDS;

                double dx = dist * Math.cos(info.moteDir);
                double dy = dist * Math.sin(info.moteDir);
                mote.translate(dx, dy);

                info.moteUpdates++;
            } else {
                info.moteUpdates++;
            }
        }
    }

    private void chooseNewDirection(MobilityMote mote) {
        RandomDirectionInfo info = moteInfo.get(mote);

        info.moteSpeed = MathUtils.linearInterpolate(ui.getSpeedMin(), ui.getSpeedMax(), random.nextDouble());

        double angle = random.nextDouble() * Math.PI;

        //Add appropriate value of pi, depending on which boundary the mote is on
        Position current = mote.getPosition();
        if (current.getXCoordinate() >= ui.getSimulationArea().getAreaLength()) {
            info.moteDir = angle + 0.5 * Math.PI;
        } else if (current.getYCoordinate() >= ui.getSimulationArea().getAreaWidth()) {
            info.moteDir = angle + Math.PI;
        } else if (current.getXCoordinate() <= 0) {
            info.moteDir = angle + 1.5 * Math.PI;
        } else {
            info.moteDir = angle;
        }

        info.pauseTime = random.nextDouble() * ui.getMaxPauseTime();
    }
}
