package model.roles;

import model.logic.God;

/**
 * This class contains the role of sniper
 * 
 * @author Farhad Aman
 * @version 1.0
 */
public class Sniper extends Citizen
{
    /**
     * the number of timers sniper shots
     */
    private int snipeCount;
    
    /**
     * creates a new sniper
     */
    public Sniper() 
    {
        this.snipeCount = 0;
    }

    /**
     * @return the number of times sniper shots
     */
    public int getSnipeCount()
    {
        return snipeCount;
    }

    /**
     * set the number of times sniper shots
     * @param snipeCount
     */
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
