package server;

import data.network.Request;
import data.network.Response;
import server.system.CollectionManager;
import server.system.CommandManager;
import server.system.WorkerCreator;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class Server {


    private final WorkerCreator workerCreator = new WorkerCreator();
    private final CollectionManager collectionManager = new CollectionManager();
    private final CommandManager commandManager = new CommandManager(collectionManager, workerCreator);

    public void startServer() throws IOException, ClassNotFoundException {


        // Создаем серверный канал
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        // Привязываем канал к порту
        serverChannel.bind(new InetSocketAddress(InetAddress.getByName("localhost"), 8080));

        // Создаем селектор
        Selector selector = Selector.open();
        // Регистрируем серверный канал для приема подключений
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("TCP-сервер запущен на порту 8080");

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
        ByteBuffer buffer = ByteBuffer.allocate(5000);
        try {
            int bytesRead = clientChannel.read(buffer);
            if (bytesRead > 0) {
                buffer.flip();

                ByteArrayInputStream bi = new ByteArrayInputStream(buffer.array());
                ObjectInputStream oi = new ObjectInputStream(bi);
                Request request = (Request) oi.readObject();

                System.out.println("Получено сообщение от клиента: " + clientChannel.getRemoteAddress().toString());

                Response response = new Response(commandManager.doCommand(request));

                sendAnswer(clientChannel, response);

                clientChannel.register(selector, SelectionKey.OP_READ);
            } else if (bytesRead == -1) {
                // Соединение закрыто клиентом
                clientChannel.close();
                System.out.println("Соединение закрыто клиентом.");
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
}
