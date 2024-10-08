package it.polimi.ingsw.ClientsHandler.Messages.CharacterCardMessages;

import it.polimi.ingsw.ClientsHandler.Messages.Message;

public abstract class CardMessage extends Message
{
    final int cardID;
    static final long serialVersionUID= 30100L;

    /**
     * Sets uID and cardID
     * @param uID the player who activated the card
     * @param cardID the card activated
     */
    public CardMessage(String uID, int cardID)
    {
        super(uID);
        if(cardID<0)
            throw new IllegalArgumentException();
        this.cardID=cardID;
    }
}
