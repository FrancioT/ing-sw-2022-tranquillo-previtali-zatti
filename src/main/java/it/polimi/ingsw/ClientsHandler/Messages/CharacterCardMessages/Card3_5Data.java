package it.polimi.ingsw.ClientsHandler.Messages.CharacterCardMessages;

import it.polimi.ingsw.Controller.DataBuffer;

class Card3_5Data extends CardMessage
{
    private final int islandPos;
    static final long serialVersionUID= 30102L;

    Card3_5Data(String uID, int cardID, int islandPos)
    {
        super(uID, cardID);
        if(islandPos<0)
            throw new IndexOutOfBoundsException();
        this.islandPos=islandPos;
    }
    @Override
    public void handle(DataBuffer dataBuffer)
    {
        super.checkDataBuffer(dataBuffer);
        dataBuffer.setIslandPos(islandPos);
    }
}
