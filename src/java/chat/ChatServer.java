/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import custom.MyCustomSocketSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
/**
 *
 * @author maytan
 */
@ServerEndpoint("/chatServer/{name}")
public class ChatServer {
    //currently local ip address to access this app on the same network
    //http://192.168.3.251:8080/WebSocketSample/
    
    /**
     * @param name
     * @param session
     * @OnOpen allows us to intercept the creation of a new session.
     * The session class allows us to send data to the user.
     * In the method onOpen, we'll let the user know that the handshake was 
     * successful.
     */
    @OnOpen
    public void onOpen(@PathParam("name") String name, Session session) {
        System.out.println(name + " has opened a connection at " + session.getId()); 
        try {
            session.getBasicRemote().sendText("Connection Established");
            MyCustomSocketSession mySession = new MyCustomSocketSession(name, session);
            SocketTracker.socketCreated(mySession);
            broadcastMessageToAll("joined the chatroom", mySession);
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @OnMessage
    public void onMessage(String message, Session session) {
         System.out.println("Message from " + session.getId() + ": " + message);
         MyCustomSocketSession mySession = SocketTracker.getSessionById(session.getId());
         broadcastMessageToAll(message, mySession);
         //session.getBasicRemote().sendText("I got this message: " + message);
    }
    
    @OnClose
    public void onClose(Session session) {
         System.out.println("Session " +session.getId()+" has ended");
         MyCustomSocketSession mySession = SocketTracker.getSessionById(session.getId());
         SocketTracker.socketDeleted(mySession);
         broadcastMessageToAll("left the chatroom", mySession);
    }
    
    private void broadcastMessageToAll(String message, MyCustomSocketSession fromSession) {
        //boardcast message to everyone 
            for (MyCustomSocketSession availableSession: SocketTracker.getAllSocketConnections().values()) {
                if(availableSession.getSocketSession().isOpen()) {
                    try {
                        availableSession.getSocketSession().getBasicRemote().sendText(fromSession.getName() + ": " + message);
                    } catch (IOException ex) {
                        Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    //removed dead session
                    SocketTracker.socketDeleted(availableSession);
                }
            }
    }
}
