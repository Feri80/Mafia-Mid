package model.roles;

import model.logic.ClientHandler;

public class Sniper extends Citizen
{
    private int snipeCount;
    
    public Sniper() 
    {
        this.snipeCount = 0;
    }

    public int getSnipeCount()
    {
        return snipeCount;
    }

    public void setSnipeCount(int snipeCount)
    {
        this.snipeCount = snipeCount;
    }

    @Override
    public String toString() 
    {
        return ClientHandler.ANSI_YELLOW + "Sniper" + ClientHandler.ANSI_RESET;   
    }
}
