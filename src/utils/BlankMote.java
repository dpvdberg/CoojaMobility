package utils;

import org.contikios.cooja.*;
import org.contikios.cooja.contikimote.ContikiMoteType;
import org.contikios.cooja.interfaces.Position;
import org.contikios.cooja.mote.memory.MemoryInterface;
import org.jdom.Element;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public class BlankMote extends MobilityMote {
    private Position position = new Position(null);
    UUID uniqueID = UUID.randomUUID();

    public BlankMote() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BlankMote blankMote = (BlankMote) o;
        return Objects.equals(uniqueID, blankMote.uniqueID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), position);
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

    @Override
    public String toString() {
        return "BlankMote";
    }
}
