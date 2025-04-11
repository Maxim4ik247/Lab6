package client;

import data.network.Request;
import data.network.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client {
    public void startClient() {

//        WorkerCreator workerCreator = new WorkerCreator();

        try {
            // Подключение к серверу
            InetSocketAddress address = new InetSocketAddress("localhost", 8080); // создаем адрес сокета (IP-адрес и порт)
            SocketChannel socket = SocketChannel.open();
            socket.connect(address);
            // Отправка сообщения на сервер

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {

                String input = scanner.nextLine();

                Request request = new Request(input);

//                if (input.contains("add") || input.contains("update")){
//
//                    request.setWorker(workerCreator.createWorker());
//                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(request);

                objectOutputStream.close();
                ByteBuffer buffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
                socket.write(buffer);

                // Получение ответа от сервера
                buffer = ByteBuffer.allocate(1024);
                socket.read(buffer);

                ByteArrayInputStream bi = new ByteArrayInputStream(buffer.array());
                ObjectInputStream oi = new ObjectInputStream(bi);
                Response response = (Response) oi.readObject();
                System.out.println("Получено сообщение от сервера: " + response);
            }


        } catch (IOException e) {
            System.err.println("Ошибка соединения с сервером: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}