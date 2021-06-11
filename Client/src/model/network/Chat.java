package model.network;

import java.io.Serializable;

import model.logic.*;

/**
 * This class contains a chat that program sends true network
 * 
 * @author Farhad Aman
 * @version 1.0
 */
public class Chat implements Serializable
{
    /**
     * the user name of the player who sends this chat
     */
    private String sender;

    /**
     * the content of the chat
     */
    private String text;

    /**
     * creates a new chat
     * @param sender
     * @param text
     */
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

    /**
     * this method creates a string for saving chat in files
     */
    public String fileToString()
    {
        return sender + " : " + text;
    }

    /**
     * @return the text of the chat
     */
    public String getText()
    {
        return text;
    }

    /**
     * @return the user name of chat sender
     */
    public String getSender()
    {
        return sender;
    }
}
