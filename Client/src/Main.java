import java.util.Scanner;
import model.logic.ClientHandler;

/**
 * This class is the main class of the client that starts the client
 * 
 * @author Farhad Aman
 * @version 1.0
 */
public class Main 
{
    /**
     * gets the port of the server
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
     * the main method of the program
     * @param args
     */
    public static void main(String[] args) 
    {
        ClientHandler clientHandler = new ClientHandler(getPort());
        clientHandler.startGame();
    }
}
