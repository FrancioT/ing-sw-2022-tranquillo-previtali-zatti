package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.ModelAndDecorators.ModelTest;
import it.polimi.ingsw.Model.Student;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCard12Test
{
    @Test
    public void characterCard12() throws Exception {
        String player1 = "Aldo";
        String player2 = "Giovanni";
        String player3 = "Giacomo";
        Bag bag= new Bag();

        Map<String, DataBuffer> uIDs = new HashMap<>();
        uIDs.put(player1, new DataBuffer(player1));
        uIDs.put(player2, new DataBuffer(player2));
        uIDs.put(player3, new DataBuffer(player3));

        Controller controller = new Controller(uIDs, true);
        CharacterCard12 card = new CharacterCard12(bag, ModelTest.getPlayers(controller.getModel()));
        ModelTest.changeCard(controller.getModel(), card);

        for(int i = 0; i < 7; i++)
            controller.getModel().addStudentDashboard(player1, new Student(Colour.green));

        controller.getModel().addStudentDashboard(player1, new Student(Colour.red));
        controller.getModel().addStudentDashboard(player1, new Student(Colour.red));
        controller.getModel().addStudentDashboard(player1, new Student(Colour.red));
        controller.getModel().addStudentDashboard(player1, new Student(Colour.red));

        controller.getModel().addStudentDashboard(player2, new Student(Colour.red));
        controller.getModel().addStudentDashboard(player2, new Student(Colour.red));

        uIDs.get(player1).setStudColour(Colour.red);

        card.handle(player1, uIDs.get(player1), controller);

    }
}