package model.roles;

import model.logic.ClientHandler;

public class GodFather extends Mafia
{


    public GodFather()
    {

    }

    @Override
    public String toString() 
    {
        return ClientHandler.ANSI_YELLOW + "GodFather" + ClientHandler.ANSI_RESET;    
    }
}
