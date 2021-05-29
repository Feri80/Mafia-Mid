import java.util.ArrayList;

public class God 
{
    private ArrayList<Player> players;   

    private ArrayList<Citizen> citizens;

    private ArrayList<Mafia> mafias;

    private int playersCount;

    private int alivePlayersCount;

    private int aliveMafiaCount;

    private int port;

    private ChatRoom chatRoom;

    public God(int playersCount, int port)
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
        chatRoom.connect(port, playersCount);
        players = chatRoom.getPlayers();

        
    }

    private void loop()
    {
        
    }

    private void startFirstNight()
    {
        divideRoles();
    }

    private void divideRoles()
    {

    }

    private ArrayList<Player> getPlayers()
    {
        return players;
    }

}
