package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;

import java.util.List;

public class CharacterCard10 extends CharacterCard{

    public CharacterCard10(){ super(10, 1); }

    @Override
    public void handle(String uID, Object choice, Controller controller) throws Exception
    {
        Model model;
        Colour entranceStudentColour=null;
        Colour classroomStudentColour=null;
        List<Colour> studentsToMove;
        int studentsNum=0;

        if(uID==null || choice==null || controller==null)
            throw new NullPointerException();
        if(!controller.getModel().checkEnoughMoney(uID, cardID))
            throw new NotEnoughMoneyException();

        model=controller.getModel();
        studentsToMove=(List<Colour>) choice;
        studentsNum=studentsToMove.size();

        for(int i=0; i<(studentsNum/2); i++)
        {
            entranceStudentColour=studentsToMove.get(0);
            classroomStudentColour=studentsToMove.get(1);
            model.studentsSwap(uID, entranceStudentColour, classroomStudentColour);
            studentsToMove.remove(0);
            studentsToMove.remove(0);
        }

        controller.getModel().payCard(uID, cardID);
        overPrice++;
    }
}
