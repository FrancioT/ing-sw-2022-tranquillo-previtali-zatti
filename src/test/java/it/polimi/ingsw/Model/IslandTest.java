package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;
import it.polimi.ingsw.Model.ModelAndDecorators.ModelTest;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest
{
    /**
     * This test tests all the setter methods of the class island by calling them to add students/towers/inhibition
     * tiles/mother nature; it checks their correctness with getter and assert methods.
     * It also tests all the possible exceptions by creating wrong situations.
     */
    @Test
    public void islTest() throws FullTowersException, RunOutOfTowersException,
            EmptyException, LinkFailedException, RunOutOfStudentsException {
        List<String> uIDs=new ArrayList<>();
        uIDs.add("Francio"); uIDs.add("Tarallo");
        Model model=new Model(uIDs, false);
        Island island1;
        Island island2;
        Bag bag=new Bag();
        island1=new Island(true);
        island2=new Island(bag.randomExtraction());
        assertTrue(island2.getStudentsColours().size()>0);
        assertFalse(island2.isMotherNatureFlag());
        assertFalse(island2.getInhibition());
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
        island1.removeMotherNature();
        assertFalse(island1.isMotherNatureFlag());
        island1.setMotherNature();
        assertTrue(island1.isMotherNatureFlag());
        assertFalse(island1.getInhibition());
        island1.addInhibition();
        assertTrue(island1.getInhibition());
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
        assertTrue(island3.getInhibition());
        island1.subInhibition();
        Island island4= island1.islandsLinker(island2);
        assertFalse(island4.getInhibition());
        assertEquals(island3.getStudentsColours(), finalList);
        try{
            island3.islandsLinker(null);
            assertTrue(false);
        } catch (NullPointerException e){}
        Island islandTmp=new Island(false);
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

    /**
     * This test creates two islands, adds some students and towers on them, and adds mother nature on one of the two.
     * Then, the method islandLinker is called and the test checks if the island have been merged in a single
     * one with the correct number of students and towers.
     * Also the possible exceptions are tested.
     */
    @Test
    public void testIslandLinker() throws Exception{
        List<String> uIDs=new ArrayList<>();
        uIDs.add("Francio"); uIDs.add("Tarallo");
        Model model=new Model(uIDs, false);
        Island island1;
        Island island2;
        Bag bag=new Bag();
        island1=new Island(true);
        island2=new Island(false);

        island1.towersSwitcher(ModelTest.getPlayers(model).get(0).getTowers());
        island2.towersSwitcher(ModelTest.getPlayers(model).get(0).getTowers());

        ColourT colourT = ModelTest.getPlayers(model).get(0).getTowers().getColour();

        for (int i=0; i<5; i++)
            island1.addStudent(new Student(Colour.red));

        for (int i=0; i<3; i++)
            island2.addStudent(new Student(Colour.green));

        List<Colour> studentsOnIsland1 = new ArrayList<>(island1.getStudentsColours());
        List<Colour> studentsOnIsland2 = new ArrayList<>(island2.getStudentsColours());

        assertEquals(studentsOnIsland1.size(), 5);
        assertEquals(studentsOnIsland2.size(), 3);

        Island island = island1.islandsLinker(island2);

        List<Colour> studentsOnIsland = new ArrayList<>(island.getStudentsColours());
        assertEquals(studentsOnIsland.size(), 8);
        assertTrue(island.isMotherNatureFlag());
        int numOfTowers = island.getNumTowers();

        assertEquals(numOfTowers, 2);
        assertTrue(island.getTowersColour() == colourT);

        try {
            island.subInhibition();
            fail();
        }catch (IllegalAccessError i){}
    }

    /**
     * test for printer methods
     */
    @Test
    public void printerTest() throws RunOutOfStudentsException, FullTowersException, RunOutOfTowersException, LinkFailedException {
        List<String> uIDs=new ArrayList<>();
        uIDs.add("Francio"); uIDs.add("Tarallo");
        Model model=new Model(uIDs, true);
        Island island= new Island(true);
        island.towersSwitcher(new Towers(ColourT.black, 0));
        System.setOut(new PrintStream(new OutputStream()
        {
            public void close() {}
            public void flush() {}
            public void write(byte[] b) {}
            public void write(byte[] b, int off, int len) {}
            public void write(int b) {}
        }
        ));
        island.islandPrinter(true);
    }

    /**
     * Tests the correct functioning of the custom serialization by serializing an island and checking if the
     * parameters are sent correctly.
     */
    @Test
    public void serializationTest() throws IOException, IOException, FullTowersException, RunOutOfTowersException, LinkFailedException {
        List<String> uIDs=new ArrayList<>();
        uIDs.add("Francio"); uIDs.add("Tarallo");
        Model model = new Model(uIDs, false);
        Island island= new Island(true);
        island.addStudent(new Student(Colour.pink));
        island.addStudent(new Student(Colour.red));
        island.towersSwitcher(new Towers(ColourT.black, 0));
        ServerSocket serverSocket= new ServerSocket(55132);
        Thread server= new Thread(){
            public void run()
            {
                Socket client;
                try {
                    client= serverSocket.accept();
                    ObjectInputStream in= new ObjectInputStream(client.getInputStream());
                    Object message= in.readObject();
                    Island receivedIsland= (Island)message;
                    assertFalse(receivedIsland.getInhibition());
                    assertTrue(receivedIsland.isMotherNatureFlag());
                    assertEquals(receivedIsland.getNumTowers(), 1);
                    assertEquals(receivedIsland.getInhibitionCounter(), 0);
                    assertEquals(receivedIsland.getStudentsColours().size(), 2);
                    assertTrue(receivedIsland.getStudentsColours().contains(Colour.red));
                    assertTrue(receivedIsland.getStudentsColours().contains(Colour.pink));
                }catch (Exception e) { throw new RuntimeException(e.toString()); }
            }
        };
        server.start();
        Socket socket= new Socket("127.0.0.1", 55132);
        ObjectOutputStream out= new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(island);
    }
}