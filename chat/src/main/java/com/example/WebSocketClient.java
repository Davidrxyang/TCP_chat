package com.example;
import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.net.URI;
import java.util.Scanner;

@ClientEndpoint
public class WebSocketClient {

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to server");
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Message from server: " + message);
    }

    public void sendMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        javax.websocket.WebSocketContainer container = javax.websocket.ContainerProvider.getWebSocketContainer();
        String uri = "ws://localhost:8080/chat"; // Replace with your WebSocket server URI

        try {
            Session session = container.connectToServer(WebSocketClient.class, URI.create(uri));

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
