package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.Card3Actuator;

public class CharacterCard3 extends CharacterCard
{
    static final long serialVersionUID= 80302L;

    /**
     * Constructor of the card
     */
    public CharacterCard3()
    {
        super(3, 3);
        this.effect="Choose an Island and resolve the Island as if " +
                    "Mother Nature had ended her movement there. Mother " +
                    "Nature will still move and the Island where she ends " +
                    "her movement will also be resolved.";
    }

    /**
     * This method is implemented by the superclass, but does nothing
     */
    @Override
    public void initialize(){}

    /**
     * @param userData   the databuffer with the index of the island where the player wants to calculate the dominance
     */
    @Override
    public void handle(String uID, DataBuffer userData, Controller controller) throws Exception
    {

        if(userData==null || uID==null || controller==null)
            throw new NullPointerException();
        if(!controller.getModel().checkEnoughMoney(uID, cardID))
            throw new NotEnoughMoneyException();

        int index= userData.getIslandPos();
        synchronized (controller.getModel())
        {    Card3Actuator.card3Effect(index, controller.getModel());   }

        controller.getModel().payCard(uID, cardID);
        overPrice=true;
    }

    @Override
    public void ccPrinter()
    {
        super.ccPrinter();
        System.out.println("Effect: "+effect);
        System.out.println("Parameters: Island's position");
    }

}
