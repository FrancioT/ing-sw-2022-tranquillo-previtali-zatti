package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Exceptions.EmptyException;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.Card3Actuator;

public class CharacterCard3 extends CharacterCard {

    public CharacterCard3() {
        super(3, 3);
    }

    @Override
    public void handle(String uID, DataBuffer userData, Controller controller) throws Exception
    {

        if(userData==null || uID==null || controller==null)
            throw new NullPointerException();
        if(!controller.getModel().checkEnoughMoney(uID, cardID))
            throw new NotEnoughMoneyException();

        int index=-1;
        while(index==-1)
        {
            try {
                index = userData.getIslandPos();
            } catch (EmptyException e) {
                try {
                    userData.wait();
                } catch (InterruptedException ignored) {}
            }
        }
        Card3Actuator.card3Effect(index, controller.getModel());

        controller.getModel().payCard(uID, cardID);
        overPrice++;
    }

}
