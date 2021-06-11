package model.network;


import model.logic.*;

/**
 * This class reads new chats from server
 * 
 * @author Farhad Aman
 * @version 1.0
 */
public class ReadingChatHandler implements Runnable
{
    /**
     * the client handler of the game
     */
    private ClientHandler clientHandler;

    /**
     * creates a new reading chat handler
     * @param clientHandler
     */
    public ReadingChatHandler(ClientHandler clientHandler)
    {   
        this.clientHandler = clientHandler;
    }

    @Override
    public void run() 
    {
        while(true)
        {
            try 
            {
                Chat chat = (Chat)clientHandler.getObjectInputStream().readObject();
                if(chat.getSender().equals("SPECIAL"))
                {   
                    System.out.println("Special Chat Got.");
                    if(chat.getText().equals("MUTE"))
                    {
                        System.out.println("Your Muted.");
                        clientHandler.setIsMuted(true);
                        try 
                        {
                            Thread.sleep(200);    
                        } 
                        catch (Exception e) 
                        {
                            e.printStackTrace();
                        }
                    }
                    else if(chat.getText().equals("UNMUTE"))
                    {
                        System.out.println("Your Unmuted.");
                        clientHandler.setIsMuted(false);
                        try 
                        {
                            Thread.sleep(200);    
                        } 
                        catch (Exception e) 
                        {
                            e.printStackTrace();
                        }
                    }
                    else if(chat.getText().equals("VOTE"))
                    {
                        clientHandler.setIsVoting(true);
                        try 
                        {
                            Thread.sleep(200);    
                        } 
                        catch (Exception e) 
                        {
                            e.printStackTrace();
                        }
                    }
                    else if(chat.getText().equals("FREE"))
                    {
                        clientHandler.setIsVoting(false);
                        try 
                        {
                            Thread.sleep(200);    
                        } 
                        catch (Exception e) 
                        {
                            e.printStackTrace();
                        }
                    }
                    else if(chat.getText().equals("END"))
                    {
                        System.exit(0);
                    }
                    else if(chat.getText().equals("KILL"))
                    {
                        clientHandler.setIsKilled(true);
                        System.out.println(ClientHandler.ANSI_RED + "YOU ARE DEAD !!!\n" + ClientHandler.ANSI_YELLOW + "You Can Still Watch Game." + ClientHandler.ANSI_RESET);
                    }
                    else if(chat.getText().length() > 0 && chat.getText().toCharArray()[0] == '#')
                    {
                        clientHandler.setUserName(chat.getText().substring(1));
                    }
                    else
                    {
                        System.out.println(chat);
                    }
                }
                else
                {
                    System.out.println("Non Special Chat Got.");
                    if(!(chat.getSender().equals(clientHandler.getUserName())))
                    {
                        System.out.println(chat);
                    }
                }
            } 
            catch(Exception e)
            {
                System.out.println("Reading Error.");
                e.printStackTrace();
                break;
            }
            
        }
    }
}
