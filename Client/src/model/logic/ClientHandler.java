package model.logic;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import model.network.Chat;
import model.network.ReadingChatHandler;
import model.network.SendingChatHandler;

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
        System.out.println(port);
        try
        {
            channel = new Socket("127.0.0.1", port);
            System.out.println("Socket Created.");
            objectOutputStream = new ObjectOutputStream(channel.getOutputStream());
            objectInputStream = new ObjectInputStream(channel.getInputStream());
            System.out.println("Streams Created.");
        } 
        catch (Exception e) 
        {   
            e.printStackTrace();
        }

        System.out.println("Successfully Connected To The Server.");

        Thread thread = new Thread(new ReadingChatHandler(this));
        thread.start();

        
        while(true)
        {
            if(isMuted == false)
            {
                if(isVoting == true)
                {

                }
                else
                {
                    Scanner input = new Scanner(System.in);
                    String s = input.nextLine();
                    if(isMuted == false && isVoting == false)
                    {
                        Thread thread2 = new Thread(new SendingChatHandler(this, new Chat(new Player(userName, channel, objectOutputStream, objectInputStream), s)));
                        thread2.start();
                    }
                }
            }
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
