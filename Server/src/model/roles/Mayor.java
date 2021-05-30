package model.roles;

import model.logic.God;

public class Mayor extends Citizen 
{


    public Mayor() 
    {

    }

    @Override
    public String toString() 
    {
        return God.ANSI_YELLOW + "Mayor" + God.ANSI_RESET;   
    }
}
