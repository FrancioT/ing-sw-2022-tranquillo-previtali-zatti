package it.polimi.ingsw.ClientsHandler.Messages;

import it.polimi.ingsw.Controller.DataBuffer;

import java.io.Serializable;

public abstract class Message implements Serializable
{
    private final String uID;
    static final long serialVersionUID= 30000L;

    /**
     * Constructor of message which sets its owner to the client who sent it
     * @param uID the client's nickname who sent the message
     */
    protected Message(String uID) { this.uID=uID; }

    /**
     * Method that sets the datas needed for an action in the DataBuffer
     * @param dataBuffer the DataBuffer where the datas received will be stored
     */
    public abstract void handle(DataBuffer dataBuffer);
    protected void checkDataBuffer(DataBuffer dataBuffer)
    {
        if(dataBuffer==null)
            throw new NullPointerException();
        if(!dataBuffer.getUID().equals(uID))
            throw new IllegalArgumentException();
    }
}
