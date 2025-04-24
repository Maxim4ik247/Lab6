package server.util;

import java.io.FileWriter;
import java.io.IOException;

public class WriteToFile {
    public static void writeToFile(String data) {
        try (FileWriter fileWriter = new FileWriter("/data.xml")) {
            System.out.println(data);
            fileWriter.write(data);
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл: " + e.getMessage());
        }
    }
}
