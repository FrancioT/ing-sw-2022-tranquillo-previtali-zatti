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
     * Method that sets the data needed for an action in the DataBuffer
     * @param dataBuffer the DataBuffer where the data received will be stored
     */
    public abstract void handle(DataBuffer dataBuffer);

    /**
     * Method that checks if the dataBuffer which the method handle is trying to fill, is coherent with the
     * uID in the message (must be called at the beginning of the handle method)
     * @param dataBuffer
     */
    protected void checkDataBuffer(DataBuffer dataBuffer)
    {
        if(dataBuffer==null)
            throw new NullPointerException();
        if(!dataBuffer.getUID().equals(uID))
            throw new IllegalArgumentException();
    }
}
