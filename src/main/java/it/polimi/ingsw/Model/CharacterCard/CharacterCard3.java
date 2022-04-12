package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.Card3Actuator;

public class CharacterCard3 extends CharacterCard {

    public CharacterCard3() {
        super(3, 3);
    }

    @Override
    public void handle(String uID, Object choice, Controller controller) throws Exception
    {

        if(choice==null || uID==null || controller==null)
            throw new NullPointerException();
        if(!controller.getModel().checkEnoughMoney(uID, cardID))
            throw new NotEnoughMoneyException();

        Card3Actuator.card3Effect((int)choice, controller.getModel());

        controller.getModel().payCard(uID, cardID);
        overPrice++;
    }

}
