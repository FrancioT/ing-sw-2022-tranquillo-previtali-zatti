package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.ModelAndDecorators.ModelTest;
import it.polimi.ingsw.Model.Student;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCard7Test
{
    @Test
    public void CharacterCard7Handle() throws Exception
    {
        String player1="Aldo";
        String player2="Giovanni";
        String player3="Giacomo";
        String player4="Agnese";
        Bag bag=new Bag();
        CharacterCard7 card = new CharacterCard7(bag);
        List<String> uIDs = new ArrayList<>();
        uIDs.add(player1);
        uIDs.add(player2);
        uIDs.add(player3);
        uIDs.add(player4);

        Controller controller = new Controller(uIDs, true);
        ModelTest.changeCard(controller.getModel(), card);
        DataBuffer data= new DataBuffer(player4);
        List<Colour> colours= new ArrayList<>();
        colours.add(Colour.blue);
        colours.add(Colour.pink);
        colours.add(Colour.red);
        colours.add(Colour.green);
        card.studentsList.clear();
        card.studentsList.add(new Student(Colour.red));
        card.studentsList.add(new Student(Colour.green));
        List<Colour> playerStudents= controller.getModel().getStudents(player4);
        controller.getModel().entranceEmptier(player4, playerStudents.remove(0));
        controller.getModel().entranceEmptier(player4, playerStudents.remove(0));
        List<Student> students= new ArrayList<>();
        students.add(new Student(Colour.blue));
        students.add(new Student(Colour.pink));
        controller.getModel().entranceFiller(player4, students);
        data.setStudentsColours(colours);
        card.handle(player3, data, controller);
    }
}