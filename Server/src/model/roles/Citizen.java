package model.roles;

import model.logic.God;

public class Citizen extends Role
{


    public Citizen()
    {

    }

    @Override
    public String toString() 
    {
        return God.ANSI_YELLOW + "Regular Citizen" + God.ANSI_RESET;
    }
}
