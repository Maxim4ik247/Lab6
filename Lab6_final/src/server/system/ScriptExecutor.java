package server.system;


import data.*;
import data.network.Request;

import java.io.*;
import java.util.Stack;

public class ScriptExecutor {

    private final CommandManager commandManager;
    private final Stack<String> historyOfFiles = new Stack<>();

    public ScriptExecutor(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public String readFile(String filePath, CollectionManager collectionManager) {
        if (historyOfFiles.contains(filePath)) {
            return "Была пропущена рекурсия";
        }

        historyOfFiles.add(filePath);
        try (FileReader fileReader = new FileReader(filePath); BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {

                String command = line;
                if (command.contains("add") || command.contains("update")) {


                    String name = bufferedReader.readLine();
                    Float x = Float.parseFloat(bufferedReader.readLine());
                    Float y = Float.parseFloat(bufferedReader.readLine());

                    Float salary = Float.parseFloat(bufferedReader.readLine());
                    Position position = Position.valueOf(bufferedReader.readLine());
                    Status status = Status.valueOf(bufferedReader.readLine());

                    Integer height = Integer.parseInt(bufferedReader.readLine());
                    Color eyeColor = Color.valueOf(bufferedReader.readLine());
                    Color hairColor = Color.valueOf(bufferedReader.readLine());
                    Country nationality = Country.valueOf(bufferedReader.readLine());

                    Float xL = Float.parseFloat(bufferedReader.readLine());
                    Float yL = Float.parseFloat(bufferedReader.readLine());
                    Long zL = Long.parseLong(bufferedReader.readLine());
                    String locationName = bufferedReader.readLine();


                    Coordinates coordinates = new Coordinates(x,y);
                    Location location = new Location(xL, yL,zL, locationName);
                    Person person = new Person(height, eyeColor, hairColor, nationality, location);



                    if (command.equals("add")) {
                        Worker worker = new Worker(name, coordinates, salary, position, status, person);


                        collectionManager.add(worker);
                        System.out.println("Worker был добавлен в коллекцию");
                    } else if (command.contains("update")) {
                        Integer id = Integer.parseInt(command.split(" ")[1]);
                        for (Worker w : collectionManager.getworkerLinkedList()) {
                            if (w.getId() == id) {
                                w.setName(name).setCoordinates(coordinates).setSalary(salary).setPosition(position).setStatus(status).setPerson(person);
                            }
                        }

                        System.out.println("Worker был обновлен");

                    }

                } else {
                    Request r = new Request(line);
                    System.out.println(commandManager.doCommand(r));
                }

            }
            historyOfFiles.pop();

        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
        return "";
    }

}
