package model.network;

import model.logic.ClientHandler;

public class SendingChatHandler implements Runnable
{
    private ClientHandler clientHandler;

    private Chat chat;

    public SendingChatHandler(ClientHandler clientHandler, Chat chat)
    {
        this.clientHandler = clientHandler;
        this.chat = chat;
    }

    @Override
    public void run() 
    {
        try 
        {
            clientHandler.getObjectOutputStream().writeObject(chat);
        } 
        catch (Exception e) 
        {
            System.out.println("Sending Chat Error.");
        }
    }
    
}
