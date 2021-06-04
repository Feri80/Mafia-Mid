package model.roles;

import model.logic.God;

public class Armored extends Citizen
{
    private int inquiryCount;

    public Armored() 
    {
        this.inquiryCount = 0;
    }
    
    public int getInquiryCount()
    {
        return inquiryCount;
    }

    public void setInquiryCount(int k)
    {
        inquiryCount = k;
    }

    @Override
    public String toString() 
    {
        return God.ANSI_YELLOW + "Aromored" + God.ANSI_RESET;    
    }
}
