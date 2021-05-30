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

            out.writeObject(new Chat(new Special(), "OK your connected to the server please enter your name to start the game."));

            while(chatRoom.addPlayer(new Player(((Chat)in.readObject()).getText(), channel, out, in)) != true)
            {
                out.writeObject(new Chat(new Special(), "This username is already taken please enter another one."));
            }
            out.writeObject(new Chat(new Special(), "OK your username accepted."));
            out.writeObject(new Chat(new Special(), "MUTE"));
            out.flush();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
}
