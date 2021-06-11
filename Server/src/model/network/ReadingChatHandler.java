package model.network;

import java.io.IOException;

import model.logic.God;
import model.logic.Player;
import model.roles.Mafia;

/**
 * This class reads new chat from a player and adds it to the chat queue
 * 
 * @author Farhad Aman
 * @version 1.0
 */
public class ReadingChatHandler implements Runnable
{
    /**
     * the god for getting storages
     */
    private God god;

    /**
     * the player that we want to read from
     */
    private Player player;

    /**
     * creates a new reading chat handler 
     * @param god
     * @param player
     */
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
                if(!(god.getForceMute() != null && god.getForceMute().getUserName().equals(player.getUserName())))
                {
                    god.getChatQueue().pushBackChat(chat);
                }
            } 
            catch(ClassNotFoundException | IOException e) 
            {
                System.out.println("reading chat from " + player.getUserName() + " error.");
                if(player.getUserName().equals(god.getHeadOfMafia().getUserName()))
                {
                    god.changeHeadOfMafia();
                }
                god.getChatRoom().sendTo(new Chat("SPECIAL", "KILL"), player);
                god.getChatRoom().sendToAll(new Chat("SPECIAL", player + " Connection Lost."));
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
