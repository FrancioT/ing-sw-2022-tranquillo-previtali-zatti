package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Model.ModelAndDecorators.Card6Decorator;

public class CharacterCard6 extends CharacterCard
{
    public CharacterCard6() { super(6, 3); }
    @Override
    public void handle(String uID, Object choice, Controller controller) throws Exception
    {
        if(uID==null) throw new NullPointerException();

        controller.getModel().payCard(uID, cardID);
        overPrice++;
        Card6Decorator model2= new Card6Decorator(controller.getModel());
        controller.decorateModel(model2);
    }
}
