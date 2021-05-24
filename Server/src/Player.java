public abstract class Player
{
    private String userName;

    private boolean isAlive;

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
}