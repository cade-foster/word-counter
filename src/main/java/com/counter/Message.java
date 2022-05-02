package com.counter;

import java.util.Objects;

/**
 * Contains message data.
 */
public class Message {

    private String id;

    private String message;

    Message(String id, String message) {
        this.id = id;
        this.message = message;
    }

    /**
     * @return the message id.
     */
    public String getId() {
        return id;
    }

    /**
     * @return the message text.
     */
    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return getId().equals(message1.getId()) && getMessage().equals(message1.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getMessage());
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
