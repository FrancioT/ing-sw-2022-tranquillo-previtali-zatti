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

 class CharacterCard1 extends CharacterCard{
    List<Student> studentsList;
    final private Bag bag;

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
        for(Student s: studentsList)
            colours.add(s.getColour());
        return colours;
    }

    @Override
    public void handle(String uID, Object choice, Controller controller) throws Exception {
        if(choice==null || uID==null || controller==null)
            throw new NullPointerException();
        Model model = controller.getModel();
        if(!model.checkEnoughMoney(uID, cardID))
            throw new NotEnoughMoneyException();
        Student tmp = null;
        for(Student s: studentsList)
            if(s.getColour()==(Colour)choice)
                tmp=s;
        if (tmp==null)
            throw new NoSuchStudentException();

        model.addStudentDashboard(uID, tmp);
        studentsList.remove(tmp);
        studentsList.add(bag.randomExtraction());

        model.payCard(uID, cardID);
        overPrice++;
    }
}
