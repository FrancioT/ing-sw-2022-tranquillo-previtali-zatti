package it.polimi.ingsw.ClientsHandler.Messages;

import it.polimi.ingsw.Controller.DataBuffer;

public class ChooseCloud extends Message
{
    private final int cloudPos;
    static final long serialVersionUID= 30002L;

    public ChooseCloud(String uID, int cloudPos)
    {
        super(uID);
        if(cloudPos<0)
            throw new IndexOutOfBoundsException();
        this.cloudPos=cloudPos;
    }
    @Override
    public void handle(DataBuffer dataBuffer)
    {
        super.checkDataBuffer(dataBuffer);
        dataBuffer.setCloudPos(cloudPos);
    }
}
