package utils;

import org.contikios.cooja.*;
import org.contikios.cooja.contikimote.ContikiMoteType;
import org.contikios.cooja.interfaces.Position;
import org.contikios.cooja.mote.memory.MemoryInterface;
import org.jdom.Element;

import java.util.Collection;

public class BlankMote extends MobilityMote {
    private Position position = new Position(null);

    public BlankMote() {
        super();
    }

    @Override
    public void moveTo(Position position) {
        this.position.setCoordinates(position.getXCoordinate(), position.getYCoordinate(), position.getZCoordinate());
    }

    @Override
    public void moveTo(double x, double y) {
        this.position.setCoordinates(x, y, position.getZCoordinate());
    }

    @Override
    public void translate(double x, double y) {
        this.position.setCoordinates(
                this.position.getXCoordinate() + x,
                this.position.getYCoordinate() + y,
                this.position.getZCoordinate()
        );
    }

    @Override
    public Position getPosition() {
        return this.position;
    }
}
