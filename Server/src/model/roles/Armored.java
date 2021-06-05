package model.roles;

import model.logic.God;

public class Armored extends Citizen
{
    private int inquiryCount;

    private boolean isArmored;

    public Armored() 
    {
        this.inquiryCount = 0;
        isArmored = true;
    }
    
    public int getInquiryCount()
    {
        return inquiryCount;
    }

    public void setInquiryCount(int k)
    {
        inquiryCount = k;
    }

    public boolean getIsArmored()
    {
        return isArmored;
    }

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
