package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.Card9Decorator;

public class CharacterCard9 extends CharacterCard
{
    static final long serialVersionUID= 80307L;

    public CharacterCard9() { super(9, 3); }
    @Override
    public void handle(String uID, DataBuffer userData, Controller controller) throws Exception
    {
        if(uID==null || userData==null || controller==null) throw new NullPointerException();
        if(!controller.getModel().checkEnoughMoney(uID,cardID))
            throw new NotEnoughMoneyException();

        Colour colour= userData.getStudentColour();
        Card9Decorator model2= new Card9Decorator(controller.getModel(), colour);
        controller.decorateModel(model2);

        controller.getModel().payCard(uID, cardID);
        overPrice=true;
    }
}
