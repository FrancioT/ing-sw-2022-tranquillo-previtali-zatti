package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Model.ModelAndDecorators.Card4Decorator;

public class CharacterCard4 extends CharacterCard
{
    public CharacterCard4() { super(4, 1); }
    @Override
    public void handle(String uID, Object choice, Controller controller) throws Exception
    {
        if(uID==null) throw new NullPointerException();

        controller.getModel().payCard(uID, cardID);
        overPrice++;
        Card4Decorator model2= new Card4Decorator(controller.getModel());
        controller.decorateModel(model2);
    }
}
