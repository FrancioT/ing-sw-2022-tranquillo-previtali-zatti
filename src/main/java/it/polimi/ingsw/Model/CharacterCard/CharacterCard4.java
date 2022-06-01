package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.Card4Decorator;

public class CharacterCard4 extends CharacterCard
{
    static final long serialVersionUID= 80303L;

    /**
     * Constructor of the card
     */
    public CharacterCard4() { super(4, 1); }

    /**
     * @param userData   in this case is null as this card doesn't need any data from the databuffer
     */
    @Override
    public void handle(String uID, DataBuffer userData, Controller controller) throws Exception
    {
        if(uID==null || controller==null) throw new NullPointerException();
        if(!controller.getModel().checkEnoughMoney(uID, cardID))
            throw new NotEnoughMoneyException();

        Card4Decorator model2= new Card4Decorator(controller.getModel());
        controller.decorateModel(model2);

        controller.getModel().payCard(uID, cardID);
        overPrice=true;
    }

    @Override
    public void ccPrinter()
    {
        super.ccPrinter();
        System.out.println("Effect: You may move " +
                "Mother Nature up to " +
                "2 " +
                "additional Islands than is indicated by the Assistant " +
                "card you've played.");
        System.out.println("No parameters");
    }
}
