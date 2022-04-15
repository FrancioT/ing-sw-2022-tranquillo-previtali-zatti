package it.polimi.ingsw;

import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Exceptions.EmptyException;
import it.polimi.ingsw.Model.Exceptions.FullTowersException;
import it.polimi.ingsw.Model.Exceptions.LinkFailedException;
import it.polimi.ingsw.Model.Exceptions.RunOutOfTowersException;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest
{
    @Test
    public void islTest() throws FullTowersException, RunOutOfTowersException,
                                EmptyException, LinkFailedException
    {
        List<String> uIDs=new ArrayList<>();
        uIDs.add("Francio"); uIDs.add("Tarallo");
        Model model=new Model(uIDs, false);
        Island island1;
        Island island2;
        Bag bag=new Bag();
        island1=new Island(true, model);
        island2=new Island(bag.randomExtraction(), model);
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
        island2.towersSwitcher(new Towers(ColourT.white, 1));
        island2.addStudent(new Student(Colour.green));
        List<Colour> finalList= island1.getStudentsColours();
        finalList.addAll(island2.getStudentsColours());
        Island island3= island1.islandsLinker(island2);
        assertEquals(island3.getTowersColour(), ColourT.white);
        assertEquals(island3.getNumTowers(), 2);
        assertTrue(island3.isMotherNatureFlag());
        assertTrue(island3.getInhibitionFlag());
        island1.setInhibitionFlag(false);
        Island island4= island1.islandsLinker(island2);
        assertFalse(island4.getInhibitionFlag());
        assertEquals(island3.getStudentsColours(), finalList);
        try{
            island3.islandsLinker(null);
            assertTrue(false);
        } catch (NullPointerException e){}
        Island islandTmp=new Island(false, model);
        try{
            island3.islandsLinker(islandTmp);
            assertTrue(false);
        } catch (LinkFailedException e){}
        islandTmp.towersSwitcher(new Towers(ColourT.grey, 1));
        try{
            island3.islandsLinker(islandTmp);
            assertTrue(false);
        } catch (LinkFailedException e){}
    }
}