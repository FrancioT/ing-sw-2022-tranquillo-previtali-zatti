package it.polimi.ingsw.ClientsHandler.Messages;

import it.polimi.ingsw.Controller.DataBuffer;

public class MoveMN extends Message
{
    private final int mnPos;
    static final long serialVersionUID= 30003L;

    public MoveMN(String uID, int mnPos)
    {
        super(uID);
        if(mnPos<0)
            throw new IndexOutOfBoundsException();
        this.mnPos=mnPos;
    }
    @Override
    public void handle(DataBuffer dataBuffer)
    {
        super.checkDataBuffer(dataBuffer);
        dataBuffer.setMnPos(mnPos);
    }
}
