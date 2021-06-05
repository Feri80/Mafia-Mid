package model.logic;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import model.roles.*;

public class Player implements Serializable
{
    private String userName;

    private transient boolean isAlive;

    private transient Role role;

    private transient Socket channel;

    private transient ObjectOutputStream objectOutputStream;

    private transient ObjectInputStream objectInputStream;


    public Player(String userName, Socket channel, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream)
    {
        this.userName = userName;
        isAlive = true;
        role = null;
        this.channel = channel;
        this.objectOutputStream = objectOutputStream;
        this.objectInputStream = objectInputStream;
    }

    public String getUserName()
    {
        return userName;
    }

    public boolean getIsAlive()
    {
        return isAlive;
    }

    public void setIsAlive(boolean isAlive)
    {
        this.isAlive = isAlive;
    }

    public Socket getChannel() 
    {
        return channel;
    }

    public void setChannel(Socket channel)
    {
        this.channel = channel;
    }

    public ObjectOutputStream getObjectOutputStream()
    {
        return objectOutputStream;
    }

    public ObjectInputStream getObjectInputStream()
    {
        return objectInputStream;
    }

    public Role getRole()
    {
        return role;
    }   

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