package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.ColourT;
import it.polimi.ingsw.Model.Exceptions.FullTowersException;
import it.polimi.ingsw.Model.Exceptions.RunOutOfTowersException;
import it.polimi.ingsw.Model.Towers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TowersTest {

    /**
     * In this test two sets of towers are created, one with 6 of them and one with 8 of them: this is to simulate
     * a game with 3 players and a game with 2/4 players. Then the quantities of towers are modified with the defined
     * methods and their correctness is checked with asserts.
     * Also all the exceptions are tested by using wrong values.
     */
    @Test
    public void towersTest() throws FullTowersException, RunOutOfTowersException {
        Towers towers0;
        Towers towers1;

        towers0=new Towers(ColourT.white,1);
        towers1=new Towers(ColourT.black,0);

        assertTrue(towers0.availabilityChecker()==6);
        assertTrue(towers1.availabilityChecker()==8);
        assertTrue((towers0.getColour()).equals(ColourT.white));
        assertTrue((towers1.getColour()).equals(ColourT.black));
        towers0.availabilityModifier(-2);
        towers1.availabilityModifier(-2);
        towers0.availabilityModifier(2);
        towers1.availabilityModifier(2);

        try{
            towers0.availabilityModifier(1);
            assertTrue(false);
        }catch (FullTowersException e){}

        try{
            towers1.availabilityModifier(-9);
            assertTrue(false);
        }catch (RunOutOfTowersException e){}

        towers1.availabilityModifier(8);

        try{
            towers1.availabilityModifier(1);
            assertTrue(false);
        }catch (FullTowersException e){}
    }

}