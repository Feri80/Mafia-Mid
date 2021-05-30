package model.roles;

import model.logic.God;

public class Mafia extends Role
{
    

    public Mafia()
    {

    }

    @Override
    public String toString() 
    {
        return God.ANSI_YELLOW + "Mafia" + God.ANSI_RESET;
    }
}
