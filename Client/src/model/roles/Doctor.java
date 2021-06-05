package model.roles;

import model.logic.ClientHandler;

public class Doctor extends Citizen
{
    private int selfHealCount;

    public Doctor() 
    {
        selfHealCount = 0;
    }

    @Override
    public String toString() 
    {
        return ClientHandler.ANSI_YELLOW + "Doctor" + ClientHandler.ANSI_RESET;    
    }

    public int getSelfHealCount()
    {
        return selfHealCount;
    }

    public void setSelfHealCount(int k)
    {
        selfHealCount = k;
    }
}
