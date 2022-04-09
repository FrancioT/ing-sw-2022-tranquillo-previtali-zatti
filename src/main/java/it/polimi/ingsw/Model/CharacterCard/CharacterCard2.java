package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.Card2Decorator;

public class CharacterCard2 extends CharacterCard
{
    public CharacterCard2() { super(2, 2); }
    @Override
    public void handle(String uID, Object choice, Controller controller) throws Exception
    {
        if(controller.getModel().checkEnoughMoney(uID, cardID))
        {
            if(uID==null) throw new NullPointerException();

            Card2Decorator model2= new Card2Decorator(controller.getModel());
            controller.decorateModel(model2);
            model2.moveTeachers(uID);

            controller.getModel().payCard(uID, cardID);
            overPrice++;
        }
        else throw new NotEnoughMoneyException();
    }
}
