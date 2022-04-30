package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;
import java.util.ArrayList;

public class CharacterCard1 extends CharacterCardWithStudentsList{
    final private Bag bag;

    public CharacterCard1(Bag bag) {
        super(1, 1);
        studentsList = new ArrayList<>();
        this.bag = bag;
        for (int i = 0; i < 4; i++){
            studentsList.add(bag.randomExtraction());
        }
    }

    @Override
    public void handle(String uID, DataBuffer userData, Controller controller) throws Exception {
        if(userData==null || uID==null || controller==null)
            throw new NullPointerException();
        Model model = controller.getModel();
        if(!model.checkEnoughMoney(uID, cardID))
            throw new NotEnoughMoneyException();

        int index= userData.getIslandPos();
        Colour colour= userData.getStudentColour();
        model.addStudentIsland(index, removeStudent(colour));
        studentsList.add(bag.randomExtraction());

        model.payCard(uID, cardID);
        overPrice=true;
    }
}
