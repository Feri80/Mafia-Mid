import java.util.ArrayList;

public class ChatRoom 
{

    private ArrayList<Player> players;

    public ChatRoom(ArrayList<Player> players)
    {
        this.players = players;
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
}
