package data;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Worker implements Comparable<Worker>, Serializable {
    private static int counter = 1;
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Float salary; //Поле не может быть null, Значение поля должно быть больше 0
    private Position position; //Поле может быть null
    private Status status; //Поле может быть null
    private Person person; //Поле может быть null

    public Worker(String name, Coordinates coordinates, Float salary, Position position, Status status, Person person) {
        this.creationDate = LocalDateTime.now();
        counter++;
        this.id = counter;
        this.name = name;
        this.coordinates = coordinates;
        this.salary = salary;
        this.position = position;
        this.status = status;
        this.person = person;
    }

    public Worker(Integer id, String name, Coordinates coordinates, LocalDateTime creationDate, Float salary, Position position, Status status, Person person) {
        counter++;
        counter = Math.max(counter, id);
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.salary = salary;
        this.position = position;
        this.status = status;
        this.person = person;
    }

    public Integer getId() {
        return id;
    }

    public Worker setId(Integer id) {
        counter++;
        counter = Math.max(counter, id);
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Worker setName(String name) {
        this.name = name;
        return this;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Worker setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Worker setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public Float getSalary() {
        return salary;
    }

    public Worker setSalary(Float salary) {
        this.salary = salary;
        return this;
    }

    public Position getPosition() {
        return position;
    }

    public Worker setPosition(Position position) {
        this.position = position;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public Worker setStatus(Status status) {
        this.status = status;
        return this;
    }

    public Person getPerson() {
        return person;
    }

    public Worker setPerson(Person person) {
        this.person = person;
        return this;
    }


    public String toXML() {
        return "<Worker>" + "\n\t\t<id>" + id + "</id>" + "\n\t\t<name>" + name + "</name>" + "\n\t\t" + coordinates.toXml() + "\n\t\t<creationDate>" + creationDate + "</creationDate>" + "\n\t\t<salary>" + salary + "</salary>" + "\n\t\t<position>" + position + "</position>" + "\n\t\t<status>" + status + "</status>" + "\n\t\t" + person.toXml() + "\n\t" + "</Worker>";
    }


    @Override
    public String toString() {
        return "Worker{" + "id=" + id + ", name='" + name + '\'' + ", creationDate=" + creationDate + ", coordinates=" + coordinates + ", salary=" + salary + ", position=" + position + ", status=" + status + ", person=" + person + '}';
    }

    @Override
    public int compareTo(Worker o) {
        try {
            return (int) (this.salary - o.getSalary());
        } catch (NullPointerException e) {

            if (o == null) {
                return -1;
            } else return 1;

        }
    }
}
