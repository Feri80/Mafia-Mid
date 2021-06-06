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
        return God.ANSI_PURPLE + sender + " : " + God.ANSI_BLUE + text + God.ANSI_RESET;   
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
