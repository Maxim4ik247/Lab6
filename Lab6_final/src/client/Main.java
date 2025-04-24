package client;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        Client client = new Client();

        try {
            client.startClient();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}