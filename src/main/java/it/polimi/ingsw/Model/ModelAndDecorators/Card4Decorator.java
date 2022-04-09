package it.polimi.ingsw.Model.ModelAndDecorators;

import it.polimi.ingsw.Model.CharacterCard.CharacterCard;
import it.polimi.ingsw.Model.Exceptions.EmptyException;
import it.polimi.ingsw.Model.Exceptions.NoSuchPlayerException;
import it.polimi.ingsw.Model.Player;

public class Card4Decorator extends Model
{
    public Card4Decorator(Model model){ super(model); }

    @Override
    public synchronized int getLastCardValue(String uID) throws NoSuchPlayerException, EmptyException
    {
        return super.getLastCardValue(uID) + 2;
    }
}
