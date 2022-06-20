package it.polimi.ingsw.Model;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;
import it.polimi.ingsw.Model.ModelAndDecorators.ModelTest;
import it.polimi.ingsw.RemoteView.RemoteView;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    /**
     * This test sets a specified number of students in a player's classroom; then, with the method that is under
     * testing, we get the number of students of a specified color in the classroom, which must correspond to the number
     * of students that we added at start.
     * The exceptions are also tested.
     */
    @Test
    public void testGetStudentsNum() throws FullClassException {
        Player player = new Player("test", new Towers(ColourT.black, 1));
        //player.setNickName("test");
        int num = player.getStudentNum(Colour.red);
        assertEquals(num, 0);
        player.addStudent(new Student(Colour.red), true);
        num = player.getStudentNum(Colour.green);
        assertEquals(num, 0);
        num = player.getStudentNum(Colour.red);
        assertEquals(num, 1);
        try{
            player.getStudentNum(null);
            assertTrue(false);
        }
        catch (NullPointerException n){};
    }

    /**
     * In this method we create a list of student so that we know which students should be added in the entrance;
     * at this point we call the method entranceFiller so that the students are added in the entrance; then, with
     * a getter and an assert methods we see if the students have been added correctly.
     */
    @Test
    public void testEntranceFillerEmptier() throws EmptyException, NoSuchStudentException, FullEntranceException {
        Player player = new Player("test", new Towers(ColourT.black, 1));
        Colour colour;
        List<Student> students= new ArrayList<>();
        students.add(new Student(Colour.blue));
        player.entranceFiller(students);
        List<Colour> colours = new ArrayList<>();
        colours = player.getStudents();
        for(int i=0; i < colours.size(); i++){
            colour = colours.get(i);
            if (!colour.equals(Colour.blue))
                fail();
        }
        Colour colour1 = player.entranceEmptier(Colour.blue).getColour();
        if(!colour1.equals(Colour.blue))
            fail();
    }

    /**
     * In this test we try to add and remove teachers from the dashboard to see if the methods work correctly.
     * The correctness is verified with assert methods.
     * Also, the exceptions are called.
     */
    @Test
    public void testTeacherMovements() throws TeacherAlreadyInException, TooManyTeachersException, NoSuchTeacherException {
        Player player = new Player("test", new Towers(ColourT.black, 1));
        assertFalse(player.checkTeacherPresence(Colour.red));
        player.addTeacher(new Teacher(Colour.red));
        assertTrue(player.checkTeacherPresence(Colour.red));
        try{
            player.removeTeacher(Colour.blue);
            fail();
        } catch (NoSuchTeacherException n){};

        try{
            player.removeTeacher(null);
            fail();
        } catch (NullPointerException n){};

        Teacher teachertest = player.removeTeacher(Colour.red);
        if (!teachertest.getColour().equals(Colour.red))
            fail();
    }

    /**
     * In this test we create a set of towers and a player, and we check the correct assignment of both. Then, we
     * create another set of towers, and we check that is different from the one that the player has.
     */
    @Test
    public void towersTest(){
        Towers towers = new Towers(ColourT.black, 1);
        Player player = new Player("test", towers);
        assertTrue(player.getTowers().equals(towers));
        Towers towers1 = new Towers(ColourT.white, 1);
        assertFalse(player.getTowers().equals(towers1));
    }

    /**
     * In this test we check that the methods to play a card and the one to control its MN value are correct with
     * an assert method.
     */
    @Test
    public void getLastCardMNValuetest() throws EmptyException
    {
        Player player = new Player("test", new Towers(ColourT.black, 1));
        StandardCard sc = player.cardDiscarder(0);
        int i = player.getDiscardedCard().getMnValue();
        assertEquals(i, 1);
    }

    /**
     * In this test we check the correct assignment of coins to the player by obtaining multiple of 3 students;
     * then, we try to pay a card with a player that does not have enough coins and we catch the expected exception;
     * finally we try to pay a card being sure that the player has enough money.
     * Everything is checked with assert methods and by catching exceptions.
     */
    @Test
    public void paymentTest() throws FullClassException, CardPaymentException, NoSuchStudentException {
        Map<String, DataBuffer> uIDs = new HashMap<>();
        uIDs.put("Aldo", new DataBuffer("Aldo"));
        uIDs.put("Giovanni", new DataBuffer("Giovanni"));
        uIDs.put("Giacomo", new DataBuffer("Giacomo"));

        Controller controller = new Controller(uIDs, true, new ArrayList<RemoteView>());

        try {
            ModelTest.getPlayers(controller.getModel()).get(0).pay(2);
            fail();
        } catch (CardPaymentException c){}

        for (int i = 0; i < 4; i++){
            ModelTest.getPlayers(controller.getModel()).get(0).addStudent(new Student(Colour.red), true);
        }

        ModelTest.getPlayers(controller.getModel()).get(0).removeStudentClassroom(Colour.red);

        assertEquals(ModelTest.getPlayers(controller.getModel()).get(0).getStudentNum(Colour.red), 3);

        assertEquals(ModelTest.getPlayers(controller.getModel()).get(0).getCoins(),2);

        ModelTest.getPlayers(controller.getModel()).get(0).pay(2);

        assertEquals(ModelTest.getPlayers(controller.getModel()).get(0).getCoins(),0);

        assertEquals(ModelTest.getPlayers(controller.getModel()).get(0).getHandCards().size(), 10);
    }

    /**
     * test for printer methods
     */
    @Test
    public void printerTest() throws Exception{
        List<String> uIDs=new ArrayList<>();
        uIDs.add("Francio"); uIDs.add("Tarallo");
        Model model=new Model(uIDs, true);
        Player player = new Player("Aldo", new Towers(ColourT.black, 0));
        Player player1 = new Player("Giovanni", new Towers(ColourT.white, 0));
        Player player2 = new Player("Giacomo", new Towers(ColourT.grey, 0));
        player.addTeacher(new Teacher(Colour.red));
        player.addStudent(new Student(Colour.red), true);
        List<Student> students = new ArrayList<>();
        students.add(new Student(Colour.red));
        students.add(new Student(Colour.red));
        students.add(new Student(Colour.blue));
        player.entranceFiller(students);
        System.setOut(new PrintStream(new OutputStream()
        {
            public void close() {}
            public void flush() {}
            public void write(byte[] b) {}
            public void write(byte[] b, int off, int len) {}
            public void write(int b) {}
        }
        ));
        player.playerPrinter(true);
        player1.playerPrinter(true);
        player2.playerPrinter(true);
    }
}