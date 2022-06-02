package it.polimi.ingsw.ClientsHandler;

import java.io.IOException;

public class PingSender extends Thread
{
    private ClientHandler clientHandler;
    PingSender(ClientHandler clientHandler)
    {
        this.clientHandler=clientHandler;
    }

    /**
     * Every 3 seconds calls the ping method in ClientHandler which sends a ping to the client
     */
    @Override
    public void run()
    {
        try {
            while(true)
            {
                sleep(3000);
                clientHandler.ping();
            }
        }catch (InterruptedException e0) { return; }
        catch (IOException e1) { try{ clientHandler.close(); }catch(IOException ignored){} }
    }
}
