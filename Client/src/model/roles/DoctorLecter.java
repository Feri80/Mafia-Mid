package model.roles;

import model.logic.ClientHandler;

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
        return ClientHandler.ANSI_YELLOW + "Doctor Lecter" + ClientHandler.ANSI_RESET;    
    }
}