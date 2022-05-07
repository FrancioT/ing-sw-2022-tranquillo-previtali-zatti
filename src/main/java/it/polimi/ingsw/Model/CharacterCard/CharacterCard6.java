package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.Card6Decorator;

public class CharacterCard6 extends CharacterCard
{
    static final long serialVersionUID= 80305L;

    public CharacterCard6() { super(6, 3); }
    @Override
    public void handle(String uID, DataBuffer userData, Controller controller) throws Exception
    {
        if(uID==null || controller==null) throw new NullPointerException();
        if(!controller.getModel().checkEnoughMoney(uID, cardID))
            throw new NotEnoughMoneyException();

        Card6Decorator model2= new Card6Decorator(controller.getModel());
        controller.decorateModel(model2);

        controller.getModel().payCard(uID, cardID);
        overPrice=true;
    }
}
