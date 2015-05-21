/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import custom.MyCustomSocketSession;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author maytan
 */
public class SocketTracker {
    private static final Map<String, MyCustomSocketSession> socketConnections = new HashMap<> ();
    
    public static void socketCreated(MyCustomSocketSession session) {
        socketConnections.put(session.getSocketSession().getId(), session);
    }
    
    public static void socketDeleted(MyCustomSocketSession session) {
        socketConnections.remove(session.getSocketSession().getId());
    }
    
    public static MyCustomSocketSession getSessionById(String id) {
        MyCustomSocketSession session = socketConnections.get(id);
        return session;
    }
    
    public static Map<String, MyCustomSocketSession> getAllSocketConnections() {
        return socketConnections;
    }
}
