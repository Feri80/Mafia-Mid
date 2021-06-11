package model.roles;

import model.logic.God;

/**
 * This class contains the role of god father
 * 
 * @author Farhad Aman
 * @version 1.0
 */
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
