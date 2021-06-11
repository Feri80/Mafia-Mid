package model.logic;

import java.util.ArrayList;

import model.network.Chat;

/**
 * This class contains a queue for reading new chats in order.
 * 
 * @author Farhad Aman
 * @version 1.0
 */
public class ChatQueue 
{
    /**
     * the queue for saving chats
     */
    private ArrayList<Chat> queue;

    /**
     * creates a new chat queue
     */
    public ChatQueue()
    {
        queue = new ArrayList<>();
    }

    /**
     * adds an new chat to the queue in synchronized way because different threads may use this method
     * @param chat
     */
    public synchronized void pushBackChat(Chat chat)
    {
        queue.add(chat);
    }   

    /**
     * pops the front chat of queue in synchronized way because different threads may use this method
     * @return front chat of the queue
     */
    public synchronized Chat popFrontChat()
    {
        Chat chat = queue.get(0);
        queue.remove(0);
        return chat;
    }

    /**
     * checks if the queue is empty
     * @return is empty
     */
    public synchronized boolean isEmpty()
    {
        return queue.isEmpty();
    }
}
