package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Model.ModelAndDecorators.Card8Decorator;

public class CharacterCard8 extends CharacterCard
{
    public CharacterCard8() { super(8, 2); }
    @Override
    public void handle(String uID, Object choice, Controller controller) throws Exception
    {
        if(uID==null) throw new NullPointerException();

        controller.getModel().payCard(uID, cardID);
        overPrice++;
        Card8Decorator model2= new Card8Decorator(controller.getModel(), uID);
        controller.decorateModel(model2);
    }
}
