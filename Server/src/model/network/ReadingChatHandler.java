package model.network;

import java.io.IOException;

import model.logic.ChatQueue;
import model.logic.God;
import model.logic.Player;
import model.roles.Mafia;

public class ReadingChatHandler implements Runnable
{
    private God god;

    private Player player;

    private ChatQueue chatQueue;

    public ReadingChatHandler(God god, Player player)
    {
        this.god = god;
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
                System.out.println(chat);
                god.getChatQueue().pushBackChat(chat);
            } 
            catch(ClassNotFoundException | IOException e) 
            {
                System.out.println("reading chat from " + player.getUserName() + " error.");
                if(player.getUserName().equals(god.getHeadOfMafia().getUserName()))
                {
                    god.changeHeadOfMafia();
                }
                god.getChatRoom().sendTo(new Chat("SPECIAL", "KILL"), player);
                god.getChatRoom().sendToAll(new Chat("SPECIAL", player + " KILLED."));
                player.setIsAlive(false);
                god.setAlivePlayersCount(god.getAlivePlayersCount() - 1);
                god.getAlivePlayers().remove(player);
                if(player.getRole() instanceof Mafia)
                {
                    god.setAliveMafiaCount(god.getAliveMafiaCount() - 1);
                }
                break;
            }
        }
    }
}
