package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Exceptions.FullEntranceException;
import it.polimi.ingsw.Model.Exceptions.RunOutOfStudentsException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CloudTest {

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
}