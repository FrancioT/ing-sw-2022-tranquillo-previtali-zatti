package it.polimi.ingsw.ClientsHandler;

import java.io.IOException;

public interface PingWaiter
{
    boolean getPing();
    void close() throws IOException;
}
