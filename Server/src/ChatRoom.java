import java.net.ServerSocket;
import java.util.ArrayList;

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

    public ArrayList<Player> connect(int port, int playersCount)
    {
        try(ServerSocket server = new ServerSocket(port)) 
        {
            
        } 
        catch(Exception e) 
        {

        }
        return null;
    }
}
