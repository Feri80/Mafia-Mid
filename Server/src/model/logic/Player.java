package model.logic;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import model.roles.*;

public class Player
{
    private String userName;

    private boolean isAlive;

    private Role role;

    private Socket channel;

    private ObjectOutputStream objectOutputStream;

    private ObjectInputStream objectInputStream;


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
}