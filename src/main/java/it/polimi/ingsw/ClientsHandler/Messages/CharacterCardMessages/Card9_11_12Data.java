package it.polimi.ingsw.ClientsHandler.Messages.CharacterCardMessages;

import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Colour;

public class Card9_11_12Data extends CardMessage
{
    private final Colour studColour;
    static final long serialVersionUID= 30104L;

    public Card9_11_12Data(String uID, int cardID, Colour studentColour)
    {
        super(uID, cardID);
        if(studentColour==null)
            throw new NullPointerException();
        this.studColour=studentColour;
    }
    @Override
    public void handle(DataBuffer dataBuffer)
    {
        super.checkDataBuffer(dataBuffer);
        synchronized (dataBuffer)
        {
            dataBuffer.setCharacterCardID(cardID);
            dataBuffer.setStudColour(studColour);
            dataBuffer.activationCardRequest();
        }
    }
}
