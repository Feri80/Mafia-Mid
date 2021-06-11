import java.util.Scanner;

import model.logic.*;

/**
 * This class is the main class of the server that runs a new server
 * 
 * @author Farhad Aman
 * @version 1.0
 */
public class Main 
{
    /**
     * this method gets the number of players of the game that is at least 8
     * @return the number of players 
     */
    public static int getPlayersCount()
    {
        int playersCount = 0;
        while(playersCount < 8 || playersCount > 100)
        {
            System.out.print("Please Enter The Number Of Players (8 - 100):");
            Scanner input = new Scanner(System.in);
            try 
            {
                playersCount = input.nextInt();    
            } 
            catch(Exception e) 
            {
                System.out.println("Please Enter A Valid Number");
            }
            
        }
        return playersCount;
    }   

    /**
     * this method gets the port of the server
     * @return port
     */
    public static int getPort()
    {
        int port = 0;
        while(port <= 5000 || port >= 10000)
        {
            System.out.print("Please Enter Server Port :");
            Scanner input = new Scanner(System.in);
            try 
            {
                port = input.nextInt();    
            } 
            catch(Exception e) 
            {
                System.out.println("Please Enter A Valid Number");
            }
        }
        return port;
    }

    /**
     * this method is the main method of the program
     * @param args
     */
    public static void main(String[] args) 
    {
        God god = new God(getPlayersCount(), getPort());
        god.startGame();
    }
}