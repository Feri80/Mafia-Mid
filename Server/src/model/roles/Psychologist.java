package model.roles;

import model.logic.God;

/**
 * This class contains the role of psychologist
 * 
 * @author Farhad Aman
 * @version 1.0
 */
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
