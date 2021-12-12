package ru.otus.model;

import java.util.List;

public class ObjectForMessage {
    private List<String> data;


    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public ObjectForMessage clone()  {
        ObjectForMessage objectForMessage;
        try {
            objectForMessage = (ObjectForMessage) super.clone();
        } catch (CloneNotSupportedException e) {
            objectForMessage = new ObjectForMessage();
        }
        final var newData = data.stream().map(String::new).toList();
        objectForMessage.setData(newData);
        return objectForMessage;
    }
}
