package model.logic;

public class Timer implements Runnable 
{
    private Boolean isTimed;

    private int time;

    public Timer(Boolean isTimed, int time)
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
                Thread.sleep(1000);
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
            time -= 1000;
        }
        isTimed = true;
    }
    
}
