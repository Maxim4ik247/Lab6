package server.util;

import java.io.FileWriter;
import java.io.IOException;

public class WriteToFile {
    public static void writeToFile(String data) {
        try (FileWriter fileWriter = new FileWriter("/Users/maksneokesarijskij/Documents/Lab5_final/src/util/data.xml")) {
            System.out.println(data);
            fileWriter.write(data);
            System.out.println("Текст записан в файл data.xml");
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл: " + e.getMessage());
        }
    }
}
