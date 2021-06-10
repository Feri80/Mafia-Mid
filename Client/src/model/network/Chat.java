package model.network;

import java.io.Serializable;

import model.logic.*;

public class Chat implements Serializable
{
    private String sender;

    private String text;

    public Chat(String sender, String text)
    {
        this.sender = sender;
        this.text = text;
    }

    @Override
    public String toString() 
    {
        return "\u001B[35m" + sender + " : " + "\u001B[34m" + text + "\u001B[0m";   
    }

    public String fileToString()
    {
        return sender + " : " + text;
    }

    public String getText()
    {
        return text;
    }

    public String getSender()
    {
        return sender;
    }
}
