package com.example;
import org.glassfish.tyrus.server.Server;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/chat")
public class WebSocketServer {

    private static Set<Session> sessions = new HashSet<>();

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println("New client connected: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session senderSession) {
        System.out.println("Message from " + senderSession.getId() + ": " + message);
        broadcastMessage(message, senderSession);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println("Client disconnected: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Error on session " + session.getId());
        throwable.printStackTrace();
    }

    private void broadcastMessage(String message, Session senderSession) {
        for (Session session : sessions) {
            if (!session.equals(senderSession) && session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server.Builder()
                .host("localhost")
                .port(8080)
                .contextPath("/websocket")
                .build();        
        
        try {
            server.start();
            System.out.println("WebSocket server started...");
            Thread.currentThread().join(); // Keep the main thread alive
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}