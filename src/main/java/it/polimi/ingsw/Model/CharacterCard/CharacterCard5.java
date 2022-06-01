package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Exceptions.NoInhibitionFlagsAvailable;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;

public class CharacterCard5 extends CharacterCard
{
    private int availableFlags;
    static final long serialVersionUID= 80304L;

    /**
     * Constructor of the card which also initialize the number of inhibition tiles available to 4
     */
    public CharacterCard5(){
        super(5, 2);
        availableFlags = 4;
    }

    /**
     * Method used when an inhibition tile is removed from an island and needs to be put back on the card
     */
    public void giveBackInhibitionFlag(){
        if(availableFlags>3) throw new IllegalArgumentException("Too many inhibition tiles");
        availableFlags ++;
    }

    /**
     * @param userData   the databuffer with the index of the island where the player wants to put an inhibition tile
     */
    @Override
    public void handle(String uID, DataBuffer userData, Controller controller) throws Exception {
        if(userData==null || uID==null || controller==null)
            throw new NullPointerException();
        Model model = controller.getModel();
        if(!model.checkEnoughMoney(uID, cardID))
            throw new NotEnoughMoneyException();

        int index= userData.getIslandPos();
        if (availableFlags > 0){
            model.addInhibition(index, this);
            availableFlags --;

            model.payCard(uID, cardID);
            overPrice=true;
        }
        else throw new NoInhibitionFlagsAvailable();
    }

    @Override
    public void ccPrinter()
    {
        super.ccPrinter();
        System.out.println("Effect: Place a No Entry tile on an Island of your choice. " +
                "The first time Mother Nature ends her movement there,\n" +
                "put the No Entry tile back onto this card DO NOT " +
                "calculate influence on that Island, or place any Towers.");
        System.out.println("Parameters: Island's position");
    }
}