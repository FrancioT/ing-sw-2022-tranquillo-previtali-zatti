package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.NoSuchStudentException;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;
import it.polimi.ingsw.Model.Student;

import java.util.ArrayList;
import java.util.List;

public class CharacterCard7 extends CharacterCardWithStudentsList {

    public CharacterCard7(Bag bag){
        super(7, 1);
        studentsList = new ArrayList<>();
        for( int i = 0; i < 6; i++){
            studentsList.add(bag.randomExtraction());
        }
    }

    @Override
    public void handle(String uID, DataBuffer userData, Controller controller) throws Exception {
        if(uID==null || userData==null || controller==null)
            throw new NullPointerException();
        Model model = controller.getModel();
        if(!model.checkEnoughMoney(uID, cardID))
            throw new NotEnoughMoneyException();

        boolean ok = false;
        List<Colour> studentsToSwap= userData.getStudentsColours();
        if(studentsToSwap.size()%2!=0 || studentsToSwap.size()>6)
            throw new IllegalArgumentException();

        int studentsToMove = studentsToSwap.size()/2;
        List<Colour> studentsToCard = new ArrayList<>();
        List<Colour> studentsToEntrance = new ArrayList<>();

        for(int i = 0; i < studentsToMove; i++){
            studentsToCard.add(studentsToSwap.remove(0));
        }

        for(int i = 0; i < studentsToMove; i++){
            studentsToEntrance.add(studentsToSwap.remove(0));
        }

        for(Colour c : studentsToCard){
            for(Colour colour : model.getStudents(uID)){
                if(c.equals(colour))
                    ok = true;
            }
            if(!ok)
                throw new NoSuchStudentException();
            else
                ok = false;
        }

        for(Colour c : studentsToEntrance){
            for(Colour colour : getColoursOnCard()){
                if(c.equals(colour))
                    ok = true;
            }
            if(!ok)
                throw new NoSuchStudentException();
            else
                ok = false;
        }

        for(int i = 0; i < studentsToMove; i++){
            studentsList.add(model.entranceEmptier(uID, studentsToCard.remove(0)));
        }

        for(int i = 0; i < studentsToMove; i++){
            List<Student> studentToAdd = new ArrayList<>();
            studentToAdd.add(removeStudent(studentsToEntrance.remove(0)));
            model.entranceFiller(uID, studentToAdd);
        }

        model.payCard(uID, cardID);
        overPrice ++;
    }
}
