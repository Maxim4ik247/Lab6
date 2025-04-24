package data;

import java.io.Serializable;

public class Location implements Serializable {
    private Float x; //Поле не может быть null
    private float y;
    private long z;
    private String name; //Строка не может быть пустой, Поле не может быть null

    public Location(Float x, float y, long z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    public String toXml() {
        return "<Location>" + "\n\t\t<x>" + x + "</x>" + "\n\t\t<y>" + y + "</y>" + "\n\t\t<z>" + z + "</z>" + "\n\t\t<name>" + name + "</name>" + "\n\t" + "</Location>";
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public long getZ() {
        return z;
    }

    public void setZ(long z) {
        this.z = z;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Location{" + "x=" + x + ", y=" + y + ", z=" + z + ", name='" + name + '\'' + '}';
    }
}

