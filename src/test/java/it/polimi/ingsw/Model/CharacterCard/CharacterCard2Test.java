package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.NoSuchPlayerException;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;
import it.polimi.ingsw.Model.ModelAndDecorators.ModelTest;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Student;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCard2Test {
    @Test
    public void CharacterCard2Handle() throws Exception {
        CharacterCard2 card = new CharacterCard2();
        List<CharacterCard> cards = new ArrayList<>();
        cards.add(card);

        assertEquals(card.getCardID(), 2);
        assertEquals(card.getPrice(), 2);

        Map<String, DataBuffer> players = new HashMap<>();
        players.put("Aldo", new DataBuffer("Aldo"));
        players.put("Giovanni", new DataBuffer("Giovanni"));
        players.put("Giacomo", new DataBuffer("Giacomo"));

        Controller controller = new Controller(players, true);
        ModelTest.changeCard(controller.getModel(), cards);

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

        try {
            card.handle("Ajeje", null, controller);
            fail();
        }catch (NoSuchPlayerException n){};

        assertTrue(ModelTest.getPlayers(controller.getModel()).get(1).checkTeacherPresence(Colour.red));
        assertTrue(ModelTest.getPlayers(controller.getModel()).get(1).checkTeacherPresence(Colour.blue));
        assertTrue(ModelTest.getPlayers(controller.getModel()).get(1).checkTeacherPresence(Colour.green));
        assertFalse(ModelTest.getPlayers(controller.getModel()).get(1).checkTeacherPresence(Colour.pink));
        assertFalse(ModelTest.getPlayers(controller.getModel()).get(2).checkTeacherPresence(Colour.red));
        assertFalse(ModelTest.getPlayers(controller.getModel()).get(2).checkTeacherPresence(Colour.blue));
        assertFalse(ModelTest.getPlayers(controller.getModel()).get(2).checkTeacherPresence(Colour.green));
        assertFalse(ModelTest.getPlayers(controller.getModel()).get(2).checkTeacherPresence(Colour.pink));

        card.handle("Giovanni", null, controller);

        assertFalse(ModelTest.getPlayers(controller.getModel()).get(1).checkTeacherPresence(Colour.red));
        assertTrue(ModelTest.getPlayers(controller.getModel()).get(1).checkTeacherPresence(Colour.blue));
        assertTrue(ModelTest.getPlayers(controller.getModel()).get(1).checkTeacherPresence(Colour.green));
        assertFalse(ModelTest.getPlayers(controller.getModel()).get(1).checkTeacherPresence(Colour.pink));
        assertTrue(ModelTest.getPlayers(controller.getModel()).get(2).checkTeacherPresence(Colour.red));
        assertFalse(ModelTest.getPlayers(controller.getModel()).get(2).checkTeacherPresence(Colour.blue));
        assertFalse(ModelTest.getPlayers(controller.getModel()).get(2).checkTeacherPresence(Colour.green));
        assertFalse(ModelTest.getPlayers(controller.getModel()).get(2).checkTeacherPresence(Colour.pink));

        controller.getModel().addStudentDashboard("Giovanni", new Student(Colour.blue));

        assertFalse(ModelTest.getPlayers(controller.getModel()).get(1).checkTeacherPresence(Colour.red));
        assertFalse(ModelTest.getPlayers(controller.getModel()).get(1).checkTeacherPresence(Colour.blue));
        assertTrue(ModelTest.getPlayers(controller.getModel()).get(1).checkTeacherPresence(Colour.green));
        assertFalse(ModelTest.getPlayers(controller.getModel()).get(1).checkTeacherPresence(Colour.pink));
        assertTrue(ModelTest.getPlayers(controller.getModel()).get(2).checkTeacherPresence(Colour.red));
        assertTrue(ModelTest.getPlayers(controller.getModel()).get(2).checkTeacherPresence(Colour.blue));
        assertFalse(ModelTest.getPlayers(controller.getModel()).get(2).checkTeacherPresence(Colour.green));
        assertFalse(ModelTest.getPlayers(controller.getModel()).get(2).checkTeacherPresence(Colour.pink));

        controller.getModel().addStudentDashboard("Giovanni", new Student(Colour.pink));

        assertFalse(ModelTest.getPlayers(controller.getModel()).get(1).checkTeacherPresence(Colour.red));
        assertFalse(ModelTest.getPlayers(controller.getModel()).get(1).checkTeacherPresence(Colour.blue));
        assertTrue(ModelTest.getPlayers(controller.getModel()).get(1).checkTeacherPresence(Colour.green));
        assertFalse(ModelTest.getPlayers(controller.getModel()).get(1).checkTeacherPresence(Colour.pink));
        assertTrue(ModelTest.getPlayers(controller.getModel()).get(2).checkTeacherPresence(Colour.red));
        assertTrue(ModelTest.getPlayers(controller.getModel()).get(2).checkTeacherPresence(Colour.blue));
        assertFalse(ModelTest.getPlayers(controller.getModel()).get(2).checkTeacherPresence(Colour.green));
        assertTrue(ModelTest.getPlayers(controller.getModel()).get(2).checkTeacherPresence(Colour.pink));

        assertTrue(card.overPrice);
    }

}