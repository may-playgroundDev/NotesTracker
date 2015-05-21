/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package custom;

import javax.websocket.Session;

/**
 *
 * @author maytan
 */
public class MyCustomSocketSession{
    Session session;
    String name;
    
    public MyCustomSocketSession(String name, Session session) {
        this.name = name;
        this.session = session;
    }
    
    public String getName() {
        return name;
    }
    
    public Session getSocketSession() {
        return session;
    }
}
