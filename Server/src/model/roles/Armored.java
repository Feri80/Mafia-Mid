package model.roles;

import model.logic.God;

public class Armored extends Citizen
{


    public Armored() 
    {
        
    }
    
    @Override
    public String toString() 
    {
        return God.ANSI_YELLOW + "Aromored" + God.ANSI_RESET;    
    }
}
