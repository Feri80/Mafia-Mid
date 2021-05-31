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

    private ArrayList<Player> citizens;

    private ArrayList<Player> mafias;

    private int playersCount;

    private int alivePlayersCount;

    private int aliveMafiaCount;

    private int port;

    private ChatRoom chatRoom;

    private ChatQueue chatQueue;

    private String state;

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
        chatQueue = new ChatQueue();
        state = "trash";
    }

    public void startGame()
    {
        chatRoom.connect(port, playersCount);
        players = chatRoom.getPlayers();
        for(Player player : players)
        {
            if(player.getRole() instanceof Mafia)
            {
                mafias.add(player);
            }
            else
            {
                citizens.add(player);
            }
        }
        startFirstNight();
        chatRoom.readFromAll(chatQueue);
        loop();
    }

    private void loop()
    {
        checkFinishConditions();
        startDay();
        checkFinishConditions();
        startNight();
    }

    private void startDay()
    {

    }

    private void startVoting()
    {

    }

    private void startNight()
    {

    }

    private void startFirstNight()
    {
        state = "firstNight";
        setRoles();
        sendRoles();
        mafiasIntroduction();
        introduceDoctorToMayor();
    }

    public void checkFinishConditions()
    {

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
    
    public void sendRoles()
    {
        for(Player player : players)
        {
            chatRoom.sendTo(new Chat(new Special(), player.getUserName() + "Your Role Is : " + player.getRole()), player);
        }
    }
    
    public void mafiasIntroduction()
    {
        String text = "Mafias : \n";
        for(Player mafia : mafias)
        {
            if(mafia.getRole() instanceof GodFather)
            {
                text += (mafia.getUserName() + " Is GodFather \n"); 
            }
            else if(mafia.getRole() instanceof DoctorLecter)
            {
                text += (mafia.getUserName() + " Is Doctor Lecter \n");
            }
            else
            {
                text += (mafia.getUserName() + " Is Regular Mafia \n");
            }
        }
        chatRoom.sendTo(new Chat(new Special(), text), mafias);
    }
    
    public void introduceDoctorToMayor()
    {
        for(Player mayor : citizens)
        {
            if(mayor.getRole() instanceof Mayor)
            {
                for(Player doctor : citizens)
                {
                    if(doctor.getRole() instanceof Doctor)
                    {
                        chatRoom.sendTo(new Chat(new Special(), doctor.getUserName() + " Is Doctor"), mayor);
                    }
                }
            }
        }
    }

    private ArrayList<Player> getPlayers()
    {
        return players;
    }

}
