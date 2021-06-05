package model.roles;

import model.logic.ClientHandler;

public class Psychologist extends Citizen
{


    public Psychologist() 
    {
        
    }

    @Override
    public String toString() 
    {
        return ClientHandler.ANSI_YELLOW + "Psychologist" + ClientHandler.ANSI_RESET;
    }
}
