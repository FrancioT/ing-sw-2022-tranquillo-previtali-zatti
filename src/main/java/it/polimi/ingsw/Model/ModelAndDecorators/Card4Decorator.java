package it.polimi.ingsw.Model.ModelAndDecorators;

import it.polimi.ingsw.Model.CharacterCard.CharacterCard;
import it.polimi.ingsw.Model.Exceptions.EmptyException;
import it.polimi.ingsw.Model.Exceptions.NoSuchPlayerException;
import it.polimi.ingsw.Model.Player;

public class Card4Decorator extends Model
{
    /**
     * Constructor of the model decorator
     * @param model the model on which the decorator is based
     */
    public Card4Decorator(Model model){ super(model); }

    /**
     * This method gets the MN value of the last card played by the player and increases its value by 2
     * @return the last discarded card's mother nature value increased by 2
     * @throws NoSuchPlayerException thrown if the nickname passed doesn't correspond to any player's nickname
     * @throws EmptyException thrown if the player didn't discard any card
     */
    @Override
    public synchronized int getLastCardValue(String uID) throws NoSuchPlayerException, EmptyException
    {
        return super.getLastCardValue(uID) + 2;
    }
}
