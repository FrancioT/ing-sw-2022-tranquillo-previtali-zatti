package it.polimi.ingsw.ClientsHandler;

import java.io.IOException;

public class PingSender extends Thread
{
    private ClientHandler clientHandler;
    PingSender(ClientHandler clientHandler)
    {
        this.clientHandler=clientHandler;
    }
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
