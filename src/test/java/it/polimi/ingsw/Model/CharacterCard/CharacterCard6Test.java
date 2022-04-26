package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.ModelAndDecorators.ModelTest;
import it.polimi.ingsw.Model.Student;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCard6Test
{
    @Test
    public void CharacterCard6Handle() throws Exception
    {
        String player1="Aldo";
        String player2="Giovanni";
        CharacterCard6 card = new CharacterCard6();
        Map<String, DataBuffer> uIDs = new HashMap<>();
        uIDs.put(player1, new DataBuffer(player1));
        uIDs.put(player2, new DataBuffer(player2));

        Controller controller = new Controller(uIDs, true);
        ModelTest.changeCard(controller.getModel(), card);
        for(int i=0;i<6;i++)
            controller.getModel().addStudentDashboard(player2, new Student(Colour.yellow));
        card.handle(player2, null, controller);
        controller.getModel().addStudentIsland(1, new Student(Colour.red));
        controller.getModel().addStudentDashboard(player2, new Student(Colour.red));
        controller.getModel().moveMN(1);
        controller.getModel().addStudentIsland(1, new Student(Colour.blue));
        controller.getModel().addStudentIsland(1, new Student(Colour.blue));
        controller.getModel().addStudentDashboard(player2, new Student(Colour.blue));
        controller.getModel().moveMN(12);
    }
}