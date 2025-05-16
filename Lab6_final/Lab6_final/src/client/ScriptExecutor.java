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
            while ((line = takeArgs(bufferedReader)) != null) {

                String command = line;
                if (command.contains("add") || command.contains("update")) {

                    String name = takeArgs(bufferedReader);
                    Float x = Float.parseFloat(takeArgs(bufferedReader));
                    Float y = Float.parseFloat(takeArgs(bufferedReader));
                    Position position = Position.valueOf(takeArgs(bufferedReader));
                    Status status = Status.valueOf(takeArgs(bufferedReader));

                    Float salary = Float.parseFloat(takeArgs(bufferedReader));

                    Color eyeColor = Color.valueOf(takeArgs(bufferedReader));
                    Color hairColor = Color.valueOf(takeArgs(bufferedReader));
                    Country nationality = Country.valueOf(takeArgs(bufferedReader));
                    Integer height = Integer.parseInt(takeArgs(bufferedReader));

                    Float xL = Float.parseFloat(takeArgs(bufferedReader));
                    Float yL = Float.parseFloat(takeArgs(bufferedReader));
                    Long zL = Long.parseLong(takeArgs(bufferedReader));
                    String locationName = takeArgs(bufferedReader);



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
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "";
    }


    public String takeArgs(BufferedReader bufferedReader) throws IOException {
        StringBuilder currentLine = new StringBuilder();
        int charRead;
        while ((charRead = bufferedReader.read()) != -1) {
            char c = (char) charRead;
            if (c == '\n' || c == '\r') {
                String result = currentLine.toString();
                System.out.println(result);
                return result;
            }
            currentLine.append(c);
        }

        return currentLine.length() > 0 ? currentLine.toString() : null;
    }
}
