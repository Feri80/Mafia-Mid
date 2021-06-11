package model.roles;

import model.logic.God;

/**
 * This class contains the role of mayor
 * 
 * @author Fahrad Aman 
 * @version 1.0
 */
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
