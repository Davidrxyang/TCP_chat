package com.example;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

@ClientEndpoint
public class WebSocketClient {

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to server.");
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Message from server: " + message);
    }

    @OnClose
    public void onClose() {
        System.out.println("Connection closed.");
    }

    public void sendMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws URISyntaxException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        String uri = "ws://localhost:8080/chat";

        try {
            Session session = container.connectToServer(WebSocketClient.class, new URI(uri));

            // Read messages from console and send to server
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();
                session.getBasicRemote().sendText(message);
            }
        } catch (DeploymentException | IOException e) {
            e.printStackTrace();
        }
    }
}
