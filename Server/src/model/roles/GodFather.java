package model.roles;

import model.logic.God;

public class GodFather extends Mafia
{


    public GodFather()
    {

    }

    @Override
    public String toString() 
    {
        return God.ANSI_YELLOW + "GodFather" + God.ANSI_RESET;    
    }
}
