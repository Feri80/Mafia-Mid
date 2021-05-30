package model.roles;

import model.logic.God;

public class Psychologist extends Citizen
{


    public Psychologist() 
    {
        
    }

    @Override
    public String toString() 
    {
        return God.ANSI_YELLOW + "Psychologist" + God.ANSI_RESET;
    }
}
