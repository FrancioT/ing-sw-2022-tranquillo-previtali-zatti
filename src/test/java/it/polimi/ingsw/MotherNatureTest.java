package it.polimi.ingsw;

import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Model.MotherNature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MotherNatureTest {

    @Test
    public void motherNatureTest() throws NullPointerException
    {
        MotherNature mN1;
        Island island1;
        Island island2;

        island1=new Island(true);
        island2=new Island(false);
        mN1=new MotherNature(island1);
        assertTrue(mN1.getCurrentPos().equals(island1));
        assertTrue(island1.isMotherNatureFlag()==true);
        assertFalse(mN1.getCurrentPos().equals(island2));
        assertFalse(island2.isMotherNatureFlag()==true);

        mN1.jumpNextPos(island2);

        assertTrue(mN1.getCurrentPos().equals(island2));
        assertTrue(island2.isMotherNatureFlag()==true);
        assertFalse(mN1.getCurrentPos().equals(island1));
        assertFalse(island1.isMotherNatureFlag()==true);
    }
}