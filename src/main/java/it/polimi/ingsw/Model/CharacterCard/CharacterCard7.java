package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.NoSuchStudentException;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;
import it.polimi.ingsw.Model.Student;

import java.util.ArrayList;
import java.util.List;

public class CharacterCard7 extends CharacterCard {
    List<Student> studentsList;

    public CharacterCard7(Bag bag){
        super(7, 1);
        studentsList = new ArrayList<>();
        for( int i = 0; i < 6; i++){
            studentsList.add(bag.randomExtraction());
        }
    }

    public List<Colour> getColoursOnCard(){
        List<Colour> colours = new ArrayList<>();
        for(Student s: studentsList)
            colours.add(s.getColour());
        return colours;
    }

    private Student removeStudent(Colour colour) throws NoSuchStudentException {
        Student student = null;
        for(int i = 0; i < getColoursOnCard().size(); i++){
            if(getColoursOnCard().get(i) == colour){
                student = studentsList.get(i);
                break;
            }
        }
        if (student != null){
            studentsList.remove(student);
            return student;
        }

        else
            throw new NoSuchStudentException();
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
            model.addStudentDashboard(uID, removeStudent(studentsToSwap.get(i)));
        }

        model.payCard(uID, cardID);
        overPrice ++;
    }
}
