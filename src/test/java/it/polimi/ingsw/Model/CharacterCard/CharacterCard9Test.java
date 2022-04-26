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

class CharacterCard9Test
{
    @Test
    public void CharacterCard9Handle() throws Exception
    {
        String player1="Aldo";
        String player2="Giovanni";
        CharacterCard9 card = new CharacterCard9();
        Map<String, DataBuffer> uIDs = new HashMap<>();
        uIDs.put(player1, new DataBuffer(player1));
        uIDs.put(player2, new DataBuffer(player2));

        Controller controller = new Controller(uIDs, true);
        ModelTest.changeCard(controller.getModel(), card);
        for(int i=0;i<6;i++)
            controller.getModel().addStudentDashboard(player2, new Student(Colour.blue));
        DataBuffer data= new DataBuffer(player2);
        data.setStudColour(Colour.pink);
        controller.getModel().addStudentIsland(9, new Student(Colour.pink));
        controller.getModel().addStudentIsland(9, new Student(Colour.pink));
        card.handle(player2, data, controller);
        controller.getModel().moveMN(9);
    }
}