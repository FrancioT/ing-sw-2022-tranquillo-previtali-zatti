package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.Card6Decorator;

public class CharacterCard6 extends CharacterCard
{
    static final long serialVersionUID= 80305L;

    /**
     * Constructor of the card
     */
    public CharacterCard6()
    {
        super(6, 3);
        this.effect="When resolving a Conquering on an Island, Towers do not count towards influence.";
    }

    /**
     * This method is implemented by the superclass, but does nothing
     */
    @Override
    public void initialize(){}

    /**
     * @param userData   in this case is null as this card doesn't need any data from the databuffer
     */
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

    @Override
    public void ccPrinter()
    {
        super.ccPrinter();
        System.out.println("Effect: "+effect);
        System.out.println("No parameters");
    }
}
