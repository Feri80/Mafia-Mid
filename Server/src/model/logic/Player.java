package model.logic;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import model.roles.*;

/**
 * This class contains a player information and all details the program needs to play with this player
 * 
 * @author Farhad Aman
 * @version 1.0 
 */
public class Player
{
    /**
     * the unique user name of the player
     */
    private String userName;

    /**
     * is player alive
     */
    private boolean isAlive;

    /**
     * the role of the player in the game
     */
    private Role role;

    /**
     * the socket program needs to connect with the player client
     */
    private Socket channel;

    /**
     * the object output stream program needs to send chats for the player
     */
    private ObjectOutputStream objectOutputStream;

    /**
     * the object input stream program needs to read new chat from the player
     */
    private ObjectInputStream objectInputStream;

    /**
     * creates a new player
     * @param userName
     * @param channel
     * @param objectOutputStream
     * @param objectInputStream
     */
    public Player(String userName, Socket channel, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream)
    {
        this.userName = userName;
        isAlive = true;
        role = null;
        this.channel = channel;
        this.objectOutputStream = objectOutputStream;
        this.objectInputStream = objectInputStream;
    }

    /**
     * @return the user name of the player
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * @return is player alive
     */
    public boolean getIsAlive()
    {
        return isAlive;
    }

    /**
     * sets true if player is alive else false
     * @param isAlive
     */
    public void setIsAlive(boolean isAlive)
    {
        this.isAlive = isAlive;
    }

    /**
     * @return the socket channel for connection with player
     */
    public Socket getChannel() 
    {
        return channel;
    }

    /**
     * sets the socket channel for connection with player
     * @param channel
     */
    public void setChannel(Socket channel)
    {
        this.channel = channel;
    }

    /**
     * @return the object output stream of the player
     */
    public ObjectOutputStream getObjectOutputStream()
    {
        return objectOutputStream;
    }

    /**
     * @return the object input stream of the player
     */
    public ObjectInputStream getObjectInputStream()
    {
        return objectInputStream;
    }

    /**
     * @return the role of the player
     */
    public Role getRole()
    {
        return role;
    }   

    /**
     * sets the role of the player
     * @param role
     */
    public void setRole(Role role)
    {
        this.role = role;
    }

    @Override
    public String toString() 
    {
        return userName;    
    }
}