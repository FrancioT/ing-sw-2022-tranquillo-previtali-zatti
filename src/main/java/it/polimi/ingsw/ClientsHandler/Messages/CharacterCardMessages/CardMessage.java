package it.polimi.ingsw.ClientsHandler.Messages.CharacterCardMessages;

import it.polimi.ingsw.ClientsHandler.Messages.Message;

import java.io.Serializable;

abstract public class CardMessage extends Message implements Serializable
{
    final int cardID;
    CardMessage(String uID, int cardID)
    {
        super(uID);
        if(cardID<0)
            throw new IllegalArgumentException();
        this.cardID=cardID;
    }
}
