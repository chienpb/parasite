package client;

import javafx.beans.property.SimpleStringProperty;

public class Process {
    private SimpleStringProperty name, id, sessionName, session, memUsage;
    public String getName(){
        return name.get();
    }
    public String getId(){
        return id.get();
    }
    public String getSessionName(){
        return sessionName.get();
    }
    public String getMemUsage()
    {
        return memUsage.get();
    }

    public String getSession()
    {
        return session.get();
    }

    Process(String name, String id, String sessionName, String session, String memUsage)
    {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleStringProperty(id);
        this.sessionName = new SimpleStringProperty(sessionName);
        this.session = new SimpleStringProperty(session);
        this.memUsage = new SimpleStringProperty(memUsage);
    }
}
