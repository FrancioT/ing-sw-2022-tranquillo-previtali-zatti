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

        island1=new Island(true, null);
        island2=new Island(false, null);
        mN1=new MotherNature(island1);
        assertTrue(mN1.getCurrentPos()==island1);
        assertTrue(island1.isMotherNatureFlag());
        assertTrue(mN1.getCurrentPos() != island2);
        assertFalse(island2.isMotherNatureFlag());

        mN1.jumpNextPos(island2);

        assertTrue(mN1.getCurrentPos()==island2);
        assertTrue(island2.isMotherNatureFlag());
        assertTrue(mN1.getCurrentPos() != island1);
        assertFalse(island1.isMotherNatureFlag());
    }
}