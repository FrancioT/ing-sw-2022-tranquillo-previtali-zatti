package it.polimi.ingsw;

import it.polimi.ingsw.Exceptions.FullEntranceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CloudTest {

    @Test
    public void cloudTest() throws FullEntranceException, IndexOutOfBoundsException, NullPointerException
    {
        Bag bag1;
        Towers towers1;
        Player player1;
        Cloud cloud1;

        bag1=new Bag();
        cloud1=new Cloud(bag1);
        towers1=new Towers(ColourT.white,1);
        player1=new Player("player1", towers1);

        cloud1.cloudFiller(5);
        cloud1.cloudEmptier(player1);

        try{
            cloud1.cloudFiller(-7);
            assertTrue(false);
        }catch (IndexOutOfBoundsException e){}

        try{
            cloud1.cloudEmptier(null);
            assertTrue(false);
        }catch (NullPointerException e1){}

        try{
            cloud1.cloudFiller(5);
            cloud1.cloudEmptier(player1);
            assertTrue(false);
        }catch (FullEntranceException e){}

    }
}