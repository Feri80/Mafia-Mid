package model.logic;

import java.util.ArrayList;

public class Timer implements Runnable 
{
    private ArrayList<Integer> isTimed;

    private int time;

    public Timer(ArrayList<Integer> isTimed, int time)
    {
        this.time = time;
        this.isTimed = isTimed;
    }

    @Override
    public void run()
    {
        while(time > 0)
        {
            try 
            {
                Thread.sleep(time);
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
        }
        isTimed.add(1);
        System.out.println("timer closed");
        try 
        {
            Thread.sleep(1000);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
    }
    
}
