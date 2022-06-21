package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.Card9Decorator;

public class CharacterCard9 extends CharacterCard
{
    static final long serialVersionUID= 80307L;

    /**
     * Constructor of the card
     */
    public CharacterCard9()
    {
        super(9, 3);
        this.effect="Choose a color of Student: during the influence " +
                    "calculation this turn, that color adds no influence.";
    }

    /**
     * This method is implemented by the superclass, but does nothing
     */
    @Override
    public void initialize(){}

    /**
     * @param userData   the databuffer with the colour of the students that the player wants to "inhibit" for the
     *                   calculation of the dominance for this turn
     */
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

    @Override
    public void ccPrinter()
    {
        super.ccPrinter();
        System.out.println("Effect: "+effect);
        System.out.println("Parameters: Student's colour");
    }
}
