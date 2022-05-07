package it.polimi.ingsw.ClientsHandler.Messages;

import it.polimi.ingsw.Controller.DataBuffer;

public class MoveMN extends Message
{
    private final int mnPos;
    MoveMN(String uID, int mnPos)
    {
        super(uID);
        if(mnPos<0)
            throw new IndexOutOfBoundsException();
        this.mnPos=mnPos;
    }
    @Override
    protected void handle(DataBuffer dataBuffer)
    {
        super.checkDataBuffer(dataBuffer);
        dataBuffer.setMnPos(mnPos);
    }
}
