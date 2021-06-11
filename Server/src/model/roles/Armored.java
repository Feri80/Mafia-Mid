package model.roles;

import model.logic.God;

/**
 * This class contains the role of armored
 */
public class Armored extends Citizen
{
    /**
     * the number of times that armored requests inquiry
     */
    private int inquiryCount;

    /**
     * is Armored has its armor
     */
    private boolean isArmored;

    /**
     * creates a new armored
     */
    public Armored() 
    {
        this.inquiryCount = 0;
        isArmored = true;
    }
    
    /**
     * @return inquiry count
     */
    public int getInquiryCount()
    {
        return inquiryCount;
    }

    /**
     * sets inquiry count
     * @param k
     */
    public void setInquiryCount(int k)
    {
        inquiryCount = k;
    }

    /**
     * @return is Armored has its armor
     */
    public boolean getIsArmored()
    {
        return isArmored;
    }

    /**
     * brokes the armor of the Armored
     */
    public void brokeArmor()
    {
        isArmored = false;
    }

    @Override
    public String toString() 
    {
        return God.ANSI_YELLOW + "Aromored" + God.ANSI_RESET;    
    }
}
