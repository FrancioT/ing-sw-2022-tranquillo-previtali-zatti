package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import java.util.ArrayList;


public class CharacterCard11 extends CharacterCardWithStudentsList{
    final private Bag bag;

    public CharacterCard11(Bag bag) {
        super(11, 2);
        this.bag = bag;
        studentsList = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            studentsList.add(bag.randomExtraction());
    }

    @Override
    public void handle(String uID, DataBuffer userData, Controller controller) throws Exception{
        if(userData==null || uID==null || controller==null)
            throw new NullPointerException();
        if(!controller.getModel().checkEnoughMoney(uID, cardID))
            throw new NotEnoughMoneyException();

        Colour colour= userData.getStudentColour();
        controller.getModel().addStudentDashboard(uID, removeStudent(colour));
        studentsList.add(bag.randomExtraction());

        controller.getModel().payCard(uID, cardID);
        overPrice=true;
    }
}
