package model.roles;

import model.logic.ClientHandler;

public class Mayor extends Citizen 
{


    public Mayor() 
    {

    }

    @Override
    public String toString() 
    {
        return ClientHandler.ANSI_YELLOW + "Mayor" + ClientHandler.ANSI_RESET;   
    }
}
