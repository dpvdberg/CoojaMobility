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
            double randomX = (random.nextBoolean() ? -1 : 1) *
                    MathUtils.linearInterpolate(ui.getMinDeviationSpinner(), ui.getMaxDeviationSpinner(), random.nextDouble());
            double randomY = (random.nextBoolean() ? -1 : 1) *
                    MathUtils.linearInterpolate(ui.getMinDeviationSpinner(), ui.getMaxDeviationSpinner(), random.nextDouble());

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

        double deltaX = (random.nextBoolean() ? -1 : 1)
                * MathUtils.linearInterpolate(ui.getMinDeviationSpinner(), ui.getMaxDeviationSpinner(), random.nextDouble())
                * getPeriod() / SECONDS;
        double deltaY = (random.nextBoolean() ? -1 : 1)
                * MathUtils.linearInterpolate(ui.getMinDeviationSpinner(), ui.getMaxDeviationSpinner(), random.nextDouble())
                * getPeriod() / SECONDS;

        Vector movedVector = new Vector(deviation.get(mote).getX() + deltaX, deviation.get(mote).getY() + deltaY)
                .scale(ui.getMinDeviationSpinner(), ui.getMaxDeviationSpinner());

        deviation.put(mote, movedVector);

        mote.moveTo(pos.getXCoordinate() + movedVector.getX(), pos.getYCoordinate() + movedVector.getY());
    }
}
