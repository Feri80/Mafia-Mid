import java.util.Scanner;
import model.logic.ClientHandler;

public class Main 
{
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
        ClientHandler clientHandler = new ClientHandler(getPort());
        clientHandler.startGame();
    }
}
