package client;

import data.network.Request;
import data.network.Response;
import server.system.WorkerCreator;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {

    private int connectionTries = 3;

    private SocketChannel socket = SocketChannel.open();

    public Client() throws IOException {
    }

    public void startClient() throws InterruptedException, IOException {

        socket = SocketChannel.open();

        WorkerCreator workerCreator = new WorkerCreator();

        try {

            // Подключение к серверу
            InetSocketAddress address = new InetSocketAddress("localhost", 24555); // создаем адрес сокета (IP-адрес и порт)
            socket.connect(address);
            System.out.println("Подключено успешно!");
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
        for (int i = 0; i < 10; i++) {
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

    public void SendRequest(Request r,SocketChannel socket) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(r);

        objectOutputStream.close();
        ByteBuffer buffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());



        socket.write(buffer);

    }

    public void getResponse(SocketChannel socket) throws IOException, InterruptedException, ClassNotFoundException {
        ByteBuffer buffer1 = dynamicBuffer(socket);


        ByteArrayInputStream bi = new ByteArrayInputStream(buffer1.array());
        ObjectInputStream oi = new ObjectInputStream(bi);
        Response response = (Response) oi.readObject();
        System.out.println("Получено сообщение от сервера: " + response);
    }

    public SocketChannel getSocket() {
        return socket;
    }
}