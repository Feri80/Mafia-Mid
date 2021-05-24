import java.util.ArrayList;

public class God 
{
    private ArrayList<Player> players;   

    private ArrayList<Citizen> citizens;

    private ArrayList<Mafia> mafias;

    private int playersCount;

    private int alivePlayersCount;

    private int aliveMafiaCount;

    private ChatRoom chatRoom;

    public God(int playersCount)
    {
        players = new ArrayList<>();
        citizens = new ArrayList<>();
        mafias = new ArrayList<>();
        this.playersCount = playersCount;
        alivePlayersCount = playersCount;
        if(playersCount <= 7)
        {
            aliveMafiaCount = 1;
        }
        else
        {
            aliveMafiaCount = playersCount / 3;
        }
        chatRoom = new ChatRoom();
    }

    public void game()
    {
        players = chatRoom.connect(0, playersCount);
    }

    private void loop()
    {
        
    }

    private ArrayList<Player> getPlayers()
    {
        return players;
    }

}
