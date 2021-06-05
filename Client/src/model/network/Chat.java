package model.network;

import java.io.Serializable;

import model.logic.*;

public class Chat implements Serializable
{
    private Player sender;

    private String text;

    public Chat(Player sender, String text)
    {
        this.sender = sender;
        this.text = text;
    }

    @Override
    public String toString() 
    {
        return ClientHandler.ANSI_PURPLE + sender.getUserName() + " : " + ClientHandler.ANSI_BLUE + text + ClientHandler.ANSI_RESET;   
    }

    public String getText()
    {
        return text;
    }

    public Player getSender()
    {
        return sender;
    }
}
