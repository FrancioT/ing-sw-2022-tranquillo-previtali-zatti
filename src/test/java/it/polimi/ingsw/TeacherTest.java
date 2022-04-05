package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    @Test
    public void teacherTest()
    {
        Teacher teacher1;
        Towers towers1;
        Towers towers2;
        Player player1;
        Player player2;

        teacher1=new Teacher(Colour.red);
        towers1=new Towers(ColourT.white,1);
        towers2=new Towers(ColourT.black,1);
        player1=new Player("Topo",towers1);
        player2=new Player("Gigio",towers2);

        teacher1.setNewPos(player1);
        assertTrue((teacher1.getCurrentPos()).equals(player1));
        assertTrue((teacher1.getColour()).equals(Colour.red));

        try{
            teacher1.setNewPos(null);
            assertTrue(false);
        }catch (NullPointerException e1){}

        teacher1.setNewPos(player2);
        assertTrue((teacher1.getCurrentPos()).equals(player2));
    }

}