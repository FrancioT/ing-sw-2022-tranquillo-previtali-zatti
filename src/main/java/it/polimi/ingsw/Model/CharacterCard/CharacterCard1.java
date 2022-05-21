package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.Exceptions.RunOutOfStudentsException;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;

public class CharacterCard1 extends CharacterCardWithStudentsList{
    transient final private Bag bag;
    static final long serialVersionUID= 80311L;

    public CharacterCard1(Bag bag) throws RunOutOfStudentsException
    {
        super(1, 1);
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

    @Override
    public void ccPrinter()
    {
        super.ccPrinter();
        System.out.println("Effect: Take 1 student from this card and place it on an island of your choice.");
        System.out.println("Parameters: Island's position, Student's colour");
    }
}
