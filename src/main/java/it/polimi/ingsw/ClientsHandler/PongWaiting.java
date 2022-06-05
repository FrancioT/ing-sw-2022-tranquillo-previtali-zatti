package it.polimi.ingsw.ClientsHandler;

import java.io.IOException;

public class PongWaiting extends Thread
{
    private static final int maxPingMissed= 3;
    private PingWaiter receiver;

    public PongWaiting(PingWaiter receiver)
    {
        this.receiver= receiver;
    }

    /**
     * Every 10 seconds calls getPing and expects a pong, if 3 pongs are missed the game ends
     */
    @Override
    public void run()
    {
        try {
            for(int i=0; i<maxPingMissed; i++)
            {
                sleep(10000);
                if(receiver.getPing())
                    i=-1;
            }
            try{ receiver.close(); }catch(IOException ignored){}
        }catch(InterruptedException ignored) { return; }
    }
}
