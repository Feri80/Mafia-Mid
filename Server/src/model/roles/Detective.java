package model.roles;

import model.logic.God;

/**
 * This class contains the role of the detective
 * 
 * @author Farhad Aman
 * @version 1.0
 */
public class Detective extends Citizen
{


    public Detective() 
    {

    }

    @Override
    public String toString() 
    {
        return God.ANSI_YELLOW + "Detective" + God.ANSI_RESET;
    }
}
