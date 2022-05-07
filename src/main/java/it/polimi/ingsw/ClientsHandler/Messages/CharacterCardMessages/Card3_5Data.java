package it.polimi.ingsw.ClientsHandler.Messages.CharacterCardMessages;

import it.polimi.ingsw.Controller.DataBuffer;

public class Card3_5Data extends CardMessage
{
    private final int islandPos;
    Card3_5Data(String uID, int cardID, int islandPos)
    {
        super(uID, cardID);
        if(islandPos<0)
            throw new IndexOutOfBoundsException();
        this.islandPos=islandPos;
    }
    @Override
    protected void handle(DataBuffer dataBuffer)
    {
        super.checkDataBuffer(dataBuffer);
        dataBuffer.setIslandPos(islandPos);
    }
}
