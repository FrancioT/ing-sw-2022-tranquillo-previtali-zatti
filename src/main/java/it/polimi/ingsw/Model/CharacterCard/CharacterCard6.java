package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.Card6Decorator;

public class CharacterCard6 extends CharacterCard
{
    public CharacterCard6() { super(6, 3); }
    @Override
    public void handle(String uID, Object choice, Controller controller) throws Exception
    {
        if(controller.getModel().checkEnoughMoney(uID, cardID))
        {
            if(uID==null) throw new NullPointerException();

            Card6Decorator model2= new Card6Decorator(controller.getModel());
            controller.decorateModel(model2);

            controller.getModel().payCard(uID, cardID);
            overPrice++;
        }
        else throw new NotEnoughMoneyException();
    }
}
