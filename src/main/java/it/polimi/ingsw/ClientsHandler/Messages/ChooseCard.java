package it.polimi.ingsw.ClientsHandler.Messages;

import it.polimi.ingsw.Controller.DataBuffer;

public class ChooseCard extends Message
{
    private final int cardPos;
    ChooseCard(String uID, int cardPos)
    {
        super(uID);
        if(cardPos<0)
            throw new IndexOutOfBoundsException();
        this.cardPos=cardPos;
    }
    @Override
    protected void handle(DataBuffer dataBuffer)
    {
        super.checkDataBuffer(dataBuffer);
        dataBuffer.setCardPos(cardPos);
    }
}
