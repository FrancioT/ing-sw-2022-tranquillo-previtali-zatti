package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.NoSuchStudentException;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;
import it.polimi.ingsw.Model.Student;
import java.util.ArrayList;
import java.util.List;

 class CharacterCard1 extends CharacterCard{
    List<Student> studentsList;
    final private Bag bag;
    Model model;

    public CharacterCard1(Bag bag) {
        super(1, 1);
        studentsList = new ArrayList<>();
        this.bag = bag;
        for (int i = 0; i < 4; i++){
            studentsList.add(bag.randomExtraction());
        }
    }

    public List<Colour> getColoursOnCard(){
        List<Colour> colours = new ArrayList<>();
        for(int i=0;i<4;i++)
            colours.add(studentsList.get(i).getColour());
        return colours;
    }

    @Override
    public void handle(String uID, Object choice, Controller controller) throws Exception {
        model = controller.getModel();
        Student tmp = null;

        for (Student s : studentsList){         /* choice sarà un Colour o uno Student? Ho assunto sia uno Student*/
            if (s.equals(choice))
                tmp = s;
        }

        if (tmp != null){
            model.addStudentDashboard(uID, tmp);
            studentsList.remove(tmp);
            studentsList.add(bag.randomExtraction());
            controller.getModel().payCard(uID, cardID);
            overPrice++;
        }

        else throw new NoSuchStudentException();
    }
}
