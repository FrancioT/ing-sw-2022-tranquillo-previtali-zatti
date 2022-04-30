package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.ModelTest;
import it.polimi.ingsw.Model.Student;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCard2Test {
    @Test
    public void CharacterCard2Handle() throws Exception {
        CharacterCard2 card = new CharacterCard2();

        assertEquals(card.getCardID(), 2);
        assertEquals(card.getPrice(), 2);

        Map<String, DataBuffer> players = new HashMap<>();
        players.put("Aldo", new DataBuffer("Aldo"));
        players.put("Giovanni", new DataBuffer("Giovanni"));
        players.put("Giacomo", new DataBuffer("Giacomo"));

        Controller controller = new Controller(players, true);
        ModelTest.changeCard(controller.getModel(), card);

        for(int i=0; i < 3; i++){
            controller.getModel().addStudentDashboard("Aldo", new Student(Colour.red));
            controller.getModel().addStudentDashboard("Aldo", new Student(Colour.blue));
            controller.getModel().addStudentDashboard("Giovanni", new Student(Colour.red));
        }

        controller.getModel().addStudentDashboard("Aldo", new Student(Colour.green));
        controller.getModel().addStudentDashboard("Giovanni", new Student(Colour.blue));
        controller.getModel().addStudentDashboard("Giovanni", new Student(Colour.blue));

        try{
            card.handle(null, null, null);
            fail();
        }catch (NullPointerException n){};

        try {
            card.handle("Giacomo", null, controller);
            fail();
        }catch (NotEnoughMoneyException n){};

        card.handle("Giovanni", null, controller);

        controller.getModel().addStudentDashboard("Giovanni", new Student(Colour.blue));

        controller.getModel().addStudentDashboard("Giovanni", new Student(Colour.pink));

        assertEquals(card.overPrice, true);
    }

}