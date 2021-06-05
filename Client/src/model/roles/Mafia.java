package model.roles;

import model.logic.ClientHandler;

public class Mafia extends Role
{
    

    public Mafia()
    {

    }

    @Override
    public String toString() 
    {
        return ClientHandler.ANSI_YELLOW + "Mafia" + ClientHandler.ANSI_RESET;
    }
}
