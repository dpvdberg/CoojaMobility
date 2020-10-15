package models.group.referencePoint;

import gui.group.MoteGroupPanel;
import gui.models.group.referencePoint.ReferencePointMobilityModelGUI;
import models.individual.IndividualMobilityModel;
import org.contikios.cooja.Mote;
import org.contikios.cooja.Simulation;
import org.contikios.cooja.interfaces.Position;
import utils.MathUtils;
import utils.MobilityMote;
import utils.MoteGroup;
import utils.Vector;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

public class ReferencePointMobilityModel extends ReferencePointIMobilityModel{
    private ReferencePointMobilityModelGUI ui = new ReferencePointMobilityModelGUI(1.0, 5.0);
    private HashMap<MobilityMote, Vector> deviation = new HashMap<>();
    private static final double SECONDS = Math.pow(10, 6);

    public ReferencePointMobilityModel(Simulation simulation) {
        super(simulation);
        createReferencePoints();

        for (MobilityMote mote : getMotes()) {
            double randomX = MathUtils.linearInterpolate(ui.getMinDeviationSpinner(), ui.getMaxDeviationSpinner(), random.nextDouble());
            double randomY = MathUtils.linearInterpolate(ui.getMinDeviationSpinner(), ui.getMaxDeviationSpinner(), random.nextDouble());

            deviation.put(mote, new Vector(randomX, randomY));
        }

        MoteGroupPanel.getInstance().addListener(this);
        ui.getReferencePointMobilityPanel().addListener(this);
    }

    @Override
    public String getMobilityModelName() {
        return "Reference Point";
    }

    @Override
    public Component getModelSettingsComponent() {
        return ui.getMainPanel();
    }

    @Override
    protected IndividualMobilityModel buildReferencePointMobilityModel(Collection<MobilityMote> motes) {
        ui.getReferencePointMobilityModel().setMotes(motes);
        return ui.getReferencePointMobilityModel();
    }

    @Override
    protected void moveMote(MobilityMote mote, MobilityMote point) {
        Position pos = point.getPosition();

        double dX = deviation.get(mote).getX() +
                MathUtils.linearInterpolate(-1, 1, random.nextDouble()) +
                MathUtils.linearInterpolate(ui.getMinDeviationSpinner(), ui.getMaxDeviationSpinner(), getPeriod() / SECONDS);
        double dY = deviation.get(mote).getY() +
                MathUtils.linearInterpolate(-1, 1, random.nextDouble()) +
                MathUtils.linearInterpolate(ui.getMinDeviationSpinner(), ui.getMaxDeviationSpinner(), getPeriod() / SECONDS);

        mote.moveTo(pos.getXCoordinate() + dX, pos.getYCoordinate() + dY);
    }
}
