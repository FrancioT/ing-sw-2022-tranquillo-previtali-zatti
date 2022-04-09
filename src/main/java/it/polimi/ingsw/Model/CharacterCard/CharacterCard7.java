package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Colour;
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
    public void handle(String uID, Object choice, Controller controller) throws Exception {
        if(uID==null || choice==null || controller==null)
            throw new NullPointerException();
        Model model = controller.getModel();
        if(!model.checkEnoughMoney(uID, cardID))
            throw new NotEnoughMoneyException();

        List<Colour> studentsToSwap = (List<Colour>) choice;    /*the first half will be the colors to add to the card*/
                                                                /*the second half are the ones to add in the entrance*/


        for(int i = 0; i < studentsToSwap.size()/2; i++){
            studentsList.add(model.entranceEmptier(uID, studentsToSwap.get(i)));
        }

        for(int i = studentsToSwap.size()/2; i < studentsToSwap.size(); i++){
            List<Student> studentToAdd = new ArrayList<>();
            studentToAdd.add(removeStudent(studentsToSwap.get(i)));
            model.entranceFiller(uID, studentToAdd);
        }

        model.payCard(uID, cardID);
        overPrice ++;
    }
}
