package model.logic;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.InputMismatchException;
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
        this.userName = "";
        this.objectOutputStream = null;
        this.objectInputStream = null;
        this.isMuted = false;
        this.isVoting = false;
    }

    public void startGame()
    {
        try
        {
            this.channel = new Socket("localhost", port);
            System.out.println("Socket Created.");
            this.objectOutputStream = new ObjectOutputStream(channel.getOutputStream());
            this.objectInputStream = new ObjectInputStream(channel.getInputStream());
            System.out.println("Streams Created.");
        } 
        catch (Exception e) 
        {   
            System.out.println("Error In Making Socket Or Streams.");
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
                    Scanner input = new Scanner(System.in);
                    try 
                    {
                        int k = input.nextInt();
                        if(isMuted == false && isVoting == true)
                        {
                            try 
                            {
                                objectOutputStream.writeObject(new Chat(userName, String.valueOf(k)));
                            } 
                            catch (Exception e) 
                            {
                                System.out.println("Sending Chat Error.");
                                e.printStackTrace();
                            }
                        }
                    } 
                    catch (InputMismatchException e) 
                    {
                        System.out.println("Voting State !!! Please Insert A Number.");
                    }
                }
                else
                {
                    Scanner input = new Scanner(System.in);
                    String s = input.nextLine();
                    if(isMuted == false && isVoting == false)
                    {
                        try 
                        {
                            objectOutputStream.writeObject(new Chat(userName, s));
                        } 
                        catch (Exception e) 
                        {
                            System.out.println("Sending Chat Error.");
                            e.printStackTrace();
                        }
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

    public void setUserName(String userName)
    {
        this.userName = userName;
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
