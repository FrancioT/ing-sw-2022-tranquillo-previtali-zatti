package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Exceptions.EmptyException;
import it.polimi.ingsw.Model.Exceptions.InhibitionFlagAlreadyActiveException;
import it.polimi.ingsw.Model.Exceptions.NoInhibitionFlagsAvailable;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;

public class CharacterCard5 extends CharacterCard{
    private int availableFlags;

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
        if (availableFlags > 0){
            model.activateInhibitionFlag(index, this);
            availableFlags --;

            model.payCard(uID, cardID);
            overPrice++;
        }
        else if (availableFlags <= 0)
            throw new NoInhibitionFlagsAvailable();
    }
}