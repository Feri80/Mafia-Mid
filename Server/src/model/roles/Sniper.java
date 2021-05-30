package model.roles;

import model.logic.God;

public class Sniper extends Citizen
{

    
    public Sniper() 
    {

    }

    @Override
    public String toString() 
    {
        return God.ANSI_YELLOW + "Sniper" + God.ANSI_RESET;   
    }
}
