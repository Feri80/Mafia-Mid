package model.logic;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import model.network.*;
import model.roles.*;

/**
 * This class contains the logic of the game and most of the methods of server
 * 
 * @author Farhad Aman
 * @version 1.0
 */
public class God 
{
    /**
     * some colors program use in console
     */
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m"; 

    /**
     * the players of the game
     */
    private ArrayList<Player> players;   

    /**
     * the players are alive
     */
    private ArrayList<Player> alivePlayers;   

    /**
     * the citizens
     */
    private ArrayList<Player> citizens;

    /**
     * the mafias
     */
    private ArrayList<Player> mafias;

    /**
     * the votes of players in voting state
     */
    private HashMap<Player, Player> votes;

    /**
     * the kills in night state
     */
    private ArrayList<Player> nightKills;

    /**
     * the whole chats of players in day
     */
    private ArrayList<Chat> chats;

    /**
     * the player who is force muted in day
     */
    private Player forceMute;

    /**
     * the player who is the head of mafia first god father the random
     */
    private Player headOfMafia;

    /**
     * true if inquiry check should be done 
     */
    private boolean inquiryCheck;

    /**
     * the number of players of the game
     */
    private int playersCount;

    /**
     * the number of alive players of the game
     */
    private int alivePlayersCount;

    /**
     * the number of alive mafias
     */
    private int aliveMafiaCount;

    /**
     * the port of the server
     */
    private int port;

    /**
     * the chat room of the game
     */
    private ChatRoom chatRoom;

    /**
     * the chat queue of the game
     */
    private ChatQueue chatQueue;

    /**
     * creates a new god
     * @param playersCount
     * @param port
     */
    public God(int playersCount, int port)
    {
        this.players = new ArrayList<>();
        this.alivePlayers = new ArrayList<>();
        this.citizens = new ArrayList<>();
        this.mafias = new ArrayList<>();
        this.votes = new HashMap<>();
        this.nightKills = new ArrayList<>();
        this.chats = new ArrayList<>();
        this.forceMute = null;
        this.headOfMafia = null;
        this.inquiryCheck = false;
        this.playersCount = playersCount;
        this.alivePlayersCount = playersCount;
        if(playersCount <= 7)
        {
            this.aliveMafiaCount = 1;
        }
        else
        {
            this.aliveMafiaCount = playersCount / 3;
        }
        this.port = port;
        chatRoom = new ChatRoom();
        chatQueue = new ChatQueue();
    }

    /**
     * this methods starts the whole game
     */
    public void startGame()
    {
        System.out.println("server port : " + port);

        chatRoom.connect(port, playersCount);

        players = chatRoom.getPlayers();

        for(Player p : players)
        {
            alivePlayers.add(p);
        }

        startFirstNight();

        System.out.println("first night completed.");

        chatRoom.readFromAll(this);

        loop();
    }

    /**
     * this methods starts the first night of the game
     */
    private void startFirstNight()
    {
        System.out.println("first night started.");

        setRoles();

        for(Player player : players)
        {
            if(player.getRole() instanceof GodFather)
            {
                headOfMafia = player;
            }
            if(player.getRole() instanceof Mafia)
            {
                mafias.add(player);
            }
            else
            {
                citizens.add(player);
            }
        }

        System.out.println("set roles completed.");

        sendRoles();

        System.out.println("send roles completed.");

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

        chatRoom.sendToAll(new Chat("SPECIAL", "Day Starts In 10 Seconds."));

        try 
        {
            Thread.sleep(10000);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
    }

    /**
     * this method performs the game loop
     */
    private void loop()
    {
        while(true)
        {
            checkFinishConditions();
            startDay();
            startVoting();
            checkFinishConditions();
            startNight();
        }
    }

    /**
     * this method starts the day state
     */
    private void startDay()
    {
        chatRoom.sendToAll(new Chat("SPECIAL", "Day Starts You Can Wake Up."));

        if(inquiryCheck == true)
        {
            inquiryCheck = false;

            int killed = playersCount - alivePlayersCount;

            int mafiaKilled = mafias.size() - aliveMafiaCount;

            int citizenKilled = killed - mafiaKilled;

            chatRoom.sendToAll(new Chat("SPECIAL", killed + " Players Killed : " + mafiaKilled + " Mafias & " + citizenKilled + " Citizens."));
            
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
            chatRoom.sendToAll(new Chat("SPECIAL", "No One Killed Last Night"));
        }
        else
        {
            String s = nightKills.size() + " Players Killed Last Night \n" + ANSI_YELLOW;
            for(Player p : nightKills)
            {
                s += p.getUserName() + "\n";
            }
            s += ANSI_RESET;
            chatRoom.sendToAll(new Chat("SPECIAL", s));
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

        chatRoom.sendToAll(new Chat("SPECIAL", deadOrAliveToString()));

        try 
        {
            Thread.sleep(5000);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }

        chatRoom.sendToAllAlive(new Chat("SPECIAL", "FREE"));
        chatRoom.sendToAllAlive(new Chat("SPECIAL", "UNMUTE"));

        if(forceMute != null)
        {
            chatRoom.sendToAll(new Chat("SPECIAL",ANSI_YELLOW + forceMute.toString() + ANSI_BLUE + " Is Muted In This Day."));
        }

        ArrayList<Integer> isTimed = new ArrayList<>();
        Thread timer = new Thread(new Timer(isTimed, 120000));
        timer.start();

        System.out.println("chat started.");

        while(isTimed.size() == 0)
        {
            if(!chatQueue.isEmpty())
            {
                Chat c = chatQueue.popFrontChat();
                saveChat(c);
                chatRoom.sendToAll(c);
            }
        }

        System.out.println("time out.");

        chatRoom.sendToAllAlive(new Chat("SPECIAL", "MUTE"));

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
            saveChat(c);
            chatRoom.sendToAll(c);
        }

        chatRoom.sendToAll(new Chat("SPECIAL", "Voting Starts In 10 Seconds."));

        try 
        {
            Thread.sleep(10000);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }

        forceMute = null;
    }

    /**
     * this method starts the voting state
     */
    private void startVoting()
    {
        chatRoom.sendToAll(new Chat("SPECIAL", "Voting Please Choose One Of The Valid Choices In 30 Seconds.\nSend Your Vote At Least 2 Times."));
        chatRoom.sendToAll(new Chat("SPECIAL", alivePlayersToString()));

        chatRoom.sendToAllAlive(new Chat("SPECIAL", "VOTE"));
        chatRoom.sendToAllAlive(new Chat("SPECIAL", "UNMUTE"));

        ArrayList<Integer> isTimed = new ArrayList<>();
        Thread timer = new Thread(new Timer(isTimed, 40000));
        timer.start();

        votes.clear();

        while(isTimed.size() == 0)
        {
            if(!chatQueue.isEmpty())
            {
                Chat c = chatQueue.popFrontChat();
                if((Integer.parseInt(c.getText()) >= 1) && (Integer.parseInt(c.getText()) <= alivePlayersCount))
                {
                    for(Player p : alivePlayers)
                    {
                        if(p.getUserName().equals(c.getSender()))
                        {
                            votes.put(p, alivePlayers.get(Integer.parseInt(c.getText()) - 1));
                        }
                    }
                }
            }
        }

        chatRoom.sendToAllAlive(new Chat("SPECIAL", "MUTE"));

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
            if((Integer.parseInt(c.getText()) >= 1) && (Integer.parseInt(c.getText()) <= alivePlayersCount))
            {
                for(Player p : alivePlayers)
                {
                    if(p.getUserName().equals(c.getSender()))
                    {
                        votes.put(p, alivePlayers.get(Integer.parseInt(c.getText()) - 1));
                    }
                }
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

        chatRoom.sendToAll(new Chat("SPECIAL", votersToString()));

        System.out.println("voters to string completed.");

        try 
        {
            Thread.sleep(3000);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }

        chatRoom.sendToAll(new Chat("SPECIAL", votesToString()));
        
        System.out.println("votes to string completed.");

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
            chatRoom.sendToAll(new Chat("SPECIAL", "No One Will Be Killed."));
        }
        else
        {
            chatRoom.sendToAll(new Chat("SPECIAL", ANSI_YELLOW + candidate.getUserName() + ANSI_BLUE + " Will Be Killed."));
            chatRoom.sendToAll(new Chat("SPECIAL", "It Is Mayor's Time To Play."));

            boolean isCanceled = mayorRole();

            if(isCanceled == true)
            {
                chatRoom.sendToAll(new Chat("SPECIAL", "Mayor Canceled The Voting. No One Will Be Killed."));
            }
            else
            {
                chatRoom.sendToAll(new Chat("SPECIAL", "Mayor Accepted The Voting."));
                if(candidate.getUserName().equals(headOfMafia.getUserName()))
                {
                    changeHeadOfMafia();
                }
                chatRoom.sendTo(new Chat("SPECIAL", "KILL"), candidate);
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

        chatRoom.sendToAll(new Chat("SPECIAL", "Night Starts In 10 Seconds."));

        try 
        {
            Thread.sleep(10000);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
    }

    /**
     * this method starts the night state
     */
    private void startNight()
    {
        chatRoom.sendToAll(new Chat("SPECIAL", "Night Starts."));

        System.out.println("mafias role started.");

        Player mafiasCandidate = mafiaRole();

        System.out.println("mafias candiate is " + mafiasCandidate);

        System.out.println("doctor lecter role started.");

        Player doctorLecterCandidate = doctorLecterRole();

        System.out.println("doctor lecter candidate is " + doctorLecterCandidate);

        System.out.println("doctor role started.");

        Player doctorCandidate = doctorRole();

        System.out.println("doctor candiate is " + doctorCandidate);

        System.out.println("detective role started.");

        detectiveRole();

        System.out.println("detective role completed.");

        System.out.println("sniper role started.");

        Player sniperCandidate = sniperRole();

        System.out.println("sniper candidate is " + sniperCandidate);

        System.out.println("psychologist role started.");

        Player psychologistCandidate = psychologistRole();

        System.out.println("psychologist role completed.");

        System.out.println("armored role started.");

        boolean armoredChoice = armoredRole();

        System.out.println("armored role completed.");

        inquiryCheck = false;

        inquiryCheck = armoredChoice;

        System.out.println("armoredChoice is " + inquiryCheck);

        forceMute = null;
        
        forceMute = psychologistCandidate;

        System.out.println("force mute is " + forceMute);

        nightKills.clear();

        if(sniperCandidate != null && sniperCandidate.getRole() instanceof Sniper)
        {
            chatRoom.sendTo(new Chat("SPECIAL", "KILL"), sniperCandidate);
            nightKills.add(sniperCandidate);
            sniperCandidate.setIsAlive(false);
            alivePlayersCount--;
            alivePlayers.remove(sniperCandidate);
        }
        else if(sniperCandidate != null && doctorLecterCandidate != null && !(sniperCandidate.getUserName().equals(doctorLecterCandidate.getUserName())))
        {
            if(sniperCandidate.getUserName().equals(headOfMafia.getUserName()))
            {
                changeHeadOfMafia();
            }
            chatRoom.sendTo(new Chat("SPECIAL", "KILL"), sniperCandidate);
            nightKills.add(sniperCandidate);
            sniperCandidate.setIsAlive(false);
            alivePlayersCount--;
            alivePlayers.remove(sniperCandidate);
            if(sniperCandidate.getRole() instanceof Mafia)
            {
                aliveMafiaCount--;
            }
        }

        if(mafiasCandidate != null && (mafiasCandidate.getRole() instanceof Armored) && ((Armored)mafiasCandidate.getRole()).getIsArmored() == true)
        {
            ((Armored)mafiasCandidate.getRole()).brokeArmor();
        }
        else if(mafiasCandidate != null && (doctorCandidate == null || !(mafiasCandidate.getUserName().equals(doctorCandidate.getUserName()))))
        {
            chatRoom.sendTo(new Chat("SPECIAL", "KILL"), mafiasCandidate);
            nightKills.add(mafiasCandidate);
            mafiasCandidate.setIsAlive(false);
            alivePlayersCount--;
            alivePlayers.remove(mafiasCandidate);
        }
        
        chatRoom.sendToAll(new Chat("SPECIAL", "Day Starts In 10 Seconds."));

        try 
        {
            Thread.sleep(10000);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }

        System.out.println("night completed.");
    }

    /**
     * this method starts the mafia role in the night
     * @return the mafia candidate to kill
     */
    private Player mafiaRole()
    {
        chatRoom.sendToAll(new Chat("SPECIAL", "Mafia Wake Up."));
        Player candidate = null;

        for(Player player : mafias)
        {
            if(player.getIsAlive() == true)
            {
                chatRoom.sendTo(new Chat("SPECIAL", "Please Choose One Of The Valid Choices In 20 Seconds.\nJust Head Of Mafia's Vote Is Effective.\nSend Your Vote At Least 2 Times."), player);
                chatRoom.sendTo(new Chat("SPECIAL", aliveCitizenToString()), player);
                chatRoom.sendTo(new Chat("SPECIAL", "VOTE"), player);
                chatRoom.sendTo(new Chat("SPECIAL", "UNMUTE"), player);
            }
        }

        int aliveCitizenCount = 0;
        for(Player player : alivePlayers)
        {
            if(player.getRole() instanceof Citizen)
            {
                aliveCitizenCount++;
            }
        }

        ArrayList<Integer> isTimed = new ArrayList<>();
        Thread timer = new Thread(new Timer(isTimed, 20000));
        timer.start();
        
        while(isTimed.size() == 0)
        {
            if(!chatQueue.isEmpty())
            {
                Chat c = chatQueue.popFrontChat();
                if(c.getSender().equals(headOfMafia.getUserName()))
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
                chatRoom.sendTo(new Chat("SPECIAL", "MUTE"), player);
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
            if(c.getSender().equals(headOfMafia.getUserName()))
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

        chatRoom.sendToAll(new Chat("SPECIAL", "Mafia Sleep."));
        return candidate;
    }

    /**
     * this method starts the doctor lecter role in the night
     * @return the doctor lecter candidate to heal
     */
    private Player doctorLecterRole()
    {
        chatRoom.sendToAll(new Chat("SPECIAL", "DoctorLecter Wake Up."));

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
            ArrayList<Integer> isTimed = new ArrayList<>();
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            while(isTimed.size() == 0)
            {
                try 
                {
                    Thread.sleep(1000);
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            chatRoom.sendTo(new Chat("SPECIAL", "Please Choose One Of The Mafias To Heal In 20 Seconds.\nYou Cant Choose Yourself Twice.\nSend Your Vote At Least 2 Times."), doctorLecter);
            chatRoom.sendTo(new Chat("SPECIAL", aliveMafiaToString()), doctorLecter);
            chatRoom.sendTo(new Chat("SPECIAL", "VOTE"), doctorLecter);
            chatRoom.sendTo(new Chat("SPECIAL", "UNMUTE"), doctorLecter);

            ArrayList<Integer> isTimed = new ArrayList<>();
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            Chat c = null;

            while(isTimed.size() == 0)
            {
                if(!chatQueue.isEmpty())
                {
                    c = chatQueue.popFrontChat();
                }
            }

            chatRoom.sendTo(new Chat("SPECIAL", "MUTE"), doctorLecter);

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

        chatRoom.sendToAll(new Chat("SPECIAL", "DoctorLecter Sleep."));
        if(candidate != null)
        {
            chatRoom.sendTo(new Chat("SPECIAL", candidate.toString()), mafias);
        }
        return candidate;
    }

    /**
     * this method starts the doctor role in the night
     * @return the doctor candidate to heal
     */
    private Player doctorRole()
    {
        chatRoom.sendToAll(new Chat("SPECIAL", "Doctor Wake Up."));

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
            ArrayList<Integer> isTimed = new ArrayList<>();
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            while(isTimed.size() == 0)
            {
                try 
                {
                    Thread.sleep(1000);
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            chatRoom.sendTo(new Chat("SPECIAL", "Please Choose One Of The Players To Heal In 20 Seconds.\nYou Cant Choose Yourself 3 Times.\nSend Your Vote At Least 2 Times."), doctor);
            chatRoom.sendTo(new Chat("SPECIAL", alivePlayersToString()), doctor);
            chatRoom.sendTo(new Chat("SPECIAL", "VOTE"), doctor);
            chatRoom.sendTo(new Chat("SPECIAL", "UNMUTE"), doctor);

            ArrayList<Integer> isTimed = new ArrayList<>();
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            Chat c = null;

            while(isTimed.size() == 0)
            {
                if(!chatQueue.isEmpty())
                {
                    c = chatQueue.popFrontChat();
                }
            }

            chatRoom.sendTo(new Chat("SPECIAL", "MUTE"), doctor);

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

        chatRoom.sendToAll(new Chat("SPECIAL", "Doctor Sleep."));
        return candidate;
    }

    /**
     * this method starts the detective role in the night
     */
    private void detectiveRole()
    {
        chatRoom.sendToAll(new Chat("SPECIAL", "Detective Wake Up."));

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
            ArrayList<Integer> isTimed = new ArrayList<>();
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            while(isTimed.size() == 0)
            {
                try 
                {
                    Thread.sleep(1000);
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            chatRoom.sendTo(new Chat("SPECIAL", "Please Choose One Of The Players To Detect In 20 Seconds.\nYou Cant Choose Yourself.\nSend Your Vote At Least 2 Times."), detective);
            chatRoom.sendTo(new Chat("SPECIAL", alivePlayersToString()), detective);
            chatRoom.sendTo(new Chat("SPECIAL", "VOTE"), detective);
            chatRoom.sendTo(new Chat("SPECIAL", "UNMUTE"), detective);

            ArrayList<Integer> isTimed = new ArrayList<>();
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            Chat c = null;

            while(isTimed.size() == 0)
            {
                if(!chatQueue.isEmpty())
                {
                    c = chatQueue.popFrontChat();
                }
            }

            chatRoom.sendTo(new Chat("SPECIAL", "MUTE"), detective);

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

        if(candidate != null)
        {
            if((candidate.getRole() instanceof GodFather) || (candidate.getRole() instanceof Citizen))
            {
                chatRoom.sendTo(new Chat("SPECIAL", "Your Chosen Player Is Citizen."), detective);
            }
            else
            {
                chatRoom.sendTo(new Chat("SPECIAL", "Your Chosen Player Is Mafia."), detective);
            }
        }

        chatRoom.sendToAll(new Chat("SPECIAL", "Detective Sleep."));
    }

    /**
     * this method starts the sniper role in the night
     * @return the sniper candidate to shot
     */
    private Player sniperRole()
    {
        chatRoom.sendToAll(new Chat("SPECIAL", "Sniper Wake Up."));

        Player candidate = null;

        Player sniper = null;
        for(Player p : alivePlayers)
        {
            if(p.getRole() instanceof Sniper)
            {
                sniper = p;
            }
        }

        int sniperShots = Math.max(mafias.size() - 2, 1);

        if(sniper == null || ((Sniper)sniper.getRole()).getSnipeCount() == sniperShots)
        {
            ArrayList<Integer> isTimed = new ArrayList<>();
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            while(isTimed.size() == 0)
            {
                try 
                {
                    Thread.sleep(1000);
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            chatRoom.sendTo(new Chat("SPECIAL", "Please Choose One Of The Players To Snipe In 20 Seconds.\nSend Your Vote At Least 2 Times."), sniper);
            chatRoom.sendTo(new Chat("SPECIAL", alivePlayersToString()), sniper);
            chatRoom.sendTo(new Chat("SPECIAL", "VOTE"), sniper);
            chatRoom.sendTo(new Chat("SPECIAL", "UNMUTE"), sniper);

            ArrayList<Integer> isTimed = new ArrayList<>();
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            Chat c = null;

            while(isTimed.size() == 0)
            {
                if(!chatQueue.isEmpty())
                {
                    c = chatQueue.popFrontChat();
                }
            }

            chatRoom.sendTo(new Chat("SPECIAL", "MUTE"), sniper);

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

        chatRoom.sendToAll(new Chat("SPECIAL", "Sniper Sleep."));
        return candidate;
    }

    /**
     * this method starts the psychologist role in the night
     * @return the psychologist candidate to force mute
     */
    private Player psychologistRole() 
    {
        chatRoom.sendToAll(new Chat("SPECIAL", "Psychologist Wake Up."));

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
            ArrayList<Integer> isTimed = new ArrayList<>();
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            while(isTimed.size() == 0)
            {
                try 
                {
                    Thread.sleep(1000);
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            chatRoom.sendTo(new Chat("SPECIAL", "Please Choose One Of The Players To Mute In 20 Seconds.\nSend Your Vote At Least 2 Times."), psychologist);
            chatRoom.sendTo(new Chat("SPECIAL", alivePlayersToString()), psychologist);
            chatRoom.sendTo(new Chat("SPECIAL", "VOTE"), psychologist);
            chatRoom.sendTo(new Chat("SPECIAL", "UNMUTE"), psychologist);

            ArrayList<Integer> isTimed = new ArrayList<>();
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            Chat c = null;

            while(isTimed.size() == 0)
            {
                if(!chatQueue.isEmpty())
                {
                    c = chatQueue.popFrontChat();
                }
            }

            chatRoom.sendTo(new Chat("SPECIAL", "MUTE"), psychologist);

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

        chatRoom.sendToAll(new Chat("SPECIAL", "Psychologist Sleep."));
        return candidate;
    }

    /**
     * this method starts the armored role in the night
     * @return request of inquiry
     */
    private boolean armoredRole() 
    {
        chatRoom.sendToAll(new Chat("SPECIAL", "Armored Wake Up."));

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
            ArrayList<Integer> isTimed = new ArrayList<>();
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            while(isTimed.size() == 0)
            {
                try 
                {
                    Thread.sleep(1000);
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
            chatRoom.sendToAll(new Chat("SPECIAL", "Armored Sleep."));
            return false;
        }
        else
        {
            chatRoom.sendTo(new Chat("SPECIAL", "Armored If Your Want To Have Inquiry Tomorrow Just Send 1\nSend Your Vote At Least 2 Times."), armored);
            
            chatRoom.sendTo(new Chat("SPECIAL", "VOTE"), armored);
            chatRoom.sendTo(new Chat("SPECIAL", "UNMUTE"), armored);

            ArrayList<Integer> isTimed = new ArrayList<>();
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            Chat c = null;

            while(isTimed.size() == 0)
            {
                if(!chatQueue.isEmpty())
                {
                    c = chatQueue.popFrontChat();
                }
            }

            chatRoom.sendTo(new Chat("SPECIAL", "MUTE"), armored);

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
                chatRoom.sendToAll(new Chat("SPECIAL", "Armored Sleep."));
                return false;
            }
            else
            {
                if(Integer.parseInt(c.getText()) == 1)
                {
                    ((Armored)armored.getRole()).setInquiryCount(((Armored)armored.getRole()).getInquiryCount() + 1);
                    chatRoom.sendToAll(new Chat("SPECIAL", "Armored Sleep."));
                    return true;
                }
                else
                {
                    chatRoom.sendToAll(new Chat("SPECIAL", "Armored Sleep."));
                    return false;
                }
            }
        }
    }

    /**
     * this method starts the mayor role in the voting
     * @return request to cancel the voting
     */
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
            ArrayList<Integer> isTimed = new ArrayList<>();
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            while(isTimed.size() == 0)
            {
                try 
                {
                    Thread.sleep(1000);
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }   

            return false;
        }
        else
        {
            chatRoom.sendTo(new Chat("SPECIAL", "Mayor If Your Want To Cancel This Voting Just Send 1\nSend Your Vote At Least 2 Times."), mayor);
            
            chatRoom.sendTo(new Chat("SPECIAL", "VOTE"), mayor);
            chatRoom.sendTo(new Chat("SPECIAL", "UNMUTE"), mayor);

            ArrayList<Integer> isTimed = new ArrayList<>();
            Thread timer = new Thread(new Timer(isTimed, 20000));
            timer.start();

            Chat c = null;

            while(isTimed.size() == 0)
            {
                if(!chatQueue.isEmpty())
                {
                    c = chatQueue.popFrontChat();
                }
            }

            chatRoom.sendTo(new Chat("SPECIAL", "MUTE"), mayor);

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

    /**
     * @return list of alive players in string
     */
    private String alivePlayersToString()
    {
        String s = "\n";
        int i = 1;
        for(Player player : alivePlayers)
        {
            s += ANSI_PURPLE + i + ANSI_RESET + " : " + ANSI_BLUE + player.getUserName() + ANSI_RESET + "\n";
            i++;
        }
        return s;
    }

    /**
     * @return list of alive citizens in string
     */
    private String aliveCitizenToString()
    {
        String s = "\n";
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

    /**
     * @return lisr of alive mafias in string
     */
    public String aliveMafiaToString()
    {
        String s = "\n";
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

    /**
     * @return list of voters and who vote in string
     */
    private String votersToString()
    {
        String s = "\n";
        for(Player voter : votes.keySet())
        {
            s += ANSI_YELLOW + voter.getUserName() + ANSI_GREEN + " -> " + ANSI_PURPLE + votes.get(voter) + ANSI_RESET + "\n";
        }
        return s;
    }

    /**
     * @return list of players number of votes in string
     */
    private String votesToString()
    {
        String s = "\n";
        int[] a = new int[alivePlayersCount];

        for(int i = 0; i < alivePlayersCount; i++)
        {
            a[i] = 0;
        }

        for(Player voter : votes.keySet())
        {
            a[alivePlayers.indexOf(votes.get(voter))]++;
        }

        for(int i = 0; i < alivePlayersCount; i++)
        {
            s += ANSI_RED + alivePlayers.get(i).getUserName() + ANSI_GREEN + " : " + ANSI_PURPLE + a[i] + ANSI_RESET + "\n";
        }

        return s;
    }

    /**
     * @return the player who is candidate to kill in voting state
     */
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

    /**
     * checks if the game must be finished
     */
    private void checkFinishConditions()
    {
        if(aliveMafiaCount == 0)
        {   
            chatRoom.sendToAll(new Chat("SPECIAL", "Congrats To All Citizens \n  Citizen Won The Game."));
            chatRoom.sendToAll(new Chat("SPECIAL", "END"));
            try 
            {
                Thread.sleep(10000);
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
            System.exit(0);
        }
        else if(aliveMafiaCount >= (alivePlayersCount - aliveMafiaCount))
        {
            chatRoom.sendToAll(new Chat("SPECIAL", "Congrats To All Mafias \n  Mafia Won The Game."));
            chatRoom.sendToAll(new Chat("SPECIAL", "END"));
            try 
            {
                Thread.sleep(10000);
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
            System.exit(0);
        }
    }
    
    /**
     * set the roles of players in random way
     */
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
    
    /**
     * send the roles of players to the clients
     */
    private void sendRoles()
    {
        for(Player player : players)
        {
            System.out.println(player.getUserName() + " Role Is : " + player.getRole().toString());
            chatRoom.sendTo(new Chat("SPECIAL",ANSI_YELLOW + player.getUserName() + ANSI_BLUE + " Your Role Is : " + player.getRole().toString()), player);
        }
    }
    
    /**
     * introduce mafias to each other
     */
    private void mafiasIntroduction()
    {
        String text = "Mafias : \n";
        for(Player mafia : mafias)
        {
            if(mafia.getRole() instanceof GodFather)
            {
                text += (ANSI_YELLOW + mafia.getUserName() + ANSI_BLUE + " Is GodFather \n"); 
            }
            else if(mafia.getRole() instanceof DoctorLecter)
            {
                text += (ANSI_YELLOW + mafia.getUserName() + ANSI_BLUE + " Is Doctor Lecter \n");
            }
            else
            {
                text += (ANSI_YELLOW + mafia.getUserName() + ANSI_BLUE + " Is Regular Mafia \n");
            }
        }
        chatRoom.sendTo(new Chat("SPECIAL", text), mafias);
    }
    
    /**
     * introduce dotor to the mayor
     */
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
                        chatRoom.sendTo(new Chat("SPECIAL", ANSI_YELLOW + doctor.getUserName() + ANSI_BLUE + " Is Doctor"), mayor);
                    }
                }
            }
        }
    }

    /**
     * changes the head of mafias 
     */
    public void changeHeadOfMafia()
    {
        ArrayList<Player> candidateMafias = new ArrayList<>();
        for(Player p : mafias)
        {
            if((p.getIsAlive() == true) && !(p.getUserName().equals(headOfMafia.getUserName())))
            {
                candidateMafias.add(p);
            }
        }
        if(candidateMafias.size() == 0)
        {
            return;
        }
        Collections.shuffle(candidateMafias);
        headOfMafia = candidateMafias.get(0);
        chatRoom.sendTo(new Chat("SPECIAL", ANSI_YELLOW + headOfMafia.getUserName() + ANSI_BLUE + " Is New Head Of Mafia."), mafias);
    }

    /**
     * @return list of players
     */
    public ArrayList<Player> getPlayers()
    {
        return players;
    }

    /**
     * @return chat queue
     */
    public ChatQueue getChatQueue()
    {
        return chatQueue;
    }

    /**
     * @return list of alive players
     */
    public ArrayList<Player> getAlivePlayers()
    {
        return alivePlayers;
    }

    /**
     * sets the number of alive players
     * @param k
     */
    public void setAlivePlayersCount(int k)
    {
        alivePlayersCount = k;
    }

    /**
     * @return the number of alive players
     */
    public int getAlivePlayersCount()
    {
        return alivePlayersCount;
    }

    /**
     * sets the number of alive mafias
     * @param k
     */
    public void setAliveMafiaCount(int k)
    {
        aliveMafiaCount = k;
    }

    /**
     * @return the number of alive mafias
     */
    public int getAliveMafiaCount()
    {
        return aliveMafiaCount;
    } 

    /**
     * @return head of mafias
     */
    public Player getHeadOfMafia()
    {
        return headOfMafia;
    }

    /**
     * @return chat room of the game
     */
	public ChatRoom getChatRoom() 
    {
		return chatRoom;
	}

    /**
     * @return list of players with their state in string
     */
    public String deadOrAliveToString()
    {
        String s = "";
        for(Player p : players)
        {
            if(p.getIsAlive() == true)
            {
                s += ANSI_YELLOW + p.getUserName() + ANSI_PURPLE + " Is " + ANSI_GREEN + "Alive\n" + ANSI_RESET;
            }
            else
            {
                s += ANSI_YELLOW + p.getUserName() + ANSI_PURPLE + " Is " + ANSI_RED + "Dead\n" + ANSI_RESET;
            }
        }
        return s;
    }

    /**
     * saves chats in file
     * @param chat
     */
    public void saveChat(Chat chat)
    {
        chats.add(chat);
        try(PrintWriter out = new PrintWriter(new FileWriter("chat.txt")))
        {
            for(Chat c : chats)
            {
                out.println(c.fileToString());
            }
        } 
        catch (Exception e) 
        {
            System.out.println("error in writing chats.");
        }
    }

    /**
     * @return force muted player
     */
    public Player getForceMute()
    {
        return forceMute;
    }
}
