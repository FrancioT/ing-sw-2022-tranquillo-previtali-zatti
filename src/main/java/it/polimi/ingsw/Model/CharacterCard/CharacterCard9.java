package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.ModelAndDecorators.Card9Decorator;

public class CharacterCard9 extends CharacterCard
{
    public CharacterCard9() { super(9, 3); }
    @Override
    public void handle(String uID, Object choice, Controller controller) throws Exception
    {
        if(uID==null || choice==null) throw new NullPointerException();

        controller.getModel().payCard(uID, cardID);
        overPrice++;
        Card9Decorator model2= new Card9Decorator(controller.getModel(), (Colour)choice);
        controller.decorateModel(model2);
    }
}
