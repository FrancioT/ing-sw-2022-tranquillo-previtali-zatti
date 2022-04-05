package it.polimi.ingsw;

import it.polimi.ingsw.Exceptions.EmptyException;
import it.polimi.ingsw.Exceptions.FullTowersException;
import it.polimi.ingsw.Exceptions.RunOutOfTowersException;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;

public class IslandTest extends TestCase
{
    @Test
    public void isTest() throws FullTowersException, RunOutOfTowersException, EmptyException
    {
        Island island1;
        Island island2;
        Bag bag=new Bag();
        island1=new Island(true);
        island2=new Island(bag.randomExtraction());
        assertTrue(island2.getStudentsColours().size()>0);
        assertFalse(island2.isMotherNatureFlag());
        assertFalse(island2.getInhibitionFlag());
        int pink_n=0, yellow_n=0, green_n=0, blue_n=0, red_n=0;
        List<Colour> tmp=island1.getStudentsColours();
        for(Colour c: tmp)
        {
            if(c==Colour.yellow) yellow_n++;
            if(c==Colour.green) green_n++;
            if(c==Colour.blue) blue_n++;
            if(c==Colour.red) red_n++;
            if(c==Colour.pink) pink_n++;
        }
        assertTrue(pink_n==0 && yellow_n==0 && green_n==0 && blue_n==0 && red_n==0);
        island1.addStudent(new Student(Colour.yellow));
        island1.addStudent(new Student(Colour.yellow));
        island1.addStudent(new Student(Colour.yellow));
        island1.addStudent(new Student(Colour.yellow));
        island1.addStudent(new Student(Colour.green));
        island1.addStudent(new Student(Colour.green));
        island1.addStudent(new Student(Colour.blue));
        island1.addStudent(new Student(Colour.red));
        island1.addStudent(new Student(Colour.red));
        island1.addStudent(new Student(Colour.red));
        island1.addStudent(new Student(Colour.red));
        island1.addStudent(new Student(Colour.red));
        tmp=island1.getStudentsColours();
        pink_n=0; yellow_n=0; green_n=0; blue_n=0; red_n=0;
        for(Colour c: tmp)
        {
            if(c==Colour.yellow) yellow_n++;
            if(c==Colour.green) green_n++;
            if(c==Colour.blue) blue_n++;
            if(c==Colour.red) red_n++;
            if(c==Colour.pink) pink_n++;
        }
        assertTrue(pink_n==0 && yellow_n==4 && green_n==2 && blue_n==1 && red_n==5);
        tmp=island2.getStudentsColours();
        pink_n=0; yellow_n=0; green_n=0; blue_n=0; red_n=0;
        for(Colour c: tmp)
        {
            if(c==Colour.yellow) yellow_n++;
            if(c==Colour.green) green_n++;
            if(c==Colour.blue) blue_n++;
            if(c==Colour.red) red_n++;
            if(c==Colour.pink) pink_n++;
        }
        assertTrue(pink_n<2 && yellow_n<2 && green_n<2 && blue_n<2
                   && red_n<2 && tmp.size()==1);
        assertEquals(island1.getNumTowers(), island2.getNumTowers());
        assertEquals(island1.getNumTowers(), 0);
        try {
            island1.getTowersColour();
            assertTrue(false);
        } catch (EmptyException e){}
        island1.towersSwitcher(new Towers(ColourT.black, 1));
        assertEquals(island1.getTowersColour(), ColourT.black);
        assertTrue(island1.isMotherNatureFlag());
        island1.setMotherNatureFlag();
        assertFalse(island1.isMotherNatureFlag());
        island1.setMotherNatureFlag();
        assertTrue(island1.isMotherNatureFlag());
        assertFalse(island1.getInhibitionFlag());
        island1.setInhibitionFlag(true);
        assertTrue(island1.getInhibitionFlag());
        island1.towersSwitcher(new Towers(ColourT.white, 1));
        assertEquals(island1.getTowersColour(), ColourT.white);
        try {
            island1.towersSwitcher(null);
            assertTrue(false);
        } catch (NullPointerException e){}
        // modifico il costruttore di towers per costruire il numero giusto di torri
        // in base alla modalità
        //testato tutto a meno di islandLinker e getNumTowers
    }
}
