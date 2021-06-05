package model.network;

import model.logic.ClientHandler;
import model.logic.Special;


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
                if(chat.getSender() instanceof Special)
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
                    if(!(chat.getSender().getUserName().equals(clientHandler.getUserName())))
                    {
                        System.out.println(chat);
                    }
                }
            } 
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
        }
    }
}
