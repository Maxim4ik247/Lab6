package client;

import data.network.Request;
import data.network.Response;
import server.system.WorkerCreator;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {

    private int connectionTries = 3;

    private SocketChannel socket = SocketChannel.open();

    public Client() throws IOException {
    }

    public void SendRequest(Request r, SocketChannel socket) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {

            objectOutputStream.writeObject(r);
            objectOutputStream.flush(); // Гарантируем запись всех данных

            byte[] data = byteArrayOutputStream.toByteArray();
            ByteBuffer buffer = ByteBuffer.wrap(data);

            int chunkSize = 8192;

            while (buffer.hasRemaining()) {
                int length = Math.min(chunkSize, buffer.remaining());
                ByteBuffer chunkBuffer = ByteBuffer.wrap(data, buffer.position(), length);

                int bytesWritten = socket.write(chunkBuffer);
                buffer.position(buffer.position() + bytesWritten);
            }
        }
    }

//    public void getResponse(SocketChannel socket) throws IOException {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//
//        ByteBuffer buffer = ByteBuffer.allocate(8192); // Размер буфера
//
//        try {
//            // Читаем данные из канала
//            int bytesRead = socket.read(buffer);
//
//            if (bytesRead > 0) {
//                buffer.flip(); // Переключаем буфер в режим чтения
//                baos.write(buffer.array(), 0, buffer.limit()); // Добавляем данные
//                buffer.clear(); // Очищаем буфер
//
//                // Проверяем, можно ли десериализовать данные
//                byte[] data = baos.toByteArray();
//                if (data.length > 0) {
//                    try (ByteArrayInputStream bi = new ByteArrayInputStream(data);
//                         ObjectInputStream oi = new ObjectInputStream(bi)) {
//                        Response response = (Response) oi.readObject();
//                        System.out.println("Получено сообщение от сервера: " + response.toString());
//
//                        // Сбрасываем baos после успешной обработки
//                        baos.reset();
//
//
//                    } catch (StreamCorruptedException e) {
//                        System.err.println("Некорректный формат данных: " + e.getMessage());
//                    } catch (EOFException e) {
//                        System.err.println("Неполные данные, ждём продолжения...");
//                    } catch (ClassNotFoundException e) {
//                        System.err.println("Неизвестный класс объекта: " + e.getMessage());
//                    }
//                }
//            }
//        } catch (SocketException e) {
//            System.err.println("Соединение сброшено: " + e.getMessage());
//            socket.close();
//        }
//    }

    public void getResponse(SocketChannel socket) throws IOException, InterruptedException, ClassNotFoundException {
        ByteBuffer buffer1 = dynamicBuffer(socket);


        ByteArrayInputStream bi = new ByteArrayInputStream(buffer1.array());
        ObjectInputStream oi = new ObjectInputStream(bi);
        Response response = (Response) oi.readObject();
        System.out.println("Получено сообщение от сервера: " + response);
    }

    public void startClient() throws InterruptedException, IOException {

        socket = SocketChannel.open();

        WorkerCreator workerCreator = new WorkerCreator();

        try {

            // Подключение к серверу
            InetSocketAddress address = new InetSocketAddress("localhost", 24555); // создаем адрес сокета (IP-адрес и порт)
            socket.connect(address);
            Request rTest = new Request("");
            try {
                this.SendRequest(rTest, socket);
                this.getResponse(socket);
                System.out.println("Подключено успешно!");
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Ошибка подключения");
                System.exit(1);
            }
            // Отправка сообщения на сервер

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {

                String input = scanner.nextLine();

                Request request = new Request(input);

                if (input.contains("add") || input.contains("update")){

                    request.setWorker(workerCreator.createWorker());
                }

                if (input.contains("execute_script")){
                    ScriptExecutor se = new ScriptExecutor(this);
                    se.readFile(input.split(" ")[1]);
                }

                if (input.contains("exit")){
                    ExitCommand e = new ExitCommand();
                    e.executeCommand();
                }

                this.SendRequest(request, socket);

                this.getResponse(socket);
            }


        } catch (IOException e) {
//            System.err.println("Ошибка соединения с сервером: " + e.getMessage());
            if(connectionTries==0){
                System.out.println("Не удалось подключиться к серверу. Попробуйте позже.");
                System.exit(1);
            }
            System.out.println("Возникла ошибка при подключении к серверу, повторная попытка подключения...");
            Thread.sleep(3000);
            connectionTries-=1;
            this.startClient();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public ByteBuffer dynamicBuffer(SocketChannel server) throws IOException, InterruptedException {
        Thread.sleep(200);
        ArrayList<ByteBuffer> bufferList = new ArrayList<>();
        for (int i = 0; i < 10000000; i++) {
            ByteBuffer buffer = ByteBuffer.allocate(8192);


            int bytesRead = server.read(buffer);
            buffer.flip();
            if (bytesRead > 0) {
                bufferList.add(buffer);
            }

            if (bytesRead < buffer.capacity()) {
                break;
            }
        }
        ByteBuffer bigBuffer = ByteBuffer.allocate(bufferList.size() * 8192);
        for (ByteBuffer byteBuffer : bufferList) {
            bigBuffer.put(byteBuffer.array());
        }

        System.out.println("Данные прочитаны");

        return bigBuffer;
    }

    public SocketChannel getSocket() {
        return socket;
    }
}