package model.logic;

import java.util.ArrayList;

/**
 * This class simulates a timer for using in the program that doesnt stops the main thread
 * 
 * @author Farhad Aman
 * @version 1.0
 */
public class Timer implements Runnable 
{
    /**
     * this field will be true after the certain time
     */
    private ArrayList<Integer> isTimed;

    /**
     * the time we want time to work in miliseconds
     */
    private int time;

    /**
     * creates a new timer
     * @param isTimed
     * @param time
     */
    public Timer(ArrayList<Integer> isTimed, int time)
    {
        this.time = time;
        this.isTimed = isTimed;
    }

    @Override
    public void run()
    {    
        try 
        {
            Thread.sleep(time);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
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
