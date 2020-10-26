package com.example.chat_app.Model;

public class Chat {
    String message,sender,receiver;
    public  Chat(String message,String receiver,String sender)
    {
        this.message=message;
        this.sender=sender;
        this.receiver=receiver;
    }
    public Chat()
    {    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
