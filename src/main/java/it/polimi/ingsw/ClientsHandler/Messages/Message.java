package it.polimi.ingsw.ClientsHandler.Messages;

import it.polimi.ingsw.Controller.DataBuffer;

import java.io.Serializable;

public abstract class Message implements Serializable
{
    private final String uID;
    protected Message(String uID) { this.uID=uID; }
    protected abstract void handle(DataBuffer dataBuffer);
    protected void checkDataBuffer(DataBuffer dataBuffer)
    {
        if(dataBuffer==null)
            throw new NullPointerException();
        if(!dataBuffer.getUID().equals(uID))
            throw new IllegalArgumentException();
    }
}
