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

class CharacterCard3Test
{
    @Test
    public void CharacterCard3Handle() throws Exception
    {
        String player1="Aldo";
        String player2="Giovanni";
        String player3="Giacomo";        CharacterCard3 card= new CharacterCard3();
        Map<String, DataBuffer> uIDs = new HashMap<>();
        uIDs.put(player1, new DataBuffer(player1));
        uIDs.put(player2, new DataBuffer(player2));
        uIDs.put(player3, new DataBuffer(player3));

        Controller controller = new Controller(uIDs, true);
        ModelTest.changeCard(controller.getModel(), card);

        for(int i=0; i<6; i++)
            controller.getModel().addStudentDashboard(player3, new Student(Colour.pink));
        DataBuffer data= new DataBuffer(player3);
        data.setIslandPos(3);
        card.handle(player3, data, controller);
    }
}