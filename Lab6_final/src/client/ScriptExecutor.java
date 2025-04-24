package client;


import data.*;
import data.network.Request;
import data.network.Response;
import server.system.CollectionManager;
import server.system.CommandManager;

import java.io.*;
import java.util.Stack;

public class ScriptExecutor {

    private final Client client;
    private final Stack<String> historyOfFiles = new Stack<>();

    public ScriptExecutor(Client client) {
        this.client = client;
    }


    public String readFile(String filePath) {
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


                        Worker worker = new Worker(name, coordinates, salary, position, status, person);


                        Request r = new Request(command);
                        r.setWorker(worker);
                        client.SendRequest(r, client.getSocket());

                        client.getResponse(client.getSocket());

                } else {
                    Request r = new Request(line);
                    client.SendRequest(r, client.getSocket());

                    client.getResponse(client.getSocket());
                }

            }
            historyOfFiles.pop();

        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        } catch (InterruptedException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

}
