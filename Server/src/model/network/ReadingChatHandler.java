package model.network;

import java.io.IOException;

import model.logic.Player;

public class ReadingChatHandler implements Runnable
{
    private Player player;

    public ReadingChatHandler(Player player)
    {
        this.player = player;
    }

    @Override
    public void run() 
    {
        while(true)
        {
            try 
            {
                Chat chat = (Chat)player.getObjectInputStream().readObject();
            } 
            catch(ClassNotFoundException | IOException e) 
            {
                e.printStackTrace();
            }
        }
    }
}
