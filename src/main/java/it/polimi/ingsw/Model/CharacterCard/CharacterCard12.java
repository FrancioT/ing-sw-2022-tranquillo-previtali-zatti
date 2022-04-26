package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;

public class CharacterCard12 extends CharacterCard
{
    final private Bag bag;

    public CharacterCard12(Bag bag){
        super(12 ,3);
        this.bag = bag;
    }

    @Override
    public void handle(String uID, DataBuffer userData, Controller controller) throws Exception {
        if(userData==null || uID==null || controller==null)
            throw new NullPointerException();
        Model model = controller.getModel();
        if(!model.checkEnoughMoney(uID, cardID))
            throw new NotEnoughMoneyException();

        controller.getModel().returnStudentBag(uID, userData.getStudentColour(), bag);

        model.payCard(uID, cardID);
        overPrice++;
    }
}
