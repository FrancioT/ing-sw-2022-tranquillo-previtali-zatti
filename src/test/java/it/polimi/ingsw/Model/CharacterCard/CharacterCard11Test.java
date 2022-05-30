package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.NoSuchStudentException;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.Exceptions.RunOutOfStudentsException;
import it.polimi.ingsw.Model.ModelAndDecorators.ModelTest;
import it.polimi.ingsw.Model.Student;
import it.polimi.ingsw.RemoteView.RemoteView;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCard11Test
{
    /**
     * This test in first instance creates a controller, which creates a model with the charactercards I need.
     * Once the card is created I check if it has the correct values.
     * Then, I test all the cases in which it fails (e.g. not enough money, null pointer, no such student).
     * Finally, I activate the card and I check that the student has been added to the player's classroom.
     */
    @Test
    public void CharacterCard11Handle() throws Exception
    {
        String player1 = "Aldo";
        String player2 = "Giovanni";
        String player3 = "Giacomo";
        Bag bag= new Bag();
        CharacterCard11 card = new CharacterCard11(bag);
        List<CharacterCard> cards = new ArrayList<>();
        cards.add(card);
        Map<String, DataBuffer> uIDs = new HashMap<>();
        uIDs.put(player1, new DataBuffer(player1));
        uIDs.put(player2, new DataBuffer(player2));
        uIDs.put(player3, new DataBuffer(player3));
        Controller controller = new Controller(uIDs, true, new ArrayList<RemoteView>());
        ModelTest.changeCard(controller.getModel(), cards);

        try{
            card.handle(null, null, null);
            fail();
        }catch (NullPointerException n){};

        try {
            card.handle("Aldo", new DataBuffer(player1), controller);
            fail();
        }catch (NotEnoughMoneyException n){};

        for(int i=0; i<6; i++)
            controller.getModel().addStudentDashboard("Giacomo", new Student(Colour.yellow));

        for(int i=0; i<2; i++){
            controller.getModel().addStudentDashboard("Aldo", new Student(Colour.red));
            controller.getModel().addStudentDashboard("Giacomo", new Student(Colour.red));
        }

        assertTrue(ModelTest.getPlayers(controller.getModel()).get(1).checkTeacherPresence(Colour.red));

        card.studentsList.clear();
        card.studentsList.add(new Student(Colour.red));

        DataBuffer data = new DataBuffer("Giacomo");

        data.setStudColour(Colour.green);

        try {
            card.handle("Giacomo", data, controller);
            fail();
        }catch (NoSuchStudentException n){};

        data.setStudColour(Colour.red);

        card.handle("Giacomo", data, controller);

        assertTrue(card.getColoursOnCard().size() == 1);
        assertTrue(ModelTest.getPlayers(controller.getModel()).get(0).checkTeacherPresence(Colour.red));
    }

    /**
     * test the printer methods
     */
    @Test
    public void printerTest() throws RunOutOfStudentsException
    {
        CharacterCard11 card11 = new CharacterCard11(new Bag());
        System.setOut(new PrintStream(new OutputStream()
        {
            public void close() {}
            public void flush() {}
            public void write(byte[] b) {}
            public void write(byte[] b, int off, int len) {}
            public void write(int b) {}
        }
        ));
        card11.ccPrinter();
    }
}