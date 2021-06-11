package model.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import model.logic.*;

/**
 * This class do most of the works with network like sending chat or reading chats from players
 * 
 * @author Farhad Aman
 * @version 1.0
 */
public class ChatRoom 
{
    /**
     * the list of the players of the game
     */
    private ArrayList<Player> players;

    /**
     * creates a new chat room
     */
    public ChatRoom()
    {
        players = new ArrayList<>();
    }

    /**
     * reads chats from the player and adds them to the chat queue
     * @param god
     * @param player
     */
    public void readFrom(God god, Player player)
    {
        Thread thread = new Thread(new ReadingChatHandler(god, player));
        thread.start();
    }

    /**
     * run read from method for all players
     * @param god
     */
    public void readFromAll(God god)
    {
        for(Player player : players)
        {
            readFrom(god, player);
        }
    }

    /**
     * sends a new chat to the player
     * @param chat
     * @param dest
     */
    public void sendTo(Chat chat, Player dest) 
    {
        try 
        {
            dest.getObjectOutputStream().writeObject(chat);
        } 
        catch (Exception e) 
        {
            System.out.println("sending chat to " + dest + " error.");
        }
    }

    /**
     * sends a new chat for a list of players
     * @param chat
     * @param dest
     */
    public void sendTo(Chat chat, ArrayList<Player> dest)
    {
        for(Player p : dest)
        {
            sendTo(chat, p);
        }
    }

    /**
     * sends a new chat to all players
     * @param chat
     */
    public void sendToAll(Chat chat)
    {
        sendTo(chat, players);
    }

    /**
     * sends a new chat for all alive players
     * @param chat
     */
    public void sendToAllAlive(Chat chat)
    {
        for(Player p : players)
        {
            if(p.getIsAlive() == true)
            {
                sendTo(chat, p);
            }
        }
    }

    /**
     * this method runs in the begining of the program and connects all players with the server
     * @param port
     * @param playersCount
     */
    public void connect(int port, int playersCount)
    {
        int clientCount = 0;
        try(ServerSocket server = new ServerSocket(port)) 
        {
            while(clientCount < playersCount)
            {
                System.out.println("Server Is Waiting For A New Client.");
                Socket channel = server.accept();
                System.out.println("A Socket Created.");
                Thread thread = new Thread(new ConnectionHandler(channel, this));
                thread.start();
                System.out.println("A Client Accepted.");
                clientCount++;
            }
            try 
            {
                Thread.sleep(3000);
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
        } 
        catch(Exception e) 
        {
            e.printStackTrace();
        }
        while(this.players.size() < playersCount)
        {
            try 
            {
                Thread.sleep(500);
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
        }
        System.out.println("All Clients Are Connected.");
    }

    /**
     * @return players
     */
    public ArrayList<Player> getPlayers()
    {
        return players;
    }

    /**
     * add a new player to the list of players in synchronized way because different thread may use it
     * @param player
     * @return true if work is successfully done else false
     */
    public synchronized boolean addPlayer(Player player)
    {
        for(Player p : players)
        {
            if(player.getUserName().trim().equals(p.getUserName().trim()))
            {
                return false;
            }
        }
        players.add(player);
        return true;
    }
}
