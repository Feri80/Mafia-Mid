package model.network;

import model.logic.Player;

public class SendingChatHandler implements Runnable
{
    private Player player;

    private Chat chat;

    public SendingChatHandler(Player player, Chat chat)
    {
        this.player = player;
        this.chat = chat;
    }

    @Override
    public void run() 
    {
        try 
        {
            player.getObjectOutputStream().writeObject(chat);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
}
