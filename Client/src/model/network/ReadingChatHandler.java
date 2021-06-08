package model.network;


import model.network.*;
import model.logic.*;


public class ReadingChatHandler implements Runnable
{
    private ClientHandler clientHandler;

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
                        clientHandler.setIsMuted(true);
                    }
                    else if(chat.getText().equals("UNMUTE"))
                    {
                        clientHandler.setIsMuted(false);
                    }
                    else if(chat.getText().equals("VOTE"))
                    {
                        clientHandler.setIsVoting(true);
                    }
                    else if(chat.getText().equals("FREE"))
                    {
                        clientHandler.setIsVoting(false);
                    }
                    else if(chat.getText().equals("END"))
                    {
                        System.exit(0);
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
                
            }
            
        }
    }
}
