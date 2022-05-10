package it.polimi.ingsw.ClientsHandler.Messages;

import it.polimi.ingsw.Controller.DataBuffer;

class ChooseCard extends Message
{
    private final int cardPos;
    static final long serialVersionUID= 30001L;

    ChooseCard(String uID, int cardPos)
    {
        super(uID);
        if(cardPos<0)
            throw new IndexOutOfBoundsException();
        this.cardPos=cardPos;
    }
    @Override
    public void handle(DataBuffer dataBuffer)
    {
        super.checkDataBuffer(dataBuffer);
        dataBuffer.setCardPos(cardPos);
    }
}
