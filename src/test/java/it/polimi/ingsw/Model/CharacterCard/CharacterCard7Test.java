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

class CharacterCard7Test
{
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
            CharacterCard7 card = new CharacterCard7(bag);
            card.initialize();
            fail();
        }catch (RuntimeException r){};
    }

    /**
     * This test in first instance creates a controller, which creates a model with the charactercards I need.
     * Once the card is created I check if it has the correct values.
     * Then, I test all the cases in which it fails (e.g. not enough money, null pointer, illegal argument,
     * no such student).
     * Finally, I also test the case in which the card works correctly by checking that the students are correctly
     * swapped by using assert methods.
     */
    @Test
    public void CharacterCard7Handle() throws Exception
    {
        String player1="Aldo";
        String player2="Giovanni";
        String player3="Giacomo";
        String player4="Agnese";
        Bag bag=new Bag();
        CharacterCard7 card = new CharacterCard7(bag);
        card.initialize();
        List<CharacterCard> cards = new ArrayList<>();
        cards.add(card);
        Map<String, DataBuffer> uIDs = new HashMap<>();
        uIDs.put(player1, new DataBuffer(player1));
        uIDs.put(player2, new DataBuffer(player2));
        uIDs.put(player3, new DataBuffer(player3));
        uIDs.put(player4, new DataBuffer(player4));
        Controller controller = new Controller(uIDs, true, new ArrayList<RemoteView>());
        ModelTest.changeCard(controller.getModel(), cards);

        try{
            card.handle(null, null, null);
            fail();
        }catch (NullPointerException n){};

        card.overPrice = true;

        try {
            card.handle("Giovanni", new DataBuffer(player1), controller);
            fail();
        }catch (NotEnoughMoneyException n){};

        card.overPrice = false;

        assertTrue(card.studentsList.size() == 6);

        DataBuffer data= new DataBuffer(player4);
        List<Colour> colours= new ArrayList<>();
        colours.add(Colour.blue);
        colours.add(Colour.pink);
        colours.add(Colour.red);
        colours.add(Colour.green);

        card.studentsList.clear();
        card.studentsList.add(new Student(Colour.red));
        card.studentsList.add(new Student(Colour.green));

        List<Colour> playerStudents= controller.getModel().getStudents(player4);
        for(int i=0; i<7; i++)
            controller.getModel().entranceEmptier(player4, playerStudents.remove(0));
        List<Student> students= new ArrayList<>();
        students.add(new Student(Colour.blue));
        students.add(new Student(Colour.pink));
        controller.getModel().entranceFiller(player4, students);

        List<Colour> colourserror= new ArrayList<>();
        colourserror.add(Colour.blue);
        colourserror.add(Colour.pink);
        colourserror.add(Colour.red);
        data.setStudentsColours(colourserror);

        try {
            card.handle(player4,data, controller);
            fail();
        }catch (IllegalArgumentException i){};

        List<Colour> colourserror1= new ArrayList<>();
        colourserror1.add(Colour.blue);
        colourserror1.add(Colour.pink);
        colourserror1.add(Colour.blue);
        colourserror1.add(Colour.green);
        data.setStudentsColours(colourserror1);

        try {
            card.handle(player4, data, controller);
            fail();
        }catch (NoSuchStudentException n){};

        List<Colour> colourserror2= new ArrayList<>();
        colourserror2.add(Colour.red);
        colourserror2.add(Colour.pink);
        colourserror2.add(Colour.red);
        colourserror2.add(Colour.green);
        data.setStudentsColours(colourserror2);

        try {
            card.handle(player4, data, controller);
            fail();
        }catch (NoSuchStudentException n){};

        data.setStudentsColours(colours);
        card.handle(player4, data, controller);

        assertTrue(card.studentsList.get(0).getColour() == Colour.blue);
        assertTrue(card.studentsList.get(1).getColour() == Colour.pink);
        assertTrue(controller.getModel().getStudents(player4).contains(Colour.red));
        assertTrue(controller.getModel().getStudents(player4).contains(Colour.green));
    }

    /**
     * test the printer methods
     */
    @Test
    public void printerTest() throws RunOutOfStudentsException
    {
        CharacterCard7 card7 = new CharacterCard7(new Bag());
        System.setOut(new PrintStream(new OutputStream()
        {
            public void close() {}
            public void flush() {}
            public void write(byte[] b) {}
            public void write(byte[] b, int off, int len) {}
            public void write(int b) {}
        }
        ));
        card7.ccPrinter();
    }
}