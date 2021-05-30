package model.roles;

import model.logic.God;

public class DoctorLecter extends Mafia 
{


    public DoctorLecter() 
    {
        
    }

    @Override
    public String toString() 
    {
        return God.ANSI_YELLOW + "Doctor Lecter" + God.ANSI_RESET;    
    }
}