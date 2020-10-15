package models.individual.random;

import gui.models.individual.random.RandomWalkMobilityModelGUI;
import org.contikios.cooja.Simulation;
import utils.MathUtils;
import utils.MobilityMote;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class RandomWalkMobilityModel extends RandomIMobilityModel {
    private final RandomWalkMobilityModelGUI ui = new RandomWalkMobilityModelGUI(1.0, 10.0, 1.0, true);
    private final HashMap<MobilityMote, RandomWalkInfo> moteInfo = new HashMap<>();
    private static final double SECONDS = Math.pow(10, 6);

    private static class RandomWalkInfo {
        private double moteSpeed;
        private double moteDirec;
        private int moteUpdates = 0;
    }

    public RandomWalkMobilityModel(Simulation simulation) {
        super(simulation);
        initialize();
    }

    public RandomWalkMobilityModel() {
        super();
    }

    @Override
    public void initialize() {
        for (MobilityMote mote : getMotes()) {
            RandomWalkInfo info =  new RandomWalkInfo();
            chooseNewDirection(info);
            moteInfo.put(mote,info);
        }
    }

    @Override
    public void setMotes(Collection<MobilityMote> motes) {
        super.setMotes(motes);
        initialize();
    }

    @Override
    public String getMobilityModelName() {
        return "Random walk";
    }

    @Override
    public Component getModelSettingsComponent() {
        return ui.getMainPanel();
    }

    @Override
    protected void moveMote(MobilityMote mote) {
        RandomWalkInfo info = moteInfo.get(mote);

        if (ui.getTimeBased()) {
            //If amount of time passed (in microseconds) is the same as update interval in microseconds
            if (info.moteUpdates * getPeriod() >= ui.getUpdateInterval() * SECONDS) {
                //update speed and direction of mote
                chooseNewDirection(info);

                info.moteUpdates = 0;
            }
        } else {
            //if travelled distance is >= update interval distance
            if (info.moteUpdates * getPeriod() / SECONDS * info.moteSpeed >= ui.getUpdateInterval()) {
                chooseNewDirection(info);

                info.moteUpdates = 0;
            }
        }

        //Convert period from microseconds to seconds
        double dist = info.moteSpeed * getPeriod() / SECONDS;

        double dx = dist * Math.cos(info.moteDirec);
        double dy = dist * Math.sin(info.moteDirec);
        mote.translate(dx, dy);

        info.moteUpdates++;
    }

    private void chooseNewDirection(RandomWalkInfo info) {
        info.moteSpeed = MathUtils.linearInterpolate(ui.getSpeedMin(), ui.getSpeedMax(), random.nextDouble());
        info.moteDirec = random.nextDouble() * 2 * Math.PI;
    }
}
