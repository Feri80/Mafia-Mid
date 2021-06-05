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

    private ArrayList<Player> alivePlayers;   

    private ArrayList<Player> citizens;

    private ArrayList<Player> mafias;

    private HashMap<Player, Player> votes;

    private ArrayList<Player> nightKills;

    private Player forceMute;

    private boolean inquiryCheck;

    private int playersCount;

    private int alivePlayersCount;

    private int aliveMafiaCount;

    private int port;

    private ChatRoom chatRoom;

    private ChatQueue chatQueue;

    private String state;

    public God(int playersCount, int port)
    {
        this.port = port;
        players = new ArrayList<>();
        alivePlayers = new ArrayList<>();
        citizens = new ArrayList<>();
        mafias = new ArrayList<>();
        votes = new HashMap<>();
        nightKills = new ArrayList<>();
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
        inquiryCheck = false;
    }

    public void startGame()
    {
        System.out.println(port);
        chatRoom.connect(port, playersCount);
        players = chatRoom.getPlayers();
        for(Player p : players)
        {
            alivePlayers.add(p);
        }
        startFirstNight();

        System.out.println("first night completed.");

        chatRoom.readFromAll(chatQueue);

        loop();
    }

    private void startFirstNight()
    {
        System.out.println("First Night Started.");
        state = "firstNight";
        setRoles();
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
        System.out.println("Set Roles Completed.");

        sendRoles();

        System.out.println("Send Roles Completed.");

        try 
        {
            Thread.sleep(5000);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        mafiasIntroduction();

        System.out.println("mafias intoduction completed.");

        try 
        {
            Thread.sleep(5000);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        introduceDoctorToMayor();

        System.out.println("introduction to mayor completed");

        try 
        {
            Thread.sleep(5000);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

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

        if(inquiryCheck == true)
        {
            inquiryCheck = false;

            int killed = playersCount - alivePlayersCount;

            int mafiaKilled = mafias.size() - aliveMafiaCount;

            int citizenKilled = killed - mafiaKilled;

            chatRoom.sendToAll(new Chat(new Special(), killed + "Players Killed " + mafiaKilled + " Mafias  & " + citizenKilled + " Citizens."));
            
            try 
            {
                Thread.sleep(5000);
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
        }

        if(nightKills.isEmpty() == true)
        {
            chatRoom.sendToAll(new Chat(new Special(), "No One Killed Last Night"));
        }
        else
        {
            String s = nightKills.size() + " Players Killed Last Night \n ";
            for(Player p : nightKills)
            {
                s += p.getUserName() + "\n";
            }
            chatRoom.sendToAll(new Chat(new Special(), s));
            nightKills.clear();
        }

        try 
        {
            Thread.sleep(5000);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }

        chatRoom.sendToAllAlive(new Chat(new Special(), "FREE"));
        chatRoom.sendToAllAlive(new Chat(new Special(), "UNMUTE"));

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

        chatRoom.sendToAllAlive(new Chat(new Special(), "MUTE"));

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
        chatRoom.sendToAll(new Chat(new Special(), alivePlayersToString()));

        chatRoom.sendToAllAlive(new Chat(new Special(), "VOTE"));
        chatRoom.sendToAllAlive(new Chat(new Special(), "UNMUTE"));

        Boolean isTimed = false;
        Thread timer = new Thread(new Timer(isTimed, 30000));
        timer.start();

        while(isTimed == false)
        {
            if(!chatQueue.isEmpty())
            {
                Chat c = chatQueue.popFrontChat();
                if(!((Integer.parseInt(c.getText()) < 1) || (Integer.parseInt(c.getText()) > alivePlayersCount)))
                {
                    votes.put(c.getSender(), alivePlayers.get( ( Integer.parseInt(c.getText()) - 1) ));
                }
            }
        }

        chatRoom.sendToAllAlive(new Chat(new Special(), "MUTE"));

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
            Chat c = chatQueue.popFrontChat();
            if(!((Integer.parseInt(c.getText()) < 1) || (Integer.parseInt(c.getText()) > alivePlayersCount)))
            {
                votes.put(c.getSender(), alivePlayers.get( ( Integer.parseInt(c.getText()) - 1) ));
            }
        }

        try 
        {
            Thread.sleep(1000);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }

        chatRoom.sendToAll(new Chat(new Special(), votersToString()));

        try 
        {
            Thread.sleep(3000);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }

        chatRoom.sendToAll(new Chat(new Special(), votesToString()));
        
        try 
        {
            Thread.sleep(3000);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }

        Player candidate = findKillCandidate();

        

        if(candidate == null)
        {
            chatRoom.sendToAll(new Chat(new Special(), "No One Will Be Killed."));
        }
        else
        {
            chatRoom.sendToAll(new Chat(new Special(), candidate.getUserName() + " Will Be Killed."));
            chatRoom.sendToAll(new Chat(new Special(), "It Is Mayor's Time To Play."));
            boolean isCanceled = mayorRole();
            if(isCanceled == true)
            {
                chatRoom.sendToAll(new Chat(new Special(), "Mayor Canceled The Voting. No One Will Be Killed."));
            }
            else
            {
                chatRoom.sendToAll(new Chat(new Special(), "Mayor Accepted The Voting."));
                candidate.setIsAlive(false);
                alivePlayersCount--;
                alivePlayers.remove(candidate);
                if(candidate.getRole() instanceof Mafia)
                {
                    aliveMafiaCount--;
                }
            }
        }

        votes.clear();

        chatRoom.sendToAll(new Chat(new Special(), "Night Starts In 10 Seconds."));

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

    private void startNight()
    {
        state = "night";

        chatRoom.sendToAll(new Chat(new Special(), "Night Starts."));

        Player mafiasCandidate = mafiaRole();

        Player doctorLecterCandidate = doctorLecterRole();

        Player doctorCandidate = doctorRole();

        detectiveRole();

        Player sniperCandidate = sniperRole();

        Player psychologistCandidate = psychologistRole();

        boolean armoredChoice = armoredRole();

        inquiryCheck = false;

        inquiryCheck = armoredChoice;

        forceMute = null;
        
        forceMute = psychologistCandidate;

        nightKills.clear();

        if(sniperCandidate.getRole() instanceof Sniper)
        {
            nightKills.add(sniperCandidate);
            sniperCandidate.setIsAlive(false);
            alivePlayersCount--;
            alivePlayers.remove(sniperCandidate);
        }
        else if(!(sniperCandidate.getUserName().equals(doctorLecterCandidate.getUserName())))
        {
            nightKills.add(sniperCandidate);
            sniperCandidate.setIsAlive(false);
            alivePlayersCount--;
            alivePlayers.remove(sniperCandidate);
            if(sniperCandidate.getRole() instanceof Mafia)
            {
                aliveMafiaCount--;
            }
        }

        if((mafiasCandidate.getRole() instanceof Armored) && ((Armored)mafiasCandidate.getRole()).getIsArmored() == true)
        {
            ((Armored)mafiasCandidate.getRole()).brokeArmor();
        }
        else if(!(mafiasCandidate.getUserName().equals(doctorCandidate.getUserName())))
        {
            nightKills.add(mafiasCandidate);
            mafiasCandidate.setIsAlive(false);
            alivePlayersCount--;
            alivePlayers.remove(mafiasCandidate);
        }

        chatRoom.sendToAll(new Chat(new Special(), "Day Starts In 10 Seconds."));

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

    private Player mafiaRole()
    {
        chatRoom.sendToAll(new Chat(new Special(), "Mafia Wake Up."));
        Player candidate = null;

        for(Player player : mafias)
        {
            if(player.getIsAlive() == true)
            {
                chatRoom.sendTo(new Chat(new Special(), "Voting Please Choose One Of The Valid Choices In 20 Seconds.\n Just GodFather Vote Is Effective."), player);
                chatRoom.sendTo(new Chat(new Special(), aliveCitizenToString()), player);
                chatRoom.sendTo(new Chat(new Special(), "VOTE"), player);
                chatRoom.sendTo(new Chat(new Special(), "UNMUTE"), player);
            }
        }

        Boolean isTimed = false;
        Thread timer = new Thread(new Timer(isTimed, 20000));
        timer.start();

        int aliveCitizenCount = 0;
        for(Player player : alivePlayers)
        {
            if(player.getRole() instanceof Citizen)
            {
                aliveCitizenCount++;
            }
        }
        
        while(isTimed == false)
        {
            if(!chatQueue.isEmpty())
            {
                Chat c = chatQueue.popFrontChat();
                if(c.getSender().getRole() instanceof GodFather)
                {
                    int k = Integer.parseInt(c.getText());
                    if(!((k < 1) || (k > aliveCitizenCount)))
                    {
                        int i = 1;
                        for(Player player : alivePlayers)
                        {
                            if(player.getRole() instanceof Citizen)
                            {
                                if(i == k)
                                {
                                    candidate = player;
                                }
                                i++;
                            }
                            
                        }
                    }
                }
                chatRoom.sendTo(c, mafias);
            }
        }

        for(Player player : mafias)
        {
            if(player.getIsAlive() == true)
            {
                chatRoom.sendTo(new Chat(new Special(), "MUTE"), player);
            }
        }

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
            Chat c = chatQueue.popFrontChat();
            if(c.getSender().getRole() instanceof GodFather)
            {
                int k = Integer.parseInt(c.getText());
                if(!((k < 1) || (k > aliveCitizenCount)))
                {
                    int i = 1;
                    for(Player player : alivePlayers)
                    {
                        if(player.getRole() instanceof Citizen)
                        {
                            if(i == k)
                            {
                                candidate = player;
                            }
                            i++;
                        }
                        
                    }
                }
            }
            chatRoom.sendTo(c, mafias);
        }

        chatRoom.sendToAll(new Chat(new Special(), "Mafia Sleep."));
        return candidate;
    }

    private Player doctorLecterRole()
    {
        chatRoom.sendToAll(new Chat(new Special(), "DoctorLecter Wake Up."));
        Player candidate = null;

        Player doctorLecter = null;
        for(Player p : alivePlayers)
        {
            if(p.getRole() instanceof DoctorLecter)
            {
                doctorLecter = p;
            }
        }

        if(doctorLecter == null)
        {
            Boolean isTimed = false;
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            while(isTimed == false)
            {

            }
        }
        else
        {
            chatRoom.sendTo(new Chat(new Special(), "Please Choose One Of The Mafias To Heal In 20 Seconds. You Cant Choose Yourself Twice."), doctorLecter);
            chatRoom.sendTo(new Chat(new Special(), aliveMafiaToString()), doctorLecter);
            chatRoom.sendTo(new Chat(new Special(), "VOTE"), doctorLecter);
            chatRoom.sendTo(new Chat(new Special(), "UNMUTE"), doctorLecter);

            Boolean isTimed = false;
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            Chat c = null;

            while(isTimed == false)
            {
                if(!chatQueue.isEmpty())
                {
                    c = chatQueue.popFrontChat();
                }
            }

            chatRoom.sendTo(new Chat(new Special(), "MUTE"), doctorLecter);

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
                c = chatQueue.popFrontChat();            
            }

            if(c == null)
            {
                candidate = null;
            }
            else
            {
                int k = Integer.parseInt(c.getText());

                if(!((k < 1) || (k > aliveMafiaCount)))
                {
                    int i = 1;
                    for(Player player : alivePlayers)
                    {
                        if(player.getRole() instanceof Mafia)
                        {
                            if(i == k)
                            {
                                candidate = player;
                            }
                            i++;
                        }
                    }
                    if((candidate.getRole() instanceof DoctorLecter) && (((DoctorLecter)doctorLecter.getRole()).getIsSelfHealed() == true))
                    {
                        candidate = null;
                    }
                    else if((candidate.getRole() instanceof DoctorLecter) && (((DoctorLecter)doctorLecter.getRole()).getIsSelfHealed() == false))
                    {
                        ((DoctorLecter)doctorLecter.getRole()).setIsSelfHealed();
                    }
                }
            }
        }

        chatRoom.sendToAll(new Chat(new Special(), "DoctorLecter Sleep."));
        chatRoom.sendTo(new Chat(new Special(), candidate.toString()), mafias);
        return candidate;
    }

    private Player doctorRole()
    {
        chatRoom.sendToAll(new Chat(new Special(), "Doctor Wake Up."));
        Player candidate = null;

        Player doctor = null;
        for(Player p : alivePlayers)
        {
            if(p.getRole() instanceof Doctor)
            {
                doctor = p;
            }
        }

        if(doctor == null)
        {
            Boolean isTimed = false;
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            while(isTimed == false)
            {

            }
        }
        else
        {
            chatRoom.sendTo(new Chat(new Special(), "Please Choose One Of The Players To Heal In 20 Seconds. You Cant Choose Yourself 3 Times."), doctor);
            chatRoom.sendTo(new Chat(new Special(), alivePlayersToString()), doctor);
            chatRoom.sendTo(new Chat(new Special(), "VOTE"), doctor);
            chatRoom.sendTo(new Chat(new Special(), "UNMUTE"), doctor);

            Boolean isTimed = false;
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            Chat c = null;

            while(isTimed == false)
            {
                if(!chatQueue.isEmpty())
                {
                    c = chatQueue.popFrontChat();
                }
            }

            chatRoom.sendTo(new Chat(new Special(), "MUTE"), doctor);

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
                c = chatQueue.popFrontChat();            
            }

            if(c == null)
            {
                candidate = null;
            }
            else
            {
                int k = Integer.parseInt(c.getText());

                if(!((k < 1) || (k > alivePlayersCount)))
                {
                    int i = 1;
                    for(Player player : alivePlayers)
                    {
                        if(i == k)
                        {
                            candidate = player;
                        }
                        i++;
                    }
                    if((candidate.getRole() instanceof Doctor) && (((Doctor)doctor.getRole()).getSelfHealCount() == 2))
                    {
                        candidate = null;
                    }
                    else if((candidate.getRole() instanceof Doctor) && (((Doctor)doctor.getRole()).getSelfHealCount() < 2))
                    {
                        ((Doctor)doctor.getRole()).setSelfHealCount( ((Doctor)doctor.getRole()).getSelfHealCount() + 1 );
                    }
                }

            }
        }

        chatRoom.sendToAll(new Chat(new Special(), "Doctor Sleep."));
        return candidate;
    }

    private void detectiveRole()
    {
        chatRoom.sendToAll(new Chat(new Special(), "Detective Wake Up."));
        Player candidate = null;

        Player detective = null;
        for(Player p : alivePlayers)
        {
            if(p.getRole() instanceof Detective)
            {
                detective = p;
            }
        }

        if(detective== null)
        {
            Boolean isTimed = false;
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            while(isTimed == false)
            {

            }
        }
        else
        {
            chatRoom.sendTo(new Chat(new Special(), "Please Choose One Of The Players To Detect In 20 Seconds. You Cant Choose Yourself."), detective);
            chatRoom.sendTo(new Chat(new Special(), alivePlayersToString()), detective);
            chatRoom.sendTo(new Chat(new Special(), "VOTE"), detective);
            chatRoom.sendTo(new Chat(new Special(), "UNMUTE"), detective);

            Boolean isTimed = false;
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            Chat c = null;

            while(isTimed == false)
            {
                if(!chatQueue.isEmpty())
                {
                    c = chatQueue.popFrontChat();
                }
            }

            chatRoom.sendTo(new Chat(new Special(), "MUTE"), detective);

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
                c = chatQueue.popFrontChat();            
            }

            if(c == null)
            {
                candidate = null;
            }
            else
            {
                int k = Integer.parseInt(c.getText());

                if(!((k < 1) || (k > alivePlayersCount)))
                {
                    int i = 1;
                    for(Player player : alivePlayers)
                    {
                        if(i == k)
                        {
                            candidate = player;
                        }
                        i++;
                    }
                    if((candidate.getRole() instanceof Detective))
                    {
                        candidate = null;
                    }
                }
            }
        }
        if(candidate !=null)
        {
            if((candidate.getRole() instanceof GodFather) || (candidate.getRole() instanceof Citizen))
            {
                chatRoom.sendTo(new Chat(new Special(), "Your Chosen Player Is Citizen."), detective);
            }
            else
            {
                chatRoom.sendTo(new Chat(new Special(), "Your Chosen Player Is Mafia."), detective);
            }
        }

        chatRoom.sendToAll(new Chat(new Special(), "Detective Sleep."));
    }

    private Player sniperRole()
    {
        chatRoom.sendToAll(new Chat(new Special(), "Sniper Wake Up."));
        Player candidate = null;

        Player sniper = null;
        for(Player p : alivePlayers)
        {
            if(p.getRole() instanceof Sniper)
            {
                sniper = p;
            }
        }

        if(sniper == null || ((Sniper)sniper.getRole()).getSnipeCount() == (mafias.size() - 2))
        {
            Boolean isTimed = false;
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            while(isTimed == false)
            {

            }
        }
        else
        {
            chatRoom.sendTo(new Chat(new Special(), "Please Choose One Of The Players To Snipe In 20 Seconds."), sniper);
            chatRoom.sendTo(new Chat(new Special(), alivePlayersToString()), sniper);
            chatRoom.sendTo(new Chat(new Special(), "VOTE"), sniper);
            chatRoom.sendTo(new Chat(new Special(), "UNMUTE"), sniper);

            Boolean isTimed = false;
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            Chat c = null;

            while(isTimed == false)
            {
                if(!chatQueue.isEmpty())
                {
                    c = chatQueue.popFrontChat();
                }
            }

            chatRoom.sendTo(new Chat(new Special(), "MUTE"), sniper);

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
                c = chatQueue.popFrontChat();            
            }

            if(c == null)
            {
                candidate = null;
            }
            else
            {
                int k = Integer.parseInt(c.getText());

                if(!((k < 1) || (k > alivePlayersCount)))
                {
                    int i = 1;
                    for(Player player : alivePlayers)
                    {
                        if(i == k)
                        {
                            candidate = player;
                        }
                        i++;
                    }
                    if((candidate.getRole() instanceof Citizen))
                    {
                        candidate = sniper;
                    }
                    ((Sniper)sniper.getRole()).setSnipeCount(((Sniper)sniper.getRole()).getSnipeCount() + 1);
                }
            }
        }

        chatRoom.sendToAll(new Chat(new Special(), "Sniper Sleep."));
        return candidate;
    }

    private Player psychologistRole() 
    {
        chatRoom.sendToAll(new Chat(new Special(), "Psychologist Wake Up."));
        Player candidate = null;

        Player psychologist = null;
        for(Player p : alivePlayers)
        {
            if(p.getRole() instanceof Psychologist)
            {
                psychologist = p;
            }
        }

        if(psychologist == null)
        {
            Boolean isTimed = false;
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            while(isTimed == false)
            {

            }
        }
        else
        {
            chatRoom.sendTo(new Chat(new Special(), "Please Choose One Of The Players To Mute In 20 Seconds."), psychologist);
            chatRoom.sendTo(new Chat(new Special(), alivePlayersToString()), psychologist);
            chatRoom.sendTo(new Chat(new Special(), "VOTE"), psychologist);
            chatRoom.sendTo(new Chat(new Special(), "UNMUTE"), psychologist);

            Boolean isTimed = false;
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            Chat c = null;

            while(isTimed == false)
            {
                if(!chatQueue.isEmpty())
                {
                    c = chatQueue.popFrontChat();
                }
            }

            chatRoom.sendTo(new Chat(new Special(), "MUTE"), psychologist);

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
                c = chatQueue.popFrontChat();            
            }

            if(c == null)
            {
                candidate = null;
            }
            else
            {
                int k = Integer.parseInt(c.getText());

                if(!((k < 1) || (k > alivePlayersCount)))
                {
                    int i = 1;
                    for(Player player : alivePlayers)
                    {
                        if(i == k)
                        {
                            candidate = player;
                        }
                        i++;
                    }
                }
            }
        }

        chatRoom.sendToAll(new Chat(new Special(), "Psychologist Sleep."));
        return candidate;
    }

    private boolean armoredRole() 
    {
        chatRoom.sendToAll(new Chat(new Special(), "Armored Wake Up."));
        Player armored = null;
        for(Player p : alivePlayers)
        {
            if(p.getRole() instanceof Armored)
            {
                armored = p;
            }
        }

        if(armored == null || ((Armored)armored.getRole()).getInquiryCount() == 2)
        {
            Boolean isTimed = false;
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            while(isTimed == false)
            {

            }
            chatRoom.sendToAll(new Chat(new Special(), "Armored Sleep."));
            return false;
        }
        else
        {
            chatRoom.sendTo(new Chat(new Special(), "Armored If Your Want To Have Inquiry Tomorrow Just Send 1\nNote That Your Last Message Will Be Accepted."), armored);
            
            chatRoom.sendTo(new Chat(new Special(), "VOTE"), armored);
            chatRoom.sendTo(new Chat(new Special(), "UNMUTE"), armored);

            Boolean isTimed = false;
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            Chat c = null;

            while(isTimed == false)
            {
                if(!chatQueue.isEmpty())
                {
                    c = chatQueue.popFrontChat();
                }
            }

            chatRoom.sendTo(new Chat(new Special(), "MUTE"), armored);

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
                c = chatQueue.popFrontChat();            
            }

            if(c == null)
            {
                chatRoom.sendToAll(new Chat(new Special(), "Armored Sleep."));
                return false;
            }
            else
            {
                if(Integer.parseInt(c.getText()) == 1)
                {
                    ((Armored)armored.getRole()).setInquiryCount(((Armored)armored.getRole()).getInquiryCount() + 1);
                    chatRoom.sendToAll(new Chat(new Special(), "Armored Sleep."));
                    return true;
                }
                else
                {
                    chatRoom.sendToAll(new Chat(new Special(), "Armored Sleep."));
                    return false;
                }
            }
        }
    }

    private boolean mayorRole()
    {
        Player mayor = null;
        for(Player p : alivePlayers)
        {
            if(p.getRole() instanceof Mayor)
            {
                mayor = p;
            }
        }

        if(mayor == null)
        {
            Boolean isTimed = false;
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            while(isTimed == false)
            {

            }

            return false;
        }
        else
        {
            chatRoom.sendTo(new Chat(new Special(), "Mayor If Your Want To Cancel This Voting Just Send 1\nNote That Your Last Message Will Be Accepted."), mayor);
            
            chatRoom.sendTo(new Chat(new Special(), "VOTE"), mayor);
            chatRoom.sendTo(new Chat(new Special(), "UNMUTE"), mayor);

            Boolean isTimed = false;
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            boolean isCanceled = false;

            Chat c = null;

            while(isTimed == false)
            {
                if(!chatQueue.isEmpty())
                {
                    c = chatQueue.popFrontChat();
                }
            }

            chatRoom.sendTo(new Chat(new Special(), "MUTE"), mayor);

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
                c = chatQueue.popFrontChat();            
            }

            if(c == null)
            {
                return false;
            }
            else
            {
                if(Integer.parseInt(c.getText()) == 1)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
    }

    private String alivePlayersToString()
    {
        String s = "";
        int i = 1;
        for(Player player : alivePlayers)
        {
            s += ANSI_PURPLE + i + ANSI_RESET + " : " + ANSI_BLUE + player.getUserName() + ANSI_RESET + "\n";
            i++;
        }
        return s;
    }

    private String aliveCitizenToString()
    {
        String s = "";
        int i = 1;
        for(Player player : alivePlayers)
        {
            if(player.getRole() instanceof Citizen)
            {
                s += ANSI_PURPLE + i + ANSI_RESET + " : " + ANSI_BLUE + player.getUserName() + ANSI_RESET + "\n";
                i++;
            }
        }
        return s;
    }

    public String aliveMafiaToString()
    {
        String s = "";
        int i = 1;
        for(Player player : alivePlayers)
        {
            if(player.getRole() instanceof Mafia)
            {
                s += ANSI_PURPLE + i + ANSI_RESET + " : " + ANSI_BLUE + player.getUserName() + ANSI_RESET + "\n";
                i++;
            }
        }
        return s;
    }

    private String votersToString()
    {
        String s = "";
        for(Player voter : votes.keySet())
        {
            s += ANSI_YELLOW + voter.getUserName() + ANSI_GREEN + " -> " + ANSI_PURPLE + votes.get(voter) + ANSI_RESET + "\n";
        }
        return s;

    }

    private String votesToString()
    {
        String s = "";
        int[] a = new int[alivePlayersCount];

        for(int i = 0; i < aliveMafiaCount; i++)
        {
            a[i] = 0;
        }

        for(Player voter : votes.keySet())
        {
            a[alivePlayers.indexOf(votes.get(voter))]++;
        }

        for(int i = 0; i < aliveMafiaCount; i++)
        {
            s += ANSI_RED + alivePlayers.get(i).getUserName() + ANSI_GREEN + " : " + ANSI_PURPLE + a[i] + ANSI_RESET + "\n";
        }

        return s;
    }

    private Player findKillCandidate()
    {
        int[] a = new int[alivePlayersCount];

        for(int i = 0; i < alivePlayersCount; i++)
        {
            a[i] = 0;
        }

        for(Player voter : votes.keySet())
        {
            a[alivePlayers.indexOf(votes.get(voter))]++;
        }

        Player candidate = null;
        int max = 0;

        for(int i = 0; i < alivePlayersCount; i++)
        {
            if(a[i] > max)
            {
                max = a[i];
                candidate = alivePlayers.get(i);
            }
        }

        if(max > ((alivePlayersCount - 1) / 2))
        {
            return candidate;
        }
        
        return null;
    }

    private void checkFinishConditions()
    {
        if(aliveMafiaCount == 0)
        {   
            chatRoom.sendToAll(new Chat(new Special(), "Congrats To All Citizens \n  Citizen Won The Game."));
            chatRoom.sendToAll(new Chat(new Special(), "END"));
            try 
            {
                Thread.sleep(1000);
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
            System.exit(0);
        }
        else if(aliveMafiaCount == (alivePlayersCount - aliveMafiaCount))
        {
            chatRoom.sendToAll(new Chat(new Special(), "Congrats To All Mafias \n  Mafia Won The Game."));
            chatRoom.sendToAll(new Chat(new Special(), "END"));
            try 
            {
                Thread.sleep(1000);
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
            System.exit(0);
        }
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
            chatRoom.sendTo(new Chat(new Special(), player.getUserName() + " Your Role Is : " + player.getRole().toString()), player);
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
