package model.roles;

import model.logic.God;

public class Detective extends Citizen
{


    public Detective() 
    {

    }

    @Override
    public String toString() 
    {
        return God.ANSI_YELLOW + "Detective" + God.ANSI_RESET;
    }
}
