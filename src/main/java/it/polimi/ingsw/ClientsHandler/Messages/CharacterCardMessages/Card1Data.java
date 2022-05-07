package it.polimi.ingsw.ClientsHandler.Messages.CharacterCardMessages;

import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Colour;

public class Card1Data extends CardMessage
{
    private final int islandPos;
    private final Colour studColour;
    Card1Data(String uID, int cardID, int islandPos, Colour studentColour)
    {
        super(uID, cardID);
        if(islandPos<0)
            throw new IndexOutOfBoundsException();
        if(studentColour==null)
            throw new NullPointerException();
        this.islandPos=islandPos;
        this.studColour=studentColour;
    }
    @Override
    protected void handle(DataBuffer dataBuffer)
    {
        super.checkDataBuffer(dataBuffer);
        synchronized (dataBuffer)
        {
            dataBuffer.setCharacterCardID(cardID);
            dataBuffer.setIslandPos(islandPos);
            dataBuffer.setStudColour(studColour);
        }
    }
}
