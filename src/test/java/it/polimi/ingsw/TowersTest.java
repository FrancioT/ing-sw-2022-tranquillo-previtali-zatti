package it.polimi.ingsw;

import it.polimi.ingsw.Model.ColourT;
import it.polimi.ingsw.Model.Exceptions.FullTowersException;
import it.polimi.ingsw.Model.Exceptions.RunOutOfTowersException;
import it.polimi.ingsw.Model.Towers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TowersTest {

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
            towers0.availabilityModifier(3);
            assertTrue(false);
        }catch (FullTowersException e){}

        try{
            towers1.availabilityModifier(-9);
            assertTrue(false);
        }catch (RunOutOfTowersException e){}

    }

}