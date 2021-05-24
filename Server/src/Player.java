import java.net.Socket;

public abstract class Player
{
    private String userName;

    private boolean isAlive;

    private Socket channel;

    public Player(String userName)
    {
        this.userName = userName;
        isAlive = true;
    }

    public String getUserName()
    {
        return userName;
    }

    public boolean getIsAlive()
    {
        return isAlive;
    }

    public Socket getChannel() 
    {
        return channel;
    }
}