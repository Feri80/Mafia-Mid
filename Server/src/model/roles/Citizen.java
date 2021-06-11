package model.roles;

import model.logic.God;

/**
 * This class contains the role of citizen
 * 
 * @author Farhad Aman
 * @version 1.0
 */
public class Citizen extends Role
{


    public Citizen()
    {

    }

    @Override
    public String toString() 
    {
        return God.ANSI_YELLOW + "Regular Citizen" + God.ANSI_RESET;
    }
}
