package model.roles;

import model.logic.ClientHandler;

public class Citizen extends Role
{


    public Citizen()
    {

    }

    @Override
    public String toString() 
    {
        return ClientHandler.ANSI_YELLOW + "Regular Citizen" + ClientHandler.ANSI_RESET;
    }
}
