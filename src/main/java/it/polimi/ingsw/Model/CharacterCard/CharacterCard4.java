package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.Card4Decorator;

public class CharacterCard4 extends CharacterCard
{
    public CharacterCard4() { super(4, 1); }
    @Override
    public void handle(String uID, Object choice, Controller controller) throws Exception
    {
        if(controller.getModel().checkEnoughMoney(uID, cardID))
        {
            if(uID==null) throw new NullPointerException();

            Card4Decorator model2= new Card4Decorator(controller.getModel());
            controller.decorateModel(model2);

            controller.getModel().payCard(uID, cardID);
            overPrice++;
        }
        else throw new NotEnoughMoneyException();
    }
}
