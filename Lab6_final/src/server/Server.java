package server;

import data.Worker;
import data.network.Request;
import data.network.Response;
import server.system.CollectionManager;
import server.system.CommandManager;
import server.system.WorkerCreator;
import server.util.ReadXml;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class Server {



    private final WorkerCreator workerCreator = new WorkerCreator();
    private final CollectionManager collectionManager;
    private final CommandManager commandManager;

    public Server(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
        this.commandManager = new CommandManager(collectionManager, workerCreator);
    }



    public void startServer(String path) throws IOException, ClassNotFoundException {




        // Создаем серверный канал
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        // Привязываем канал к порту
        serverChannel.bind(new InetSocketAddress(InetAddress.getByName("localhost"), 24555));

        // Создаем селектор
        Selector selector = Selector.open();
        // Регистрируем серверный канал для приема подключений
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        for (Worker worker : ReadXml.read(path)) {
            collectionManager.add(worker);
        }


        System.out.println("TCP-сервер запущен на порту 24555");

        // Основной цикл обработки событий
        while (true) {
            selector.select(); // Количество ключей, чьи каналы готовы к операции. БЛОКИРУЕТ, ПОКА НЕ БУДЕТ КЛЮЧЕЙ
            Set<SelectionKey> selectedKeys = selector.selectedKeys(); // получаем список ключей от каналов, готовых к работе
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator(); // получаем итератор ключей
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                // Обработка событий
                if (key.isAcceptable()) {
                    handleAccept(key, selector);
                } else if (key.isReadable()) {
                    handleRead(key, selector);
                }
                keyIterator.remove();
            }
        }
    }

    // Обработка события ACCEPT (новое подключение)
    private void handleAccept(SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);

        // Регистрируем клиентский канал для чтения
        clientChannel.register(selector, SelectionKey.OP_READ);
        System.out.println("Новое подключение от " + clientChannel.getRemoteAddress());
    }

    // Обработка события READ (получение данных)
    private void handleRead(SelectionKey key, Selector selector) throws IOException, ClassNotFoundException {

        SocketChannel clientChannel = (SocketChannel) key.channel();
        // Читаем данные от клиента
        ByteBuffer buffer = dynamicBuffer(clientChannel);
        try {

            if (buffer.position() > 0) {

                ByteArrayInputStream bi = new ByteArrayInputStream(buffer.array());
                ObjectInputStream oi = new ObjectInputStream(bi);
                Request request = (Request) oi.readObject();

                System.out.println("Получено сообщение от клиента: " + clientChannel.getRemoteAddress().toString());

                Response response;
                try {
                    response = new Response(commandManager.doCommand(request));
                } catch (Exception e) {
                    response = new Response("Возникла ошибка" + e.getMessage());
                }


                sendAnswer(clientChannel, response);

                clientChannel.register(selector, SelectionKey.OP_READ);
            }
        } catch (SocketException e) {
            System.err.println("Соединение сброшено: " + e.getMessage());
            clientChannel.close(); // Закрываем канал, если соединение сброшено
        }
    }

    public void sendAnswer(SocketChannel client, Response response) throws IOException {
        client.configureBlocking(false);

        System.out.println(response);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(response);
        objectOutputStream.close();
        ByteBuffer buffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());

        // Отправляем данные
        while (buffer.hasRemaining()) {
            client.write(buffer);
        }

        System.out.println("Request was sent to " + client.socket().toString());

    }

    public ByteBuffer dynamicBuffer(SocketChannel client) throws IOException {
        ArrayList<ByteBuffer> bufferList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ByteBuffer buffer = ByteBuffer.allocate(8192);
            int bytesRead = client.read(buffer);
            if (bytesRead == -1) {
                // Соединение закрыто клиентом
                client.close();
                System.out.println("Соединение закрыто клиентом.");
                return ByteBuffer.allocate(0);
            }
            if (buffer.limit() == buffer.position() || bufferList.isEmpty()) {
                bufferList.add(buffer);
            } else {
                break;
            }
        }
        ByteBuffer bigBuffer = ByteBuffer.allocate(bufferList.size() * 8192);
        for (ByteBuffer byteBuffer : bufferList) {
            bigBuffer.put(byteBuffer.array());
        }

        return bigBuffer;
    }
}
