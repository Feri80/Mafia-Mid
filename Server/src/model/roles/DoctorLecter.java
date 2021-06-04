package model.roles;

import model.logic.God;

public class DoctorLecter extends Mafia 
{
    private boolean isSelfHealed;

    public DoctorLecter() 
    {
        isSelfHealed = false;
    }

    public boolean getIsSelfHealed()
    {
        return isSelfHealed;
    }

    public void setIsSelfHealed()
    {
        isSelfHealed = true;
    }

    @Override
    public String toString() 
    {
        return God.ANSI_YELLOW + "Doctor Lecter" + God.ANSI_RESET;    
    }
}