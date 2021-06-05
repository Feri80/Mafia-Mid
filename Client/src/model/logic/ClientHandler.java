package model.logic;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import model.network.ReadingChatHandler;

public class ClientHandler 
{
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m"; 

    private int port;

    private String userName;

    private Socket channel;

    private ObjectOutputStream objectOutputStream;

    private ObjectInputStream objectInputStream;

    private boolean isMuted;

    private boolean isVoting;

    public ClientHandler(int port)
    {
        this.port = port;
        this.isMuted = false;
        this.isVoting = false;
    }

    public void startGame()
    {
        try 
        {
            channel = new Socket("localhost",port);
            objectOutputStream = new ObjectOutputStream(channel.getOutputStream());
            objectInputStream = new ObjectInputStream(channel.getInputStream());
        } 
        catch (Exception e) 
        {   
            e.printStackTrace();
        }

        Thread thread = new Thread(new ReadingChatHandler(this));
        thread.start();

        while(true)
        {
            
        }
    }

    public ObjectOutputStream getObjectOutputStream()
    {
        return objectOutputStream;
    }

    public ObjectInputStream getObjectInputStream()
    {
        return objectInputStream;
    }

    public String getUserName()
    {
        return userName;
    }

    public boolean getIsMuted()
    {
        return isMuted;
    }
    
    public boolean getIsVoting()
    {
        return isVoting;
    }

    public void setIsMuted(boolean isMuted)
    {
        this.isMuted = isMuted;
    }

    public void setIsVoting(boolean isVoting)
    {
        this.isVoting = isVoting;
    }
}
