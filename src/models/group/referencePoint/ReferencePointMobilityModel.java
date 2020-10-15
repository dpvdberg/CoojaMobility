package models.group.referencePoint;

import gui.models.group.referencePoint.ReferencePointMobilityModelGUI;
import models.individual.IndividualMobilityModel;
import org.contikios.cooja.Mote;
import org.contikios.cooja.Simulation;
import org.contikios.cooja.interfaces.Position;
import utils.MobilityMote;
import utils.MoteGroup;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

public class ReferencePointMobilityModel extends ReferencePointIMobilityModel{
    private ReferencePointMobilityModelGUI ui = new ReferencePointMobilityModelGUI(1.0, 5.0);
    private HashMap<MobilityMote, DeviationVector> deviation = new HashMap<>();
    private static final double SECONDS = Math.pow(10, 6);


    private class DeviationVector {
        public double x;
        public double y;

        public DeviationVector(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public ReferencePointMobilityModel(Simulation simulation) {
        super(simulation);

        for (MobilityMote mote : getMotes()) {
            double randomX = random.nextDouble() * (ui.getMaxDeviationSpinner() - ui.getMinDeviationSpinner()) + ui.getMinDeviationSpinner();
            double randomY = random.nextDouble() * (ui.getMaxDeviationSpinner() - ui.getMinDeviationSpinner()) + ui.getMinDeviationSpinner();

            deviation.put(mote, new DeviationVector(randomX, randomY));
        }
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
        Position pos = point.getMote().getInterfaces().getPosition();

        double dX = deviation.get(mote).x + random.nextDouble() * (ui.getMaxDeviationSpinner() - ui.getMinDeviationSpinner()) + ui.getMinDeviationSpinner() * getPeriod() / SECONDS;
        double dY = deviation.get(mote).y + random.nextDouble() * (ui.getMaxDeviationSpinner() - ui.getMinDeviationSpinner()) + ui.getMinDeviationSpinner() * getPeriod() / SECONDS;

        deviation.put(mote, new DeviationVector(dX, dY));

        mote.moveTo(pos.getXCoordinate() + dX, pos.getYCoordinate() + dY);
    }
}
