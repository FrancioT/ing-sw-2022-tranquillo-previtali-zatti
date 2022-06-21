package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.ModelAndDecorators.ModelTest;
import it.polimi.ingsw.RemoteView.RemoteView;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the correct creation of the charactercard and test the correct throw of an exception if the method
 * Handle is called with null parameters
 */
class CharacterCard1Test {
    @Test
    public void CharacterCard1Colours() throws RunOutOfStudentsException {
        CharacterCard1 card = new CharacterCard1(new Bag());
        card.initialize();

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

    /**
     * In this test is tested an impossible case where the card is being created but there are no more students
     * available
     */
    @Test
    public void emptyBagExceptionTest() throws RunOutOfStudentsException {
        Bag bag = new Bag();
        for(int i=0; i<10; i++)
            bag.randomExtraction();
        try {
            CharacterCard1 card = new CharacterCard1(bag);
            card.initialize();
            fail();
        }catch (RuntimeException r){};
    }


    /**
     * This test tests the only method of the card: handle;
     * First, I create a controller, which creates a model with the charactercard that I want to test;
     * then, I set up everything I need to make the card work or fail.
     * In a first try I make the card fail by calling it with a student that does not exist, then I activate it
     * with a player that does not have enough money, and finally I try to activate it with a player that
     * does not exist.
     * After trying all the cases that make the card fail I call it with all correct parameters,
     * and I check if it does what I expect.
     * Finally I also test a case that would start the "endgame phase" by emptying the bag.
     *
     * @throws Exception
     */
    @Test
    public void CharacterCard1Handle() throws Exception {
        Bag bag = new Bag();
        CharacterCard1 card = new CharacterCard1(bag);
        card.initialize();
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

        for(int i=0; i<5; i++)
            bag.randomExtraction();

        try {
            bag.randomExtraction();
            fail();
        }catch (RunOutOfStudentsException r){}

        try {
            controller.getModel().activateCard("Pippo", datas, controller);
            fail();
        }catch (IllegalArgumentException i){}
    }


    /**
     * test the printer methods
     */
    @Test
    public void printerTest() throws RunOutOfStudentsException, FullTowersException, RunOutOfTowersException, LinkFailedException {
        CharacterCard1 card = new CharacterCard1(new Bag());
        card.initialize();

        System.setOut(new PrintStream(new OutputStream()
        {
            public void close() {}
            public void flush() {}
            public void write(byte[] b) {}
            public void write(byte[] b, int off, int len) {}
            public void write(int b) {}
        }
        ));
        card.ccPrinter();
    }
}