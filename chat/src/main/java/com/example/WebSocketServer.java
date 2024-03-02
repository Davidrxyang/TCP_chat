package com.example;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

// deployment using a java EE container????

@ServerEndpoint("/chat")
public class WebSocketServer {
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

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
}
