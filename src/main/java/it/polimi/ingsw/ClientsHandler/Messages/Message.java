package it.polimi.ingsw.ClientsHandler.Messages;

import it.polimi.ingsw.Controller.DataBuffer;

import java.io.Serializable;

public abstract class Message implements Serializable
{
    private final String uID;
    static final long serialVersionUID= 30000L;

    protected Message(String uID) { this.uID=uID; }
    public abstract void handle(DataBuffer dataBuffer);
    protected void checkDataBuffer(DataBuffer dataBuffer)
    {
        if(dataBuffer==null)
            throw new NullPointerException();
        if(!dataBuffer.getUID().equals(uID))
            throw new IllegalArgumentException();
    }
}
