package model.network;

import java.io.IOException;

import model.logic.ChatQueue;
import model.logic.Player;

public class ReadingChatHandler implements Runnable
{
    private Player player;

    private ChatQueue chatQueue;

    public ReadingChatHandler(Player player, ChatQueue chatQueue)
    {
        this.player = player;
        this.chatQueue = chatQueue;
    }

    @Override
    public void run() 
    {
        while(true)
        {
            try 
            {
                Chat chat = (Chat)player.getObjectInputStream().readObject();
                chatQueue.pushBackChat(chat);
            } 
            catch(ClassNotFoundException | IOException e) 
            {
                e.printStackTrace();
            }
        }
    }
}
