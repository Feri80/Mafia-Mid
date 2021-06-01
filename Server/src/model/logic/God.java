package model.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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

    private HashMap<Player, Integer> votes;

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
        votes = new HashMap<>();
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
        startVoting();
        checkFinishConditions();
        startNight();
    }

    private void startDay()
    {
        state = "day";

        chatRoom.sendToAll(new Chat(new Special(), "Day Starts You Can Wake Up."));

        chatRoom.sendToAll(new Chat(new Special(), "FREE"));
        chatRoom.sendToAll(new Chat(new Special(), "UNMUTE"));

        Boolean isTimed = false;
        Thread timer = new Thread(new Timer(isTimed, 180000));
        timer.start();

        while(isTimed == false)
        {
            if(!chatQueue.isEmpty())
            {
                chatRoom.sendToAll(chatQueue.popFrontChat());
            }
        }

        chatRoom.sendToAll(new Chat(new Special(), "MUTE"));

        try 
        {
            Thread.sleep(1000);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }

        while(!chatQueue.isEmpty())
        {
            chatRoom.sendToAll(chatQueue.popFrontChat());
        }

        chatRoom.sendToAll(new Chat(new Special(), "Voting Starts In 10 Seconds."));

        try 
        {
            Thread.sleep(10000);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }

        state = "trash";
    }

    private void startVoting()
    {
        state = "vote";

        chatRoom.sendToAll(new Chat(new Special(), "Voting Please Choose One Of The Valid Choices In 30 Seconds."));
        chatRoom.sendToAll(new Chat(new Special(), playersToString()));

        chatRoom.sendToAll(new Chat(new Special(), "VOTE"));
        chatRoom.sendToAll(new Chat(new Special(), "UNMUTE"));

        Boolean isTimed = false;
        Thread timer = new Thread(new Timer(isTimed, 30000));
        timer.start();

        while(isTimed == false)
        {
            if(!chatQueue.isEmpty())
            {
                chatRoom.sendToAll(chatQueue.popFrontChat());
            }
        }

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
        chatRoom.sendToAll(new Chat(new Special(), "Day Starts In 10 Seconds."));
        try 
        {
            Thread.sleep(10000);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
    }

    private String playersToString()
    {
        String s = "";
        int i = 1;
        for(Player player : players)
        {
            s += ANSI_PURPLE + i + ANSI_RESET + " : " + ANSI_BLUE + player.getUserName() + ANSI_RESET;
            i++;
        }
        return s;
    }

    private void checkFinishConditions()
    {
        if(aliveMafiaCount == 0)
        {   
            chatRoom.sendToAll(new Chat(new Special(), "Congrats To All Citizens \n  Citizen Won The Game."));
        }
        else if(aliveMafiaCount == (alivePlayersCount - aliveMafiaCount))
        {
            chatRoom.sendToAll(new Chat(new Special(), "Congrats To All Mafias \n  Mafia Won The Game."));
        }

        try 
        {
            Thread.sleep(5000);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }

        System.exit(0);
    }
    
    private void setRoles()
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
    
    private void sendRoles()
    {
        for(Player player : players)
        {
            chatRoom.sendTo(new Chat(new Special(), player.getUserName() + "Your Role Is : " + player.getRole()), player);
        }
    }
    
    private void mafiasIntroduction()
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
    
    private void introduceDoctorToMayor()
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

    public ArrayList<Player> getPlayers()
    {
        return players;
    }

}
