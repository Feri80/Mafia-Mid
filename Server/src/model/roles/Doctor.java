package model.roles;

import model.logic.God;

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
        return God.ANSI_YELLOW + "Doctor" + God.ANSI_RESET;    
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
