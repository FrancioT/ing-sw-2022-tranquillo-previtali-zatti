package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Model.Exceptions.NoSuchPlayerException;
import it.polimi.ingsw.Model.ModelAndDecorators.Chard2Decorator;
import it.polimi.ingsw.Model.Player;

import java.util.List;

public class CharacterCard2 extends CharacterCard
{
    public CharacterCard2(int cardID, int price) { super(cardID, price); }
    @Override
    public void handle(String uID, Object choice, Controller controller) throws Exception
    {
        if(uID==null) throw new NullPointerException();

        controller.getModel().payCard(uID, cardID);
        overPrice++;
        Chard2Decorator model2= new Chard2Decorator(controller.getModel());
        controller.decorateModel(model2);
        model2.moveTeachers(uID);
    }
}
