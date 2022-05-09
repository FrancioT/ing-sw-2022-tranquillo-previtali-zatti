package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.ModelTest;
import it.polimi.ingsw.Model.Student;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        List<CharacterCard> cards = new ArrayList<>();
        cards.add(card);
        ModelTest.changeCard(controller.getModel(), cards);

        try{
            card.handle(null, null, null);
            fail();
        }catch (NullPointerException n){};

        try {
            card.handle("Aldo", new DataBuffer(player1), controller);
            fail();
        }catch (NotEnoughMoneyException n){};

        DataBuffer data = new DataBuffer("Giacomo");
        data.setStudColour(Colour.red);

        for(int i=0; i<5; i++)
            controller.getModel().addStudentDashboard("Aldo", new Student(Colour.red));

        for(int i=0; i<2; i++)
            controller.getModel().addStudentDashboard("Giovanni", new Student(Colour.red));

        for(int i=0; i<6; i++)
            controller.getModel().addStudentDashboard("Giacomo", new Student(Colour.green));

        assertTrue(ModelTest.getPlayers(controller.getModel()).get(0).checkTeacherPresence(Colour.green));
        assertTrue(ModelTest.getPlayers(controller.getModel()).get(1).checkTeacherPresence(Colour.red));

        card.handle("Giacomo", data, controller);

        assertTrue(controller.getModel().getStudentsNum("Giacomo", Colour.red) == 0);
        assertTrue(controller.getModel().getStudentsNum("Aldo", Colour.red) == 2);
        assertTrue(controller.getModel().getStudentsNum("Giovanni", Colour.red) == 0);
        assertTrue(ModelTest.getPlayers(controller.getModel()).get(1).checkTeacherPresence(Colour.red));
    }
}