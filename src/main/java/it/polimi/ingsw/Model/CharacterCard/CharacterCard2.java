package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.Card2Decorator;

public class CharacterCard2 extends CharacterCard
{
    static final long serialVersionUID= 80301L;

    /**
     * Constructor of the card
     */
    public CharacterCard2() { super(2, 2); }

    /**
     * @param userData   in this case is null as this card doesn't need any data from the databuffer
     */
    @Override
    public void handle(String uID, DataBuffer userData, Controller controller) throws Exception
    {
        if(uID==null || controller==null) throw new NullPointerException();
        if(!controller.getModel().checkEnoughMoney(uID, cardID))
            throw new NotEnoughMoneyException();

        Card2Decorator model2= new Card2Decorator(controller.getModel());
        controller.decorateModel(model2);
        model2.moveTeachers(uID);

        controller.getModel().payCard(uID, cardID);
        overPrice=true;
    }

    @Override
    public void ccPrinter()
    {
        super.ccPrinter();
        System.out.println("Effect: During this turn, you take control of any number of Professors " +
                           "even if you have the same number of Students as the player who currently " +
                           "controls them.");
        System.out.println("No parameters");
    }
}
