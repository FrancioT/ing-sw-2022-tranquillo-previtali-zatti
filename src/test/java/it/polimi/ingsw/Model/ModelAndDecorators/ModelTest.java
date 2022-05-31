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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ModelTest
{
    Model model;
    final private static String P1="Francio";
    final private static String P2="Tarallo";

    /**
     * A model is created and a basic check is done
     */
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

    /**
     * This test checks the correctness of all exception in payment by forcing incorrect situations.
     * It also tests the correctness of a valid payment and if the coins are correctly decreased.
     */
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

    /**
     * This test tests the correctness of the method that is called whenever a student is added or removed in a
     * classroom. It controls whether the teacher is added or removed and checks its correctness with assert methods.
     */
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

    /**
     * This test tests specifically the method which adds student in a dashboard.
     * It checks the correct addition of a student and the exceptions which it can throw.
     */
    @Test
    void addStudentDashboard() throws Exception
    {
        model.addStudentDashboard(P1, new Student(Colour.green));
        assertEquals(model.getStudentsNum(P1, Colour.green), 1);
        try {
            model.addStudentDashboard(null, null);
            assertTrue(false);
        } catch (NullPointerException e){}
        try {
            model.addStudentDashboard("aaa", new Student(Colour.yellow));
            assertTrue(false);
        } catch (NoSuchPlayerException e){}
    }

    /**
     * Tests the specific method to add a student to an island. It checks if it's added correctly or if it throws
     * an exception.
     */
    @Test
    void addStudentIsland()
    {
        model.addStudentIsland(0, new Student(Colour.pink));
        assertEquals(ModelTest.getIslandsList(model).get(0).getStudentsColours().size(), 1);
        try {
            model.addStudentIsland(-1, new Student(Colour.red));
            assertTrue(false);
        } catch (IndexOutOfBoundsException e){}
        try {
            model.addStudentIsland(12, new Student(Colour.yellow));
            assertTrue(false);
        } catch (IndexOutOfBoundsException e){}
    }


    /**
     * Tests the cloudFiller and cloudEmptier methods with assert methods.
     * It also checks the correct functioning of the exceptions.
     */
    @Test
    void cloudsFill_empty() throws FullEntranceException, NoSuchPlayerException, RunOutOfStudentsException
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


    /**
     * Test the cardDiscarder method and controls that the correct value has been saved.
     * It also tests the correct throw of the exceptions.
     */
    @Test
    void cardDiscarder() throws NoSuchPlayerException, EmptyException {
        model.cardDiscarder(P1, 3);
        assertEquals(model.getLastCardValue(P1), 2);
        try {
            model.cardDiscarder(null, 9);
            assertTrue(false);
        } catch (NullPointerException e){}
        try {
            model.cardDiscarder("abab", 8);
            assertTrue(false);
        } catch (NoSuchPlayerException e){}
    }

    /**
     * Tests the entranceEmptier method by adding known students to the entrance, emptying it and checking
     * the correctness of the students removed.
     * It also checks the correctness of the exceptions.
     */
    @Test
    void entranceEmptier() throws EmptyException, FullEntranceException,
            NoSuchStudentException, NoSuchPlayerException, RunOutOfStudentsException
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

    /**
     * Tests the correctness of the method that gets MN's position by moving it and then checking its position
     * with assert methods.
     */
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


    /**
     * In this test is tested in first instance whether MN is moved on the correct island; then is tested if towers
     * have been swapped correctly and finally if islands have been linked correctly.
     * It also checks if all the exceptions are thrown correctly.
     */
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

    /**
     * It checks if the lastCardValue is correct with assert methods.
     * It also tests all the possible exceptions.
     */
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

    /**
     * Tests the correct addition, functioning and removal of the inhibition tiles by adding it to an island,
     * moving MN on it, checking the towers and then controlling that the tile is not anymore on the island.
     * It also tests all the exceptions.
     */
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

    /**
     * Test the correct functioning of the method entranceFiller by checking the number of students before and after
     * its call.
     * It also tests all its exceptions.
     */
    @Test
    void entranceFiller() throws NoSuchPlayerException, FullEntranceException {
        List<Student> students=new ArrayList<>();
        assertThrows(NullPointerException.class, () -> model.entranceFiller(null, null));
        assertThrows(NullPointerException.class, () -> model.entranceFiller(P1, null));
        assertThrows(NoSuchPlayerException.class, () -> model.entranceFiller(" ", students));
        students.add(new Student(Colour.red));
        model.entranceFiller(P1, students);
        assertEquals(model.getStudents(P1).size(), 1);
    }

    /**
     * Tests the correct students swap between classroom and entrance by setting known students in both of them;
     * it tests the swap using assert methods.
     * It also tests all the exceptions.
     */
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

    /**
     * Tests if two islands are linked in a single one by placing known students on them, and also
     * by placing teachers in particular players' dashboard so that we know that the islands need to be linked.
     * It's checked that all the students, towers and mother nature are moved on the new island.
     * Tests also all the exceptions.
     */
    @Test
    public void checkIslandLinkingTest() throws Exception{
        for(int i=0; i<5; i++){
            model.islandsList.get(5).addStudent(new Student(Colour.red));
            model.islandsList.get(5).addStudent(new Student(Colour.green));
            model.islandsList.get(6).addStudent(new Student(Colour.green));
        }

        model.islandsList.get(5).addStudent(new Student(Colour.green));

        ColourT colourT1 = model.playersList.get(0).getTowers().getColour();
        ColourT colourT2 = model.playersList.get(1).getTowers().getColour();

        assertTrue(colourT1 != colourT2);

        Map<Colour, Integer> coloursMap=new HashMap<>();

        for(Colour c: Colour.values())
            coloursMap.put(c, Integer.valueOf(0));

        for(Colour c: model.islandsList.get(5).getStudentsColours())
        {
            coloursMap.replace(c, coloursMap.get(c)+1);
        }

        int expectedGreens = coloursMap.get(Colour.green) + 5;

        model.islandsList.get(5).towersSwitcher(getPlayers(model).get(0).getTowers());
        model.islandsList.get(6).towersSwitcher(getPlayers(model).get(0).getTowers());

        assertEquals(model.islandsList.get(5).getNumTowers(), 2);
        assertEquals(model.islandsList.get(5).getStudentsColours().size(), 17);
        assertTrue(model.motherNature.getCurrentPos() == getIslandsList(model).get(5));
        assertTrue(getIslandsList(model).get(5).isMotherNatureFlag());

        assertTrue(model.islandsList.get(5).getTowersColour() == colourT1);

        for(Colour c: Colour.values())
            coloursMap.put(c, Integer.valueOf(0));

        for(Colour c: model.islandsList.get(5).getStudentsColours())
        {
            coloursMap.replace(c, coloursMap.get(c)+1);
        }

        int effectiveGreens = coloursMap.get(Colour.green);

        assertEquals(effectiveGreens, expectedGreens);

        for(Colour c: Colour.values())
            coloursMap.put(c, Integer.valueOf(0));

        for(Colour c: model.islandsList.get(6).getStudentsColours())
        {
            coloursMap.replace(c, coloursMap.get(c)+1);
        }

        expectedGreens = expectedGreens + coloursMap.get(Colour.green);

        for(int i=0; i<5; i++){
        model.islandsList.get(6).addStudent(new Student(Colour.blue));
        }

        model.islandsList.get(6).towersSwitcher(getPlayers(model).get(0).getTowers());

        assertEquals(model.islandsList.get(5).getStudentsColours().size(), 23);

        for(Colour c: Colour.values())
            coloursMap.put(c, Integer.valueOf(0));

        for(Colour c: model.islandsList.get(5).getStudentsColours())
        {
            coloursMap.replace(c, coloursMap.get(c)+1);
        }

        effectiveGreens = coloursMap.get(Colour.green);

        assertEquals(expectedGreens, effectiveGreens);
        assertEquals(model.islandsList.get(5).getNumTowers(), 3);
        assertTrue(model.islandsList.get(5).getTowersColour() == colourT1);

        model.endGame();

        try {
            model.activateCard(null, null, null);
            fail();
        }catch (NullPointerException n){}

        try {
            model.notify(null);
            fail();
        }catch (NullPointerException n){}
    }

    /**
     * This method is fundamental because we need to force specific character cards to be in the game for testing.
     * What this method does is removing the randomly extracted charactercards and adding the ones
     * specified in the list.
     * @param model the model in which the cc need to be added
     * @param cards the cards that need to be added
     */
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
    public static MotherNature getMN(Model model) {return model.motherNature;}
}