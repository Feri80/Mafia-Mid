package model.roles;

import model.logic.God;

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
        return God.ANSI_YELLOW + "Sniper" + God.ANSI_RESET;   
    }
}
