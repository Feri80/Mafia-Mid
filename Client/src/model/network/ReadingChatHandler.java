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
                System.out.println("chat got.");
                if((Player)chat.getSender() instanceof Special)
                {   
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
                    else
                    {
                        System.out.println(chat);
                    }
                }
                else
                {
                    if(!(((Player)chat.getSender()).getUserName().equals(clientHandler.getUserName())))
                    {
                        System.out.println(chat);
                    }
                }
            } 
            catch(Exception e)
            {
                e.printStackTrace();
                try 
                {
                    Thread.sleep(5000);
                } 
                catch (InterruptedException e1) 
                {
                    e1.printStackTrace();
                }
            }
            
        }
    }
}
