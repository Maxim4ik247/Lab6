package data;

import java.io.Serializable;

public class Person implements Serializable {
    private Integer height; //Поле может быть null, Значение поля должно быть больше 0
    private Color eyeColor; //Поле может быть null
    private Color hairColor; //Поле может быть null
    private Country nationality; //Поле может быть null
    private Location location; //Поле не может быть null

    public Person(Integer height, Color eyeColor, Color hairColor, Country nationality, Location location) {
        this.height = height;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.nationality = nationality;
        this.location = location;
    }

    public String toXml() {
        return "<Person>" + "\n\t\t<height>" + height + "</height>" + "\n\t\t<eyeColor>" + eyeColor + "</eyeColor>" + "\n\t\t<hairColor>" + hairColor + "</hairColor>" + "\n\t\t<nationality>" + nationality + "</nationality>" + "\n\t\t" + location.toXml() + "\n\t" + "</Person>";
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Color getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(Color eyeColor) {
        this.eyeColor = eyeColor;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Person{" + "height=" + height + ", eyeColor=" + eyeColor + ", hairColor=" + hairColor + ", nationality=" + nationality + ", location=" + location + '}';
    }
}

