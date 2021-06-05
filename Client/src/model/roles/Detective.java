package model.roles;

import model.logic.ClientHandler;

public class Detective extends Citizen
{


    public Detective() 
    {

    }

    @Override
    public String toString() 
    {
        return ClientHandler.ANSI_YELLOW + "Detective" + ClientHandler.ANSI_RESET;
    }
}
