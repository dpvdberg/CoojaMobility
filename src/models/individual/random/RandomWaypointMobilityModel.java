package models.individual.random;

import gui.models.individual.random.RandomWaypointMobilityModelGUI;
import org.contikios.cooja.Simulation;
import org.contikios.cooja.interfaces.Position;
import utils.MathUtils;
import utils.MobilityMote;
import utils.Vector;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class RandomWaypointMobilityModel extends RandomIMobilityModel {
    private final RandomWaypointMobilityModelGUI ui = new RandomWaypointMobilityModelGUI(100, 100, 1.0, 10, 5);
    private final HashMap<MobilityMote, RandomWaypointInfo> moteInfo = new HashMap<>();
    private static final double SECONDS = Math.pow(10, 6);

    private static class RandomWaypointInfo {
        private double moteSpeed;
        private double moteDist;
        private double dx;
        private double dy;
        private int moteUpdates = 0;
        private boolean moving = true;
        private double pauseTime;
    }

    public RandomWaypointMobilityModel(Simulation simulation) {
        super(simulation);
        initialize();
    }

    public RandomWaypointMobilityModel() {
        super();
    }

    @Override
    public void setMotes(Collection<MobilityMote> motes) {
        super.setMotes(motes);
        initialize();
    }

    @Override
    public void initialize() {
        for (MobilityMote mote : getMotes()) {
            RandomWaypointInfo info = new RandomWaypointInfo();
            moteInfo.put(mote, info);
            chooseNewDirection(mote);
        }
    }

    @Override
    public String getMobilityModelName() {
        return "Random Waypoint";
    }

    @Override
    public Component getModelSettingsComponent() {
        return ui.getMainPanel();
    }

    @Override
    protected void moveMote(MobilityMote mote) {
        RandomWaypointInfo info = moteInfo.get(mote);
        //if travelled distance is >= distance between previous and next position
        if (info.moving) {
            if (info.moteUpdates * getPeriod() / SECONDS * info.moteSpeed >= info.moteDist) {
                //wait until pause time is over
                info.moving = false;
                info.moteUpdates = 0;
            }

            //distance to be travelled
            double dist = info.moteSpeed * getPeriod() / SECONDS;

            //calculate new dx, dy to be travelled in this period
            double dx = dist / info.moteDist * info.dx;
            double dy = dist / info.moteDist * info.dy;

            mote.translate(dx, dy);
            info.moteUpdates++;
        } else {
            if ((info.moteUpdates + 1) * getPeriod() >= info.pauseTime * SECONDS) {
                chooseNewDirection(mote);
                info.moteUpdates = 0;
                info.moving = true;
            } else {
                info.moteUpdates++;
            }
        }
    }

    private void chooseNewDirection(MobilityMote mote) {
        RandomWaypointInfo info = moteInfo.get(mote);

        //Save a new random speed
        info.moteSpeed = MathUtils.linearInterpolate(ui.getSpeedMin(), ui.getSpeedMax(), random.nextDouble());

        //Generate random position in simulation area
        Vector pos = new Vector(random.nextDouble() * ui.getArea().getAreaLength(), random.nextDouble() * ui.getArea().getAreaWidth());
        Position current = mote.getPosition();

        //Calculate total dx, dy and distance to be travelled
        info.dx = pos.getX() - current.getXCoordinate();
        info.dy = pos.getY() - current.getYCoordinate();
        info.moteDist = Math.sqrt(Math.pow(info.dx, 2) + Math.pow(info.dy, 2));

        //Generate a random pause time
        info.pauseTime = random.nextDouble() * ui.getMaxPauseTime();
    }
}
