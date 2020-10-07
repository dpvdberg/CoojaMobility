package utils;

import org.contikios.cooja.*;
import org.contikios.cooja.contikimote.ContikiMoteType;
import org.contikios.cooja.interfaces.Position;
import org.contikios.cooja.mote.memory.MemoryInterface;
import org.jdom.Element;

import java.util.Collection;

public class BlankMote implements Mote {

    @Override
    public int getID() {
        return -1;
    }

    @Override
    public MoteInterfaceHandler getInterfaces() {
        return new MoteInterfaceHandler(this, new Class[] {Position.class});
    }

    @Override
    public MemoryInterface getMemory() {
        return null;
    }

    @Override
    public MoteType getType() {
        return null;
    }

    @Override
    public Simulation getSimulation() {
        return null;
    }

    @Override
    public Collection<Element> getConfigXML() {
        return null;
    }

    @Override
    public boolean setConfigXML(Simulation simulation, Collection<Element> collection, boolean b) {
        return false;
    }

    @Override
    public void removed() {

    }

    @Override
    public void setProperty(String s, Object o) {

    }

    @Override
    public Object getProperty(String s) {
        return null;
    }
}
