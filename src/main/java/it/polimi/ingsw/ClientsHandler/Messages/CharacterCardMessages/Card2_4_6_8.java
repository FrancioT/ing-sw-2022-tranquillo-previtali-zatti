package it.polimi.ingsw.ClientsHandler.Messages.CharacterCardMessages;

import it.polimi.ingsw.Controller.DataBuffer;

public class Card2_4_6_8 extends CardMessage
{
    static final long serialVersionUID= 30105L;
    public Card2_4_6_8(String uID, int cardID)
    {
        super(uID, cardID);
    }
    @Override
    public void handle(DataBuffer dataBuffer)
    {
        super.checkDataBuffer(dataBuffer);
        synchronized (dataBuffer)
        {
            dataBuffer.setCharacterCardID(cardID);
            dataBuffer.activationCardRequest();
        }
    }
}
