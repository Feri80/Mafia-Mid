package model.logic;

import java.util.ArrayList;

import model.network.Chat;

public class ChatQueue 
{
    private ArrayList<Chat> queue;

    public ChatQueue()
    {
        queue = new ArrayList<>();
    }

    public synchronized void pushBackChat(Chat chat)
    {
        queue.add(chat);
    }   

    public synchronized Chat popFrontChat()
    {
        Chat chat = queue.get(0);
        queue.remove(0);
        return chat;
    }

    public synchronized boolean isEmpty()
    {
        return queue.isEmpty();
    }
}
