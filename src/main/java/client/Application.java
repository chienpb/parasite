package client;

import javafx.beans.property.SimpleStringProperty;

public class Application {
    private SimpleStringProperty name, id, title;

    public String getName() {
        return name.get();
    }

    public String getId() {
        return id.get();
    }


    public String getTitle() {
        return title.get();
    }

    Application(String name, String id, String title)
    {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleStringProperty(id);
        this.title = new SimpleStringProperty(title);
    }
}
