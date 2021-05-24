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
        channel = null;
    }

    public String getUserName()
    {
        return userName;
    }

    public boolean getIsAlive()
    {
        return isAlive;
    }

    public void setIsAlive(boolean isAlive)
    {
        this.isAlive = isAlive;
    }

    public Socket getChannel() 
    {
        return channel;
    }

    public void setChannel(Socket channel)
    {
        this.channel = channel;
    }
}