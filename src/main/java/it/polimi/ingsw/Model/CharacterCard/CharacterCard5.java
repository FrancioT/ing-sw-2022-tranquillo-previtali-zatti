package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Model.Exceptions.InhibitionFlagAlreadyActiveException;
import it.polimi.ingsw.Model.Exceptions.NoInhibitionFlagsAvailable;
import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;

public class CharacterCard5 extends CharacterCard{
    Model model;
    int availableFlags;

    public CharacterCard5(){
        super(5, 1);
        availableFlags = 4;
    }

    public void giveBackInhibitionFlag(){
        if(availableFlags>3) throw new IllegalArgumentException("Too many inhibition tiles");
        availableFlags ++;
    }

    @Override
    public void handle(String uID, Object choice, Controller controller) throws Exception {
        model = controller.getModel();

        if (!model.getInhibitionFlag((Island) choice) && availableFlags > 0){
            controller.getModel().payCard(uID, cardID);
            overPrice++;
            model.activateInhibitionFlag((Island) choice, this);
            availableFlags --;
        }
        else if (availableFlags <= 0)
                 throw new NoInhibitionFlagsAvailable();
             else
                 throw new InhibitionFlagAlreadyActiveException();
    }
}