import java.util.Scanner;

public class Main 
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
        god.game();
    }
}