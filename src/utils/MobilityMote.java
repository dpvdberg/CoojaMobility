package utils;

import org.contikios.cooja.Mote;
import org.contikios.cooja.MoteInterfaceHandler;
import org.contikios.cooja.MoteType;
import org.contikios.cooja.Simulation;
import org.contikios.cooja.interfaces.Position;
import org.contikios.cooja.mote.memory.MemoryInterface;
import org.jdom.Element;

import java.util.Collection;
import java.util.Objects;

public class MobilityMote implements Mote {
    private Mote mote;

    public MobilityMote() {

    }

    public MobilityMote(Mote mote) {
        this.mote = mote;
    }

    public void moveTo(double x, double y, double z) {
        Position pos = mote.getInterfaces().getPosition();
        pos.setCoordinates(x, y, z);
    }

    public void moveTo(double x, double y) {
        Position pos = mote.getInterfaces().getPosition();
        moveTo(x, y, pos.getZCoordinate());
    }

    public void translate(double x, double y) {
        Position pos = mote.getInterfaces().getPosition();
        moveTo(pos.getXCoordinate() + x, pos.getYCoordinate() + y);
    }

    public Position getPosition() {
        return mote.getInterfaces().getPosition();
    }

    @Override
    public String toString() {
        return String.format("Mote #%d (%s)", mote.getID(), mote.getType().getIdentifier());
    }

    public Mote getMote() {
        return mote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MobilityMote that = (MobilityMote) o;
        return Objects.equals(mote, that.mote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mote);
    }

    //region Decorator delegates
    @Override
    public int getID() {
        return mote.getID();
    }

    @Override
    public MoteInterfaceHandler getInterfaces() {
        return mote.getInterfaces();
    }

    @Override
    public MemoryInterface getMemory() {
        return mote.getMemory();
    }

    @Override
    public MoteType getType() {
        return mote.getType();
    }

    @Override
    public Simulation getSimulation() {
        return mote.getSimulation();
    }

    @Override
    public Collection<Element> getConfigXML() {
        return mote.getConfigXML();
    }

    @Override
    public boolean setConfigXML(Simulation simulation, Collection<Element> collection, boolean b) {
        return mote.setConfigXML(simulation, collection, b);
    }

    @Override
    public void removed() {
        mote.removed();
    }

    @Override
    public void setProperty(String s, Object o) {
        mote.setProperty(s, o);
    }

    @Override
    public Object getProperty(String s) {
        return mote.getProperty(s);
    }
    //endregion
}
