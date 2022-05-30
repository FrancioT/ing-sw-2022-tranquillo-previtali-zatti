package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Exceptions.FullEntranceException;
import it.polimi.ingsw.Model.Exceptions.RunOutOfStudentsException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class CloudTest {

    /**
     * This test tests all the methods of cloud by calling them and checking their functioning with assert methods.
     * It also tests if the exceptions are called correctly.
     */
    @Test
    public void cloudTest() throws FullEntranceException, IndexOutOfBoundsException, NullPointerException, RunOutOfStudentsException
    {
        Bag bag1;
        Towers towers1;
        Player player1;
        Cloud cloud1;

        bag1=new Bag();
        cloud1=new Cloud(bag1);
        towers1=new Towers(ColourT.white,1);
        player1=new Player("player1", towers1, null);

        cloud1.cloudFiller(5);
        assertEquals(cloud1.getStudentsColours().size(), 5);

        cloud1.cloudEmptier(player1);
        assertEquals(player1.getStudents().size(), 5);
        assertEquals(cloud1.getStudentsColours().size(), 0);

        try{
            cloud1.cloudFiller(-7);
            fail();
        }catch (IndexOutOfBoundsException e){}

        try{
            cloud1.cloudEmptier(null);
            fail();
        }catch (NullPointerException e1){}

        try{
            cloud1.cloudFiller(5);
            cloud1.cloudEmptier(player1);
            fail();
        }catch (FullEntranceException e){}

    }

    /**
     * test for printer methods
     */
    @Test
    public void printerTest() throws RunOutOfStudentsException
    {
        Cloud c= new Cloud(new Bag());
        c.cloudFiller(4);
        System.setOut(new PrintStream(new OutputStream()
            {
                public void close() {}
                public void flush() {}
                public void write(byte[] b) {}
                public void write(byte[] b, int off, int len) {}
                public void write(int b) {}
            }
        ));
        c.cloudPrinter();
    }
}