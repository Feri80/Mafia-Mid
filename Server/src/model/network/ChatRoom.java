package model.network;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.logic.*;

public class ChatRoom 
{

    private ArrayList<Player> players;

    private ArrayList<Chat> chats;

    public ChatRoom()
    {
        players = new ArrayList<>();
        chats = new ArrayList<>();
    }


    public void sendTo(Chat chat, Player dest) 
    {
        
    }

    public void sendTo(Chat chat, ArrayList<Player> dest)
    {

    }

    public void sendToAll(Chat chat)
    {
        sendTo(chat, players);
    }

    public void connect(int port, int playersCount)
    {
        ExecutorService pool = Executors.newCachedThreadPool();
        int clientCount = 0;
        try(ServerSocket server = new ServerSocket(port)) 
        {
            while(clientCount < playersCount)
            {
                System.out.println("Server Is Waiting For A New Client.");
                pool.execute(new ConnectionHandler(server.accept(), this));
                System.out.println("A Client Accepted");
                clientCount++;
            }
            pool.shutdown();
        } 
        catch(Exception e) 
        {
            e.printStackTrace();
        }
        while(players.size() < playersCount)
        {
            try 
            {
                Thread.sleep(1000);
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Player> getPlayers()
    {
        return players;
    }

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
