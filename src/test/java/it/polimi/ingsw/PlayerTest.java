package it.polimi.ingsw;

import it.polimi.ingsw.Exceptions.EmptyException;
import it.polimi.ingsw.Exceptions.FullEntranceException;
import it.polimi.ingsw.Exceptions.NoSuchStudentException;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    public void testGetStudentsNum(){
        Player player = new Player("test", new Towers(ColourT.black, 1));
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
        Player player = new Player("test", new Towers(ColourT.black, 1));
        List<Student> students= new ArrayList<>();
        students.add(new Student(Colour.blue));
        player.entranceFiller(students);
        Colour colour = player.entranceEmptier(Colour.blue).getColour();
        if(colour.equals(Colour.blue))
            assertTrue(true);
        else
            assertTrue(false);
    }

}