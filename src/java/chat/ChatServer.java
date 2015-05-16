/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
/**
 *
 * @author maytan
 */
@ServerEndpoint("/chatServer")
public class ChatServer {
    //currently local ip address to access this app on the same network
    //http://192.168.3.251:8080/WebSocketSample/
    
    /**
     * @param session
     * @OnOpen allows us to intercept the creation of a new session.
     * The session class allows us to send data to the user.
     * In the method onOpen, we'll let the user know that the handshake was 
     * successful.
     */
    @OnOpen
    public void onOpen(Session session) {
        System.out.println(session.getId() + " has opened a connection"); 
        try {
            session.getBasicRemote().sendText("Connection Established");
            broadcastMessageToAll("joined the chatroom", session);
            SocketTracker.socketCreated(session);
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @OnMessage
    public void onMessage(String message, Session session) {
         System.out.println("Message from " + session.getId() + ": " + message);
        try {
               broadcastMessageToAll(message, session);
            //session.getBasicRemote().sendText("I got this message: " + message);
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @OnClose
    public void onClose(Session session) {
         System.out.println("Session " +session.getId()+" has ended");
         SocketTracker.socketDeleted(session);
        try {
            broadcastMessageToAll("left the chatroom", session);
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void broadcastMessageToAll(String message, Session fromSession) throws IOException {
        //boardcast message to everyone 
            for (Session availableSession: SocketTracker.getAllSocketConnections().values()) {
                if(availableSession.isOpen()) {
                     availableSession.getBasicRemote().sendText(fromSession.getId() + ": " + message);
                } else {
                    //removed dead session
                    SocketTracker.socketDeleted(availableSession);
                }
            }
    }
}
