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

class CharacterCard5Test
{
    @Test
    public void CharacterCard5Handle() throws Exception
    {
        String player1 = "Aldo";
        String player2 = "Giovanni";
        String player3 = "Giacomo";
        CharacterCard5 card = new CharacterCard5();
        Map<String, DataBuffer> uIDs = new HashMap<>();
        uIDs.put(player1, new DataBuffer(player1));
        uIDs.put(player2, new DataBuffer(player2));
        uIDs.put(player3, new DataBuffer(player3));

        Controller controller = new Controller(uIDs, true);
        ModelTest.changeCard(controller.getModel(), card);
        for(int i=0;i<3;i++)
            controller.getModel().addStudentDashboard(player2, new Student(Colour.yellow));
        DataBuffer data= new DataBuffer(player2);
        data.setIslandPos(2);
        card.handle(player2, data, controller);
        for(int i=0;i<9;i++)
            controller.getModel().addStudentDashboard(player2, new Student(Colour.blue));
        data.setIslandPos(2);
        card.handle(player2, data, controller);
        controller.getModel().moveMN(2);
    }
}