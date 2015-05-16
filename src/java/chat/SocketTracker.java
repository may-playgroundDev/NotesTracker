/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.util.HashMap;
import java.util.Map;
import javax.websocket.Session;

/**
 *
 * @author maytan
 */
public class SocketTracker {
    private static final Map<String, Session> socketConnections = new HashMap<> ();
    
    public static void socketCreated(Session session) {
        socketConnections.put(session.getId(), session);
    }
    
    public static void socketDeleted(Session session) {
        socketConnections.remove(session.getId());
    }
    
    public static Session getSessionById(String id) {
        Session session = socketConnections.get(id);
        return session;
    }
    
    public static Map<String, Session> getAllSocketConnections() {
        return socketConnections;
    }
}
