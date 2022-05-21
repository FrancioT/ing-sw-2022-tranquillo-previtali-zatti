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

    public CharacterCard5(){
        super(5, 2);
        availableFlags = 4;
    }

    public void giveBackInhibitionFlag(){
        if(availableFlags>3) throw new IllegalArgumentException("Too many inhibition tiles");
        availableFlags ++;
    }

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
                "The first time Mother Nature ends her movement " +
                "there, put the No Entry tile back onto this card DO NOT " +
                "calculate influence on that Island, or place any Towers.");
        System.out.println("Parameters: Island's position");
    }
}