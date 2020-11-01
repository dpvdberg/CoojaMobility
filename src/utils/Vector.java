package utils;

import org.contikios.cooja.interfaces.Position;

public class Vector {
    private double x;
    private double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(Position position) {
        this(position.getXCoordinate(), position.getYCoordinate());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double size() {
        return Math.sqrt(x*x + y*y);
    }

    public Vector scaleToMaxSize(double max) {
        double size = size();
        if (size < max) {
            return this;
        } else {
            return new Vector(max/size * x, max/size * y);
        }
    }

    public Vector scaleToMinSize(double min) {
        double size = size();
        if (size > min) {
            return this;
        } else {
            return new Vector(min/size * x, min/size * y);
        }
    }

    public Vector scale(double min, double max) {
        return this.scaleToMaxSize(max).scaleToMinSize(min);
    }
}