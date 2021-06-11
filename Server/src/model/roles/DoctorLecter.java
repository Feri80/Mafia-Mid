package model.roles;

import model.logic.God;

/**
 * This class contains the role of doctor lecter
 * 
 * @author Farhad Aman
 * @version 1.0
 */
public class DoctorLecter extends Mafia 
{
    /**
     * is doctor lecter used self healing
     */
    private boolean isSelfHealed;

    /**
     * creates a new doctor lecter
     */
    public DoctorLecter() 
    {
        isSelfHealed = false;
    }

    /**
     * @return is doctor lecter used self healing
     */
    public boolean getIsSelfHealed()
    {
        return isSelfHealed;
    }

    /**
     * sets the self healing of doctor lecter
     */
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