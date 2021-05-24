public class Chat 
{
    private Player sender;

    private String text;

    public Chat(Player sender, String text)
    {
        this.sender = sender;
        this.text = text;
    }

    @Override
    public String toString() 
    {
        return Main.ANSI_PURPLE + sender.getUserName() + " : " + Main.ANSI_BLUE + text + Main.ANSI_RESET;   
    }
}
