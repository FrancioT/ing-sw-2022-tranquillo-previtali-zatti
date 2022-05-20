package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Exceptions.NoSuchStudentException;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.Exceptions.RunOutOfStudentsException;
import it.polimi.ingsw.Model.ModelAndDecorators.ModelTest;
import it.polimi.ingsw.RemoteView.RemoteView;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCard1Test {
    @Test
    public void CharacterCard1Colours() throws RunOutOfStudentsException {
        CharacterCard1 card = new CharacterCard1(new Bag());

        assertEquals(card.getCardID(), 1);
        assertEquals(card.getPrice(), 1);

        for(int i = 0; i < 4; i++) {
            assertNotNull(card.studentsList.get(i));
        }

        List<Colour> colours = card.getColoursOnCard();
        for(int i = 0; i < colours.size(); i++){
            if(!colours.get(i).equals(card.studentsList.get(i).getColour()))
                fail();
        }
        assertThrows(NullPointerException.class, () -> card.handle(null, null, null));
    }

    @Test
    public void CharacterCard1Handle() throws Exception {
        CharacterCard1 card = new CharacterCard1(new Bag());
        List<CharacterCard> cards = new ArrayList<>();
        cards.add(card);
        Map<String, DataBuffer> uIDs = new HashMap<>();
        uIDs.put("Aldo", new DataBuffer("Aldo"));
        uIDs.put("Giovanni", new DataBuffer("Giovanni"));
        uIDs.put("Giacomo", new DataBuffer("Giacomo"));

        Controller controller = new Controller(uIDs, true, new ArrayList<RemoteView>());

        ModelTest.changeCard(controller.getModel(), cards);

        card.overPrice = true;

        assertEquals(card.getPrice(), 2);

        controller.getModel().addStudentDashboard("Aldo", new Student(Colour.red));
        controller.getModel().addStudentDashboard("Aldo", new Student(Colour.red));
        controller.getModel().addStudentDashboard("Aldo", new Student(Colour.red));
        controller.getModel().addStudentDashboard("Aldo", new Student(Colour.red));

        Colour presentColour = Colour.red, absentColour = Colour.blue;
        DataBuffer datas= new DataBuffer("Aldo");


        datas.setIslandPos(2);
        datas.setStudColour(absentColour);

        card.studentsList.clear();

        for(int i = 0; i < 4; i++)
            card.studentsList.add(new Student(Colour.red));

        try {
            card.handle("Giovanni", datas, controller);
            fail();
        } catch (NotEnoughMoneyException n){}

        try {
            card.handle("Aldo", datas, controller);
            fail();
        } catch (NoSuchStudentException ne){}

        datas.setIslandPos(2);
        datas.setStudColour(presentColour);

        card.handle("Aldo", datas, controller);

        assertEquals(card.studentsList.size(), 4);
        assertTrue(card.overPrice);

        List<Colour> studentsOnIsland = new ArrayList<>(ModelTest.getIslandsList(controller.getModel()).get(2).getStudentsColours());
        assertTrue(studentsOnIsland.get(studentsOnIsland.size()-1) == (Colour.red));
    }
}