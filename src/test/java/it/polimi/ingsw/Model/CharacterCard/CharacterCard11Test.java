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

class CharacterCard11Test
{
    @Test
    public void CharacterCard11Handle() throws Exception
    {
        String player1 = "Aldo";
        String player2 = "Giovanni";
        String player3 = "Giacomo";
        Bag bag= new Bag();
        CharacterCard11 card = new CharacterCard11(bag);
        List<String> uIDs = new ArrayList<>();
        uIDs.add(player1);
        uIDs.add(player2);
        uIDs.add(player3);

        Controller controller = new Controller(uIDs, true);
        ModelTest.changeCard(controller.getModel(), card);
        for (int i = 0; i < 3; i++)
            controller.getModel().addStudentDashboard(player1, new Student(Colour.green));
        card.studentsList.remove(0);
        card.studentsList.add(new Student(Colour.red));
        DataBuffer data = new DataBuffer(player1);
        data.setStudColour(Colour.red);
        card.handle(player1, data, controller);
    }
}