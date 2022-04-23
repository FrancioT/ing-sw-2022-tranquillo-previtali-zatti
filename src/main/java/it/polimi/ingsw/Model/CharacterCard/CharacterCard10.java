package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;

import java.util.List;

public class CharacterCard10 extends CharacterCard{

    public CharacterCard10(){ super(10, 1); }

    @Override
    public void handle(String uID, DataBuffer userData, Controller controller) throws Exception
    {
        Model model;
        Colour entranceStudentColour=null;
        Colour classroomStudentColour=null;
        List<Colour> studentsToMove;
        int studentsNum=0;

        if(uID==null || userData==null || controller==null)
            throw new NullPointerException();
        if(!controller.getModel().checkEnoughMoney(uID, cardID))
            throw new NotEnoughMoneyException();

        model=controller.getModel();
        studentsToMove= userData.getStudentsColours();
        studentsNum=studentsToMove.size();
        if(studentsNum%2!=0)
            throw new IllegalArgumentException();

        for(int i=0; i<(studentsNum/2); i++)
        {
            entranceStudentColour=studentsToMove.get(0);
            classroomStudentColour=studentsToMove.get(1);
            model.studentsSwap(uID, entranceStudentColour, classroomStudentColour);
            studentsToMove.remove(0);
            studentsToMove.remove(0);
        }

        model.payCard(uID, cardID);
        overPrice++;
    }
}
