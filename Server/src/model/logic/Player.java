package model.logic;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import model.roles.*;

public class Player
{
    private String userName;

    private boolean isAlive;

    private Socket channel;

    private Role role;

    public Player(String userName, Socket channel)
    {
        this.userName = userName;
        isAlive = true;
        this.channel = channel;
        role = null;
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

    public Role getRole()
    {
        return role;
    }   

    public void setRole(Role role)
    {
        this.role = role;
    }
}