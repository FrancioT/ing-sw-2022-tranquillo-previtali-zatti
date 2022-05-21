package it.polimi.ingsw.Model;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.ModelAndDecorators.ModelTest;
import it.polimi.ingsw.RemoteView.RemoteView;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    public void testGetStudentsNum() throws FullClassException {
        Player player = new Player("test", new Towers(ColourT.black, 1), null);
        //player.setNickName("test");
        int num = player.getStudentNum(Colour.red);
        assertEquals(num, 0);
        player.addStudent(new Student(Colour.red));
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

    @Test
    public void testEntranceFillerEmptier() throws EmptyException, NoSuchStudentException, FullEntranceException {
        Player player = new Player("test", new Towers(ColourT.black, 1), null);
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

    @Test
    public void testTeacherMovements() throws TeacherAlreadyInException, TooManyTeachersException, NoSuchTeacherException {
        Player player = new Player("test", new Towers(ColourT.black, 1), null);
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

    @Test
    public void towersTest(){
        Towers towers = new Towers(ColourT.black, 1);
        Player player = new Player("test", towers, null);
        assertTrue(player.getTowers().equals(towers));
        Towers towers1 = new Towers(ColourT.white, 1);
        assertFalse(player.getTowers().equals(towers1));
    }

    @Test
    public void getLastCardMNValuetest() throws EmptyException
    {
        Player player = new Player("test", new Towers(ColourT.black, 1), null);
        StandardCard sc = player.cardDiscarder(0);
        int i = player.getLastCardMNValue();
        assertEquals(i, 1);
    }

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
            ModelTest.getPlayers(controller.getModel()).get(0).addStudent(new Student(Colour.red));
        }

        ModelTest.getPlayers(controller.getModel()).get(0).removeStudentClassroom(Colour.red);

        assertEquals(ModelTest.getPlayers(controller.getModel()).get(0).getStudentNum(Colour.red), 3);

        assertEquals(ModelTest.getPlayers(controller.getModel()).get(0).getCoins(),2);

        ModelTest.getPlayers(controller.getModel()).get(0).pay(2);

        assertEquals(ModelTest.getPlayers(controller.getModel()).get(0).getCoins(),0);

        assertEquals(ModelTest.getPlayers(controller.getModel()).get(0).getHandCards().size(), 10);
    }

}