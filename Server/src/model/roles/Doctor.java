package model.roles;

import model.logic.God;

/**
 * This class contains the role of doctor
 * 
 * @author Farhad Aman
 * @version 1.0
 */
public class Doctor extends Citizen
{
    /**
     * the number of times doctor used self healing
     */
    private int selfHealCount;

    /**
     * creates a new doctor
     */
    public Doctor() 
    {
        selfHealCount = 0;
    }

    @Override
    public String toString() 
    {
        return God.ANSI_YELLOW + "Doctor" + God.ANSI_RESET;    
    }

    /**
     * @return the number of times doctor used self healing
     */
    public int getSelfHealCount()
    {
        return selfHealCount;
    }

    /**
     * sets the number of times doctor used self healing
     * @param k
     */
    public void setSelfHealCount(int k)
    {
        selfHealCount = k;
    }
}
