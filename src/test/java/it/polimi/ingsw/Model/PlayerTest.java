package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    public void testGetStudentsNum() throws FullClassException {
        Player player = new Player("test", new Towers(ColourT.black, 1), null);
        player.setNickName("test");
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
            if (colour.equals(Colour.blue))
                assertTrue(true);
        }
        Colour colour1 = player.entranceEmptier(Colour.blue).getColour();
        if(colour1.equals(Colour.blue))
            assertTrue(true);
        else
            assertTrue(false);
    }

    @Test
    public void testTeacherMovements() throws TeacherAlreadyInException, TooManyTeachersException, NoSuchTeacherException {
        Player player = new Player("test", new Towers(ColourT.black, 1), null);
        assertFalse(player.checkTeacherPresence(Colour.red));
        player.addTeacher(new Teacher(Colour.red));
        assertTrue(player.checkTeacherPresence(Colour.red));
        try{
            player.removeTeacher(Colour.blue);
            assertTrue(false);
        } catch (NoSuchTeacherException n){};

        try{
            player.removeTeacher(null);
            assertTrue(false);
        } catch (NullPointerException n){};

        Teacher teachertest = player.removeTeacher(Colour.red);
        if (teachertest.getColour().equals(Colour.red))
            assertTrue(true);
        else
            assertTrue(false);
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
    public void paymentTest() throws FullClassException, CardPaymentException {
        List<String> uIDs= new ArrayList<>();
        uIDs.add("1"); uIDs.add("2");
        Model model= new Model(uIDs, false);
        Player player = new Player("Gastani Frinzi", new Towers(ColourT.black, 1), model);

        try {
            player.pay(2);
            fail();
        } catch (CardPaymentException c){}

        for (int i = 0; i < 4; i++){
            player.addStudent(new Student(Colour.red));
        }

        player.pay(1);
    }

}