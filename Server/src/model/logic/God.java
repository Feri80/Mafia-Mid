package model.logic;

import java.util.ArrayList;
import java.util.Collections;

import model.network.*;
import model.roles.*;


public class God 
{
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m"; 

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
        this.setRoles();

        
    }

    public void setRoles()
    {
        ArrayList<Role> roles = new ArrayList<>();

        if(playersCount >= 8)
        {
            roles.add(new GodFather());
            roles.add(new DoctorLecter());
            roles.add(new Mayor());
            roles.add(new Doctor());
            roles.add(new Detective());
            roles.add(new Armored());
            roles.add(new Sniper());
            roles.add(new Psychologist());
            for(int i = 0; i < (aliveMafiaCount - 2); i++)
            {
                roles.add(new Mafia());
            }
            for(int i = 0; i < (alivePlayersCount - aliveMafiaCount - 6); i++)
            {
                roles.add(new Citizen());
            }
        }

        Collections.shuffle(roles);

        for(int i = 0; i < playersCount ; i++)
        {
            players.get(i).setRole(roles.get(i));
        }
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
