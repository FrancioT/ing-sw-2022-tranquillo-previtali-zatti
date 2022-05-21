package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.NoSuchStudentException;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;

import java.util.ArrayList;
import java.util.List;

public class CharacterCard10 extends CharacterCard
{
    static final long serialVersionUID= 80308L;

    public CharacterCard10(){ super(10, 1); }

    @Override
    public void handle(String uID, DataBuffer userData, Controller controller) throws Exception
    {
        Model model;
        Colour entranceStudentColour=null;
        Colour classroomStudentColour=null;
        List<Colour> studentsToMove;
        List<Colour> studentsToEntrance = new ArrayList<>();
        List<Colour> studentsToClassroom = new ArrayList<>();

        int studentsNum=0;

        if(uID==null || userData==null || controller==null)
            throw new NullPointerException();
        if(!controller.getModel().checkEnoughMoney(uID, cardID))
            throw new NotEnoughMoneyException();

        model=controller.getModel();
        // The order of studentToSwap must be: up to 2 pairs of colours, the first colour of the pair represents
        // the student in the classroom that must be swapped with the student from the entrance whose colour is
        // the second one of the pair
        studentsToMove= userData.getStudentsColours();
        studentsNum=studentsToMove.size();
        if(studentsNum%2!=0 || studentsNum>4)
            throw new IllegalArgumentException();

        for(int i=0; i< studentsNum/2; i++){
            studentsToClassroom.add(studentsToMove.get(2*i));
            studentsToEntrance.add(studentsToMove.get(2*i+1));
        }

        for(Colour c : studentsToClassroom) {
            int check = 0;
            for (Colour colour : studentsToClassroom)
                if (c == colour)
                    check++;
            for (Colour colour1 : model.getStudents(uID))
                if (c == colour1)
                    check--;
            if (check > 0)
                throw new NoSuchStudentException();
        }

        for(Colour c : studentsToEntrance) {
            int check = 0;
            for (Colour colour : studentsToEntrance)
                if (c == colour)
                    check++;
            check = check - model.getStudentsNum(uID,c);
            if (check > 0)
                throw new NoSuchStudentException();
        }

        for(int i=0; i<(studentsNum/2); i++)
        {
            entranceStudentColour=studentsToMove.get(0);
            classroomStudentColour=studentsToMove.get(1);
            model.studentsSwap(uID, entranceStudentColour, classroomStudentColour);
            studentsToMove.remove(0);
            studentsToMove.remove(0);
        }

        model.payCard(uID, cardID);
        overPrice=true;
    }

    @Override
    public void ccPrinter()
    {
        super.ccPrinter();
        System.out.println("Effect: You may exchange up to 2 Students between your Entrance and your classroom.");
        System.out.println("Parameters: list of colours (order must be: up to 2 pairs of colours, the first colour of\n" +
                "the pair represents the student in the classroom that must be swapped with the student from\n" +
                "the entrance whose colour is the second one of the pair)");
    }
}
