package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.Card8Decorator;

public class CharacterCard8 extends CharacterCard
{
    static final long serialVersionUID= 80306L;

    /**
     * Constructor of the card
     */
    public CharacterCard8() { super(8, 2); }

    /**
     * @param userData   in this case is null as this card doesn't need any data from the databuffer
     */
    @Override
    public void handle(String uID, DataBuffer userData, Controller controller) throws Exception
    {
        if(uID==null || controller==null) throw new NullPointerException();
        if(!controller.getModel().checkEnoughMoney(uID, cardID))
            throw new NotEnoughMoneyException();

        Card8Decorator model2= new Card8Decorator(controller.getModel(), uID);
        controller.decorateModel(model2);

        controller.getModel().payCard(uID, cardID);
        overPrice=true;
    }

    @Override
    public void ccPrinter()
    {
        super.ccPrinter();
        System.out.println("Effect: During the influence calculation this turn, you " +
                "count as having 2 more influence.");
        System.out.println("No parameters");
    }
}
