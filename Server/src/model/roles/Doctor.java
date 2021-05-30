package model.roles;

import model.logic.God;

public class Doctor extends Citizen
{


    public Doctor() 
    {

    }

    @Override
    public String toString() 
    {
        return God.ANSI_YELLOW + "Doctor" + God.ANSI_RESET;    
    }
}
