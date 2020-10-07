package models.individual.random;

import gui.MobilityPluginPanel;
import gui.models.individual.random.RandomDirectionMobilityModelGUI;
import gui.models.individual.random.RandomWaypointMobilityModelGUI;
import org.contikios.cooja.Simulation;
import org.contikios.cooja.interfaces.Position;
import utils.MobilityMote;

import java.awt.*;
import java.util.HashMap;

public class RandomDirectionMobilityModel extends RandomIMobilityModel {
    private final RandomDirectionMobilityModelGUI ui = new RandomDirectionMobilityModelGUI(100, 100, 5);
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

    @Override
    protected void moveMote(MobilityMote mote) {
        RandomDirectionInfo info = moteInfo.get(mote);
        //if travelled distance is >= distance between previous and next position
        if (info.moving) {
            Position current = mote.getInterfaces().getPosition();
            if (current.getXCoordinate() >= ui.getSimulationArea().getAreaLength() ||
                    current.getYCoordinate() >= ui.getSimulationArea().getAreaWidth() ||
                        current.getXCoordinate() <= 0 ||
                            current.getYCoordinate() <= 0) {
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
            System.out.println("Waiting for " + (info.moteUpdates + 1) * getPeriod() + " >= " + info.pauseTime * SECONDS);
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

        info.moteSpeed = random.nextDouble() * (ui.getSpeedMax() - ui.getSpeedMin()) + ui.getSpeedMin();

        double angle = random.nextDouble() * Math.PI;

        //Add appropriate value of pi, depending on which boundary the mote is on
        Position current = mote.getInterfaces().getPosition();
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
