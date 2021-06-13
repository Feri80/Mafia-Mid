package model.logic;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

import model.network.Chat;
import model.network.ReadingChatHandler;

/**
 * This class contains the logic of a client in the game
 * 
 * @author Farhad Aman
 * @version 1.0
 */
public class ClientHandler 
{
    /**
     * some colors to use in console
     */
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m"; 

    /**
     * the port of the server
     */
    private int port;

    /**
     * the unique user name of the player
     */
    private String userName;

    /**
     * the socket channel for connecting to the server
     */
    private Socket channel;

    /**
     * the object output stream to send chat to server
     */
    private ObjectOutputStream objectOutputStream;

    /**
     * the object input stream for reading chats from server
     */
    private ObjectInputStream objectInputStream;

    /**
     * is player muted
     */
    private boolean isMuted;

    /**
     * is game in voting state
     */
    private boolean isVoting;

    /**
     * is player killed
     */
    private boolean isKilled;

    /**
     * creates a new client handler
     * @param port
     */
    public ClientHandler(int port)
    {
        this.port = port;
        this.userName = "";
        this.objectOutputStream = null;
        this.objectInputStream = null;
        this.isMuted = false;
        this.isVoting = false;
        this.isKilled = false;
    }

    /**
     * this method starts the game
     */
    public void startGame()
    {
        try
        {
            this.channel = new Socket("localhost", port);
            System.out.println(ANSI_CYAN + "Socket Created." + ANSI_RESET);
            this.objectOutputStream = new ObjectOutputStream(channel.getOutputStream());
            this.objectInputStream = new ObjectInputStream(channel.getInputStream());
            System.out.println(ANSI_CYAN + "Streams Created." + ANSI_RESET);
        } 
        catch (Exception e) 
        {   
            System.out.println(ANSI_RED + "Error In Making Socket Or Streams." + ANSI_RESET);
            e.printStackTrace();
            return;
        }

        System.out.println(ANSI_CYAN + "Successfully Connected To The Server." + ANSI_RESET);

        Thread thread = new Thread(new ReadingChatHandler(this));
        thread.start();

        while(true)
        {
            while(isKilled == true)
            {
                try 
                {
                    Thread.sleep(1000);    
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
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
                                System.out.println(ANSI_RED + "Sending Chat Error." + ANSI_RESET);
                            }
                        }
                    } 
                    catch (InputMismatchException e) 
                    {
                        System.out.println(ANSI_YELLOW + "Please Insert A Valid Input Another Time." + ANSI_RESET);
                    }
                }
                else
                {
                    Scanner input = new Scanner(System.in);
                    try 
                    {
                        String s = input.nextLine();
                        if(s.equals("EXIT"))
                        {
                            System.exit(0);
                        }
                        if(isMuted == false && isVoting == false)
                        {
                            try 
                            {
                                objectOutputStream.writeObject(new Chat(userName, s));
                            } 
                            catch (Exception e) 
                            {
                                System.out.println(ANSI_RED + "Sending Chat Error." + ANSI_RESET);
                            }
                        }    
                    } 
                    catch (Exception e) 
                    {
                        e.printStackTrace();    
                    }
                }
            }
            try 
            {
                Thread.sleep(500);   
            } 
            catch (Exception e) 
            {
                e.printStackTrace();    
            }
        }
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
     * @return the user name of the player
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * sets the user name of the player
     * @param userName
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    /**
     * @return is player muted
     */
    public boolean getIsMuted()
    {
        return isMuted;
    }
    
    /**
     * @return is game in state of voting
     */
    public boolean getIsVoting()
    {
        return isVoting;
    }

    /**
     * @return is player killed
     */
    public boolean getIsKilled()
    {
        return isKilled;
    }

    /**
     * sets is muted of the player
     * @param isMuted
     */
    public void setIsMuted(boolean isMuted)
    {
        this.isMuted = isMuted;
    }

    /**
     * sets is voting of the game
     * @param inVoting
     */
    public void setIsVoting(boolean isVoting)
    {
        this.isVoting = isVoting;
    }

    /**
     * sets is killed of the player
     * @param isKilled
     */
    public void setIsKilled(boolean isKilled)
    {
        this.isKilled = isKilled;
    }
}
