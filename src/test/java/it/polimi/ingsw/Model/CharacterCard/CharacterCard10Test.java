package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.ModelAndDecorators.ModelTest;
import it.polimi.ingsw.Model.Student;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCard10Test
{
    @Test
    public void CharacterCard10Handle() throws Exception
    {
        String player1="Aldo";
        String player2="Giovanni";
        String player3="Giacomo";
        String player4="Alba";
        CharacterCard10 card = new CharacterCard10();
        List<String> uIDs = new ArrayList<>();
        uIDs.add(player1);
        uIDs.add(player2);
        uIDs.add(player3);
        uIDs.add(player4);

        Controller controller = new Controller(uIDs, true);
        ModelTest.changeCard(controller.getModel(), card);
        DataBuffer data= new DataBuffer(player3);
        List<Colour> colours1= controller.getModel().getStudents(player3);
        List<Colour> colours2= new ArrayList<>();
        colours2.add(colours1.remove(0));
        colours2.add(Colour.pink);
        colours2.add(colours1.remove(0));
        colours2.add(Colour.pink);
        controller.getModel().addStudentDashboard(player3, new Student(Colour.pink));
        controller.getModel().addStudentDashboard(player3, new Student(Colour.pink));
        data.setStudentsColours(colours2);
        card.handle(player3, data, controller);
    }
}