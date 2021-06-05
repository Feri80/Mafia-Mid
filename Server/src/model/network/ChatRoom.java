package model.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.logic.*;

public class ChatRoom 
{

    private ArrayList<Player> players;

    private ArrayList<Player> alivePlayers;

    private ArrayList<Chat> chats;

    public ChatRoom()
    {
        players = new ArrayList<>();
        alivePlayers = new ArrayList<>();
        chats = new ArrayList<>();
    }

    public void readFrom(Player sender, ChatQueue chatQueue)
    {
        Thread thread = new Thread(new ReadingChatHandler(sender, chatQueue));
        thread.start();
    }

    public void readFromAll(ChatQueue chatQueue)
    {
        for(Player player : players)
        {
            readFrom(player, chatQueue);
        }
    }

    public void sendTo(Chat chat, Player dest) 
    {
        Thread thread = new Thread(new SendingChatHandler(dest, chat));
        thread.start();
    }

    public void sendTo(Chat chat, ArrayList<Player> dest)
    {
        for(Player p : dest)
        {
            sendTo(chat, p);
        }
    }

    public void sendToAll(Chat chat)
    {
        sendTo(chat, players);
    }

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
        } 
        catch(Exception e) 
        {
            e.printStackTrace();
        }
        while(players.size() < playersCount)
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
