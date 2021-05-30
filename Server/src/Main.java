import java.util.Scanner;

import model.logic.*;

public class Main 
{
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

    public static void main(String[] args) 
    {
        God god = new God(getPlayersCount(), getPort());
        god.startGame();
    }
}