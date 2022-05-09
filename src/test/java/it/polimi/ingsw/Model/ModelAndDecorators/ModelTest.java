package it.polimi.ingsw.Model.ModelAndDecorators;

import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.CharacterCard.CharacterCard;
import it.polimi.ingsw.Model.CharacterCard.CharacterCard3;
import it.polimi.ingsw.Model.CharacterCard.CharacterCard4;
import it.polimi.ingsw.Model.CharacterCard.CharacterCard5;
import it.polimi.ingsw.Model.Exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ModelTest
{
    Model model;
    final private static String P1="Francio";
    final private static String P2="Tarallo";

    @BeforeEach
    void initialization() throws Exception
    {
        List<String> uIDs=new ArrayList<>();
        uIDs.add(P1);
        uIDs.add(P2);
        model= new Model(uIDs, false);
        List<Colour> colourList=model.getStudents(P1);
        for(Colour c: colourList)
            model.entranceEmptier(P1, c);
        colourList=model.getStudents(P2);
        for(Colour c: colourList)
            model.entranceEmptier(P2, c);
        assertEquals(model.getStudents(P1).size(), 0);
        assertEquals(model.getStudents(P2).size(), 0);
    }

    @Test
    void payCard() throws Exception
    {
        List<String> uIDs=new ArrayList<>();
        uIDs.add(P1);
        uIDs.add(P2);
        Model model2= new Model(uIDs, true);
        assertThrows(NullPointerException.class, () -> model2.payCard(null, 0));
        assertThrows(NoSuchCardException.class, () -> model2.payCard(P1, 100));
        assertThrows(NoSuchPlayerException.class, () -> model2.payCard("ttt", 1));
        model2.characterCardList.set(0, new CharacterCard3());
        assertThrows(CardPaymentException.class, () -> model2.payCard(P1, 3));
        assertThrows(IllegalAccessError.class, () -> model.payCard(P1, 1));
        for(int i=0; i<10; i++)
            model2.addStudentDashboard(P1, new Student(Colour.yellow));
        model2.payCard(P1, model2.characterCardList.get(0).getCardID());
        model2.characterCardList.set(1, new CharacterCard4());
        assertTrue(model2.checkEnoughMoney(P1, 4));
        assertFalse(model2.checkEnoughMoney(P1, 3));
        assertThrows(NullPointerException.class, () -> model2.checkEnoughMoney(null, 1));
        assertThrows(NoSuchPlayerException.class, () -> model2.checkEnoughMoney("ttt", 1));
        assertThrows(NoSuchCardException.class, () -> model2.checkEnoughMoney(P1, -1));
    }

    @Test
    void teacherDominance() throws TooManyTeachersException, TeacherAlreadyInException,
            NoSuchTeacherException, NoSuchPlayerException, FullClassException
    {
        try {
            model.teacherDominance(null, null);
            assertTrue(false);
        } catch (NullPointerException e){}

        model.teacherDominance(model.playersList.get(0), Colour.pink);
        assertFalse(model.playersList.get(0).checkTeacherPresence(Colour.pink));
        for(Teacher t: model.teachersList)
            if(t.getColour()==Colour.pink)
                assertNull(t.getCurrentPos());
        model.addStudentDashboard(P2, new Student(Colour.yellow));
        assertTrue(model.playersList.get(1).checkTeacherPresence(Colour.yellow));
        for(Teacher t: model.teachersList)
            if(t.getColour()==Colour.yellow)
                assertEquals(t.getCurrentPos(), model.playersList.get(1));
        model.addStudentDashboard(P1, new Student(Colour.yellow));
        assertTrue(model.playersList.get(1).checkTeacherPresence(Colour.yellow));
        for(Teacher t: model.teachersList)
            if(t.getColour()==Colour.yellow)
                assertEquals(t.getCurrentPos(), model.playersList.get(1));
        model.addStudentDashboard(P1, new Student(Colour.yellow));
        assertTrue(model.playersList.get(0).checkTeacherPresence(Colour.yellow));
        assertFalse(model.playersList.get(1).checkTeacherPresence(Colour.yellow));
        for(Teacher t: model.teachersList)
            if(t.getColour()==Colour.yellow)
                assertEquals(t.getCurrentPos(), model.playersList.get(0));
    }

    @Test
    void addStudentDashboard() throws Exception
    {
        model.addStudentDashboard(P1, new Student(Colour.green));
        try {
            model.addStudentDashboard(null, null);
            assertTrue(false);
        } catch (NullPointerException e){}
        try {
            model.addStudentDashboard("aaa", new Student(Colour.yellow));
            assertTrue(false);
        } catch (NoSuchPlayerException e){}
    }

    @Test
    void addStudentIsland()
    {
        model.addStudentIsland(0, new Student(Colour.pink));
        model.addStudentIsland(11, new Student(Colour.blue));
        try {
            model.addStudentIsland(-1, new Student(Colour.red));
            assertTrue(false);
        } catch (IndexOutOfBoundsException e){}
        try {
            model.addStudentIsland(12, new Student(Colour.yellow));
            assertTrue(false);
        } catch (IndexOutOfBoundsException e){}
    }

    @Test
    void cloudsFill_empty() throws FullEntranceException,
            NoSuchPlayerException
    {
        model.cloudsFiller(3);
        try {
            model.cloudEmptier(P1,-1);
            assertTrue(false);
        } catch (IndexOutOfBoundsException e){}
        try {
            model.cloudEmptier(P1,4);
            assertTrue(false);
        } catch (IndexOutOfBoundsException e){}
        try {
            model.cloudEmptier(null,100);
            assertTrue(false);
        } catch (NullPointerException e){}
        try {
            model.cloudEmptier("aaa",-1);
            assertTrue(false);
        } catch (IndexOutOfBoundsException e){}
        try {
            model.cloudEmptier("Zatti",0);
            assertTrue(false);
        } catch (NoSuchPlayerException e){}
        int i=0;
        List<Colour> colourList=new ArrayList<>();
        for(Cloud c: model.cloudsList)
        {
            assertTrue(c.getStudentsColours().size()==3);
            colourList.addAll(c.getStudentsColours());
            model.cloudEmptier(P1, i);
            i++;
        }
        for(Cloud c: model.cloudsList)
            assertTrue(c.getStudentsColours().size()==0);

        assertTrue(colourList.containsAll(model.getStudents(P1)));
        assertEquals(colourList.size(), model.getStudents(P1).size());
    }


    @Test
    void cardDiscarder() throws NoSuchPlayerException
    {
        model.cardDiscarder(P1, 3);
        try {
            model.cardDiscarder(null, 9);
            assertTrue(false);
        } catch (NullPointerException e){}
        try {
            model.cardDiscarder("abab", 8);
            assertTrue(false);
        } catch (NoSuchPlayerException e){}
    }

    @Test
    void entranceEmptier() throws EmptyException, FullEntranceException,
            NoSuchStudentException, NoSuchPlayerException
    {
        try {
            model.getStudents(null);
            assertTrue(false);
        } catch (NullPointerException e){}
        try {
            model.getStudents("sgds");
            assertTrue(false);
        } catch (NoSuchPlayerException e){}
        model.cloudsFiller(3);
        model.cloudEmptier(P2, 0);
        model.cloudEmptier(P2, 1);
        List<Colour> tmp= model.getStudents(P2);
        model.entranceEmptier(P2, tmp.get(0));
        tmp.remove(0);
        for(Colour c: model.getStudents(P2))
            tmp.remove(c);

        assertTrue(tmp.size()==0);

        try {
            model.entranceEmptier(null, null);
            assertTrue(false);
        } catch (NullPointerException e){}
        try {
            model.entranceEmptier("ekfir", null);
        } catch (NoSuchPlayerException e){}
    }

    @Test
    void getCurrPosMN() throws Exception
    {
        int pos= model.getCurrPosMN();
        assertTrue(pos==0);
        model.moveMN(4);
        pos= model.getCurrPosMN();
        assertTrue(pos==4);
        model.moveMN(10);
        pos= model.getCurrPosMN();
        assertTrue(pos==2);
    }


    @Test
    void moveMN() throws Exception
    {
        try {
            model.moveMN(-5);
            assertTrue(false);
        } catch (IndexOutOfBoundsException e){}

        model.addStudentDashboard(P1, new Student(Colour.red));
        assertTrue(model.playersList.get(0).checkTeacherPresence(Colour.red));
        model.addStudentDashboard(P1, new Student(Colour.red));
        model.addStudentDashboard(P1, new Student(Colour.red));
        assertTrue(model.playersList.get(0).checkTeacherPresence(Colour.red));
        model.addStudentDashboard(P1, new Student(Colour.blue));
        model.addStudentDashboard(P1, new Student(Colour.blue));
        model.addStudentDashboard(P1, new Student(Colour.pink));
        assertTrue(model.playersList.get(0).checkTeacherPresence(Colour.blue));
        assertTrue(model.playersList.get(0).checkTeacherPresence(Colour.pink));
        model.addStudentDashboard(P2, new Student(Colour.red));
        assertFalse(model.playersList.get(1).checkTeacherPresence(Colour.red));
        model.addStudentDashboard(P2, new Student(Colour.pink));
        assertFalse(model.playersList.get(1).checkTeacherPresence(Colour.pink));
        model.addStudentDashboard(P2, new Student(Colour.pink));
        assertTrue(model.playersList.get(1).checkTeacherPresence(Colour.pink));
        model.addStudentDashboard(P2, new Student(Colour.green));
        assertTrue(model.playersList.get(1).checkTeacherPresence(Colour.green));

        model.addStudentIsland(3, new Student(Colour.red));
        model.addStudentIsland(3, new Student(Colour.red));
        model.addStudentIsland(3, new Student(Colour.pink));
        model.addStudentIsland(3, new Student(Colour.pink));
        model.addStudentIsland(3, new Student(Colour.green));
        model.addStudentIsland(3, new Student(Colour.blue));
        model.addStudentIsland(3, new Student(Colour.blue));
        model.addStudentIsland(3, new Student(Colour.blue));
        model.moveMN(3);
        assertEquals(model.islandsList.get(3).getTowersColour(),
                     model.playersList.get(0).getTowers().getColour());
        model.addStudentIsland(3, new Student(Colour.green));
        model.addStudentIsland(3, new Student(Colour.green));
        model.addStudentIsland(3, new Student(Colour.green));
        model.addStudentIsland(3, new Student(Colour.pink));
        model.addStudentIsland(3, new Student(Colour.pink));
        model.moveMN(12);
        assertEquals(model.islandsList.get(3).getTowersColour(),
                     model.playersList.get(1).getTowers().getColour());

        model.addStudentIsland(6, new Student(Colour.red));
        model.addStudentIsland(6, new Student(Colour.red));
        model.addStudentIsland(6, new Student(Colour.pink));
        model.addStudentIsland(6, new Student(Colour.pink));
        model.addStudentIsland(6, new Student(Colour.pink));
        model.addStudentIsland(6, new Student(Colour.green));
        model.addStudentIsland(6, new Student(Colour.blue));
        model.addStudentIsland(6, new Student(Colour.blue));
        model.addStudentIsland(6, new Student(Colour.blue));
        model.moveMN(3);
        assertEquals(model.islandsList.get(6).getTowersColour(),
                model.playersList.get(0).getTowers().getColour());
        model.addStudentIsland(6, new Student(Colour.green));
        model.addStudentIsland(6, new Student(Colour.green));
        model.moveMN(12);
        assertEquals(model.islandsList.get(6).getTowersColour(),
                model.playersList.get(0).getTowers().getColour());
        model.addStudentIsland(6, new Student(Colour.pink));
        model.moveMN(12);
        assertEquals(model.islandsList.get(6).getTowersColour(),
                model.playersList.get(1).getTowers().getColour());
        assertEquals(model.playersList.get(0).getTowers().availabilityChecker(),
                     model.playersList.get(1).getTowers().availabilityChecker()+2);

        //testing of islandLinker
        model.addStudentIsland(4, new Student(Colour.green));
        model.addStudentIsland(4, new Student(Colour.green));
        model.moveMN(10);
        assertEquals(model.islandsList.size(), 11);
        assertEquals(model.islandsList.get(3).getNumTowers(), 2);
        model.addStudentIsland(4, new Student(Colour.pink));
        model.addStudentIsland(4, new Student(Colour.pink));
        model.moveMN(1);
        assertEquals(model.islandsList.size(), 9);
        assertEquals(model.islandsList.get(3).getNumTowers(), 4);
        model.addStudentIsland(0, new Student(Colour.red));
        model.moveMN(6);
        assertEquals(model.islandsList.get(0).getTowersColour(),
                     model.playersList.get(0).getTowers().getColour());
        assertEquals(model.islandsList.get(0).getNumTowers(), 1);
        model.addStudentIsland(8, new Student(Colour.red));
        model.addStudentIsland(8, new Student(Colour.red));
        model.moveMN(8);
        assertEquals(model.islandsList.size(), 8);
        assertEquals(model.islandsList.get(3).getNumTowers(), 4);
        assertEquals(model.islandsList.get(3).getTowersColour(),
                     model.playersList.get(1).getTowers().getColour());
        assertEquals(model.islandsList.get(0).getTowersColour(),
                model.playersList.get(0).getTowers().getColour());
        assertEquals(model.islandsList.get(0).getNumTowers(), 2);

        // testing islandDominance()
        assertThrows(IllegalArgumentException.class, () -> model.islandDominance(model.islandsList.get(2)));
    }

    @Test
    void getLastCardValue() throws NoSuchPlayerException, EmptyException
    {
        try {
            model.getLastCardValue(null);
            assertTrue(false);
        } catch (NullPointerException e){}
        try {
            model.getLastCardValue("rowuq");
            assertTrue(false);
        } catch (NoSuchPlayerException e){}
        try {
            model.getLastCardValue(P2);
            assertTrue(false);
        } catch (EmptyException e){}
        model.cardDiscarder(P1, 3);
        assertTrue(model.getLastCardValue(P1)==2);
    }

    private class CardTMP extends CharacterCard5{
        @Override
        public void giveBackInhibitionFlag(){}
    }
    @Test
    void inhibitionFlag() throws Exception
    {
        CharacterCard5 card5= new CardTMP();
        model.addInhibition(6, card5);

        model.addStudentIsland(6, new Student(Colour.red));
        model.addStudentDashboard(P1, new Student(Colour.red));
        assertTrue(model.islandsList.get(6).getInhibition());
        model.moveMN(6);
        assertFalse(model.islandsList.get(6).getInhibition());
        assertEquals(model.islandsList.get(6).getNumTowers(), 0);
        assertThrows(NullPointerException.class, () -> model.addInhibition(0, null));
        assertThrows(IndexOutOfBoundsException.class, () -> model.addInhibition(-1, card5));
        assertThrows(IndexOutOfBoundsException.class, () -> model.addInhibition(12, card5));
    }

    @Test
    void entranceFiller()
    {
        List<Student> students=new ArrayList<>();
        assertThrows(NullPointerException.class, () -> model.entranceFiller(null, null));
        assertThrows(NullPointerException.class, () -> model.entranceFiller(P1, null));
        assertThrows(NoSuchPlayerException.class, () -> model.entranceFiller(" ", students));
    }

    @Test
    void studentsSwap() throws Exception
    {
        List<Student> students= new ArrayList<>();
        students.add(new Student(Colour.red));
        students.add(new Student(Colour.pink));
        students.add(new Student(Colour.yellow));
        assertThrows(NullPointerException.class, () -> model.studentsSwap(null, null, null));
        assertThrows(NoSuchPlayerException.class, () -> model.studentsSwap(" ", Colour.pink, Colour.red));
        model.entranceFiller(P1, students);
        model.addStudentDashboard(P1, new Student(Colour.blue));
        model.studentsSwap(P1, Colour.pink, Colour.blue);
        assertTrue(model.getStudents(P1).contains(Colour.red));
        assertTrue(model.getStudents(P1).contains(Colour.yellow));
        assertTrue(model.getStudents(P1).contains(Colour.blue));
        assertFalse(model.getStudents(P1).contains(Colour.pink));
        assertEquals(model.getStudents(P1).size(), 3);
        assertEquals(model.playersList.get(0).getStudentNum(Colour.pink), 1);
        assertEquals(model.playersList.get(0).getStudentNum(Colour.blue), 0);
    }

    public static void changeCard(Model model, List<CharacterCard> cards)
    {
        if(model.characterCardList.size()==0)
            throw new IllegalAccessError("The passed model is not in expert mode!");
        model.characterCardList.clear();
        model.characterCardList.addAll(cards);
    }
    public static List<Player> getPlayers(Model model) { return new ArrayList<>(model.playersList); }
    public static int getUnusedCoins(Model model) { return model.unusedCoins; }
    public static List<Teacher> getTeachersList(Model model) { return new ArrayList<>(model.teachersList); }
    public static List<Island> getIslandsList(Model model) { return new ArrayList<>(model.islandsList); }
    public static List<Cloud> getCloudsList(Model model) { return new ArrayList<>(model.cloudsList); }
    public static List<CharacterCard> getCharacterCardList(Model model) { return new ArrayList<>(model.characterCardList); }
}