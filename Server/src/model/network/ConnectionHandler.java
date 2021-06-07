package model.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import model.logic.*;

public class ConnectionHandler implements Runnable
{

    private Socket channel;

    private ChatRoom chatRoom;

    public ConnectionHandler(Socket channel, ChatRoom chatRoom)
    {
        this.channel = channel;
        this.chatRoom = chatRoom;
    }

    @Override
    public void run() 
    {
        try
        {
            ObjectOutputStream out = new ObjectOutputStream(channel.getOutputStream()); 
            ObjectInputStream in = new ObjectInputStream(channel.getInputStream());

            out.writeObject(new Chat("SPECIAL", "OK Your Connected To The Server Please Enter Your Name To Start The Game.\nNote That Sending Your Name Means Your Ready For The Game."));
	
            while(chatRoom.addPlayer(new Player(((Chat)in.readObject()).getText(), channel, out, in)) != true)
            {
                out.writeObject(new Chat("SPECIAL", "This Username Is Already Taken Please Enter Another One."));
            }
			System.out.println(chatRoom.getPlayers().get(chatRoom.getPlayers().size() - 1).getUserName());
            out.writeObject(new Chat("SPECIAL", "#" + chatRoom.getPlayers().get(chatRoom.getPlayers().size() - 1).getUserName()));
            out.writeObject(new Chat("SPECIAL", "OK your username accepted."));
            out.writeObject(new Chat("SPECIAL", "MUTE"));
        } 
        catch (Exception e) 
        {
            System.out.println("connection error.");
            e.printStackTrace();
        }
    }
    
}
