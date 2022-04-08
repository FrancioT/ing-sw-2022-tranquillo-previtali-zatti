package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Model.Exceptions.InhibitionFlagAlreadyActiveException;
import it.polimi.ingsw.Model.Exceptions.NoInhibitionFlagsAvailable;
import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;

public class CharacterCard5 extends CharacterCard{
    Model model;
    int AvailableFlags;

    public CharacterCard5(){
        super(5, 1);
        AvailableFlags = 4;
    }

    public void giveBackInhibitionFlag(){
        AvailableFlags ++;
    }

    @Override
    public void handle(String uID, Object choice, Controller controller) throws Exception {
        model = controller.getModel();
        
        if (!model.getInhibitionFlag((Island) choice) && AvailableFlags > 0){
            model.activateInhibitionFlag((Island) choice);
            AvailableFlags --;
            controller.getModel().payCard(uID, cardID);
            overPrice++;
        }

        else if (model.getInhibitionFlag((Island) choice))
            throw new InhibitionFlagAlreadyActiveException();

        else if (AvailableFlags <= 0)
            throw new NoInhibitionFlagsAvailable();
    }
}
