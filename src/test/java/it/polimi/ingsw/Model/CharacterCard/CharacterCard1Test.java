package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Exceptions.NoSuchStudentException;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.ModelTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/*
class CharacterCard1Test {
    @Test
    public void CharacterCard1Colours(){
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
        List<String> uIDs = new ArrayList<>();
        uIDs.add("Aldo");
        uIDs.add("Giovanni");
        uIDs.add("Giacomo");

        Controller controller = new Controller(uIDs, true);

        ModelTest.changeCard(controller.getModel(), card);

        card.overPrice = 1;

        assertEquals(card.getPrice(), 2);

        controller.getModel().addStudentDashboard("Aldo", new Student(Colour.red));
        controller.getModel().addStudentDashboard("Aldo", new Student(Colour.red));
        controller.getModel().addStudentDashboard("Aldo", new Student(Colour.red));
        controller.getModel().addStudentDashboard("Aldo", new Student(Colour.red));

        Colour presentColour = Colour.red, absentColour = Colour.blue;
        List<Object> list = new ArrayList<>();

        list.add(2);
        list.add(absentColour);

        card.studentsList.clear();

        for(int i = 0; i < 4; i++)
            card.studentsList.add(new Student(Colour.red));

        try {
            card.handle("Giovanni", list, controller);
            fail();
        } catch (NotEnoughMoneyException n){}

        try {
            card.handle("Aldo", list, controller);
            fail();
        } catch (NoSuchStudentException ne){}

        list.remove(1);
        list.add(presentColour);

        card.handle("Aldo", list, controller);

        assertEquals(card.studentsList.size(), 4);
        assertEquals(card.overPrice, 2);
    }
}*/