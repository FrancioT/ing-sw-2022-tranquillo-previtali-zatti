package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Exceptions.ConnectionErrorException;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Exceptions.FullEntranceException;
import it.polimi.ingsw.Model.Exceptions.NoSuchPlayerException;
import it.polimi.ingsw.Model.ModelAndDecorators.ModelTest;
import it.polimi.ingsw.RemoteView.RemoteView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ControllerTest
{
    Controller controller;
    Map<String, DataBuffer> users;
    static String p1= "Francio";
    static String p2= "Zatto";
    static String p3= "Premortis";
    @BeforeEach
    void initialization()
    {
        users= new HashMap<>();
        users.put(p1, new DataBuffer(p1));
        users.put(p2, new DataBuffer(p2));
        users.put(p3, new DataBuffer(p3));
        controller=new Controller(users, true, new ArrayList<RemoteView>());
    }

    /**
     * In this test is tested the cloudsFilling method; it is called with the controller and then, its correctness
     * is validated with an assert method.
     */
    @Test
    void cloudsFilling() {
        controller.cloudsFilling();
        assertTrue(ModelTest.getCloudsList(controller.getModel()).get(0).getStudentsColours().size() == 4);
    }

    /**
     * In this test we simulate a round of the cardsPhase, where every player plays a card.
     * Everything is simulated with a thread and with sleep methods that allow us to simulate also a possible player's
     * wrong move, which would be advised with a println and then changes his move.
     * Everything is checked with assert methods.
     */
    @Test
    void cardsPhase() throws Exception {
        Thread thread= new Thread(() -> {
            while (true)
            {
                users.get(p2).setCardPos(1);
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p3).setCardPos(8);
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p1).setCardPos(15);
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p1).setCardPos(8);
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p1).setCardPos(4);
            }
        });
        thread.start();
        controller.cardsPhase();
        assertTrue(ControllerTest.getUIDsList(controller).get(0) == p2);
        assertTrue(ControllerTest.getUIDsList(controller).get(1) == p1);
        assertTrue(ControllerTest.getUIDsList(controller).get(2) == p3);
        thread.interrupt();
    }

    /**
     * In this test we simulate a match where all the players arrive at the last round with the same card. This is a
     * very rare corner case which we wanted to test because in normal conditions the controller would refuse
     * the same card played by different players; although, in this particular case it has to accept it.
     * This particular situation is tested by playing cards in a particular order and then with assert methods.
     */
    @Test
    void testFinishedCards() throws Exception{
        for(int i=1; i<8; i++){
            users.get(p1).setCardPos(1);
            users.get(p2).setCardPos(2);
            users.get(p3).setCardPos(3);
            controller.cardsPhase();
        }

        users.get(p1).setCardPos(1);
        users.get(p2).setCardPos(2);
        users.get(p3).setCardPos(1);
        controller.cardsPhase();

        users.get(p1).setCardPos(1);
        users.get(p2).setCardPos(1);
        users.get(p3).setCardPos(1);
        controller.cardsPhase();

        users.get(p1).setCardPos(0);
        users.get(p2).setCardPos(0);
        users.get(p3).setCardPos(0);
        controller.cardsPhase();

        assertTrue(ModelTest.getPlayers(controller.getModel()).get(0).getHandCards().size() == 0);
        assertTrue(ModelTest.getPlayers(controller.getModel()).get(1).getHandCards().size() == 0);
        assertTrue(ModelTest.getPlayers(controller.getModel()).get(2).getHandCards().size() == 0);
        assertTrue(ModelTest.getPlayers(controller.getModel()).get(0).getLastCardMNValue() == 1);
        assertTrue(ModelTest.getPlayers(controller.getModel()).get(1).getLastCardMNValue() == 1);
        assertTrue(ModelTest.getPlayers(controller.getModel()).get(2).getLastCardMNValue() == 1);
        assertTrue(controller.getModel().getLastCardValue(p1) == 1);
        assertTrue(controller.getModel().getLastCardValue(p2) == 1);
        assertTrue(controller.getModel().getLastCardValue(p3) == 1);
    }

    /**
     * In this test we check the correct functioning of the MoveMN method.
     * In first instance we move MN in correct islands, then with a thread we simulate a player which tries every
     * possible move that can create problems (e.g. a value higher than the card he played, a negative movement,
     * a movement on the same island). Everything is always checked with assert methods.
     * Finally, we also simulate the end of a game where one player finishes his towers.
     */
    @Test
    void moveMN() throws Exception
    {
        assertTrue(ModelTest.getMN(controller.getModel()).getCurrentPos() == ModelTest.getIslandsList(controller.getModel()).get(0));

        users.get(p1).setCardPos(9);
        users.get(p2).setCardPos(8);
        users.get(p3).setCardPos(7);
        controller.cardsPhase();

        users.get(p1).setMnPos(5);
        users.get(p2).setMnPos(10);

        controller.moveMN(p1);
        assertTrue(ModelTest.getMN(controller.getModel()).getCurrentPos() == ModelTest.getIslandsList(controller.getModel()).get(5));

        controller.moveMN(p2);
        assertTrue(ModelTest.getMN(controller.getModel()).getCurrentPos() == ModelTest.getIslandsList(controller.getModel()).get(10));


        Thread thread= new Thread(() -> {
            while (true)
            {
                users.get(p3).setCharacterCardID(6);
                users.get(p3).activationCardRequest();
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p3).setMnPos(53);
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p3).setMnPos(10);
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p3).setMnPos(-5);
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p3).setMnPos(1);
            }
        });
        thread.start();
        controller.moveMN(p3);
        assertTrue(ModelTest.getMN(controller.getModel()).getCurrentPos() == ModelTest.getIslandsList(controller.getModel()).get(1));
        thread.interrupt();

        //test runoutoftowers
        ModelTest.getPlayers(controller.getModel()).get(0).addTeacher(new Teacher(Colour.green));
        ModelTest.getPlayers(controller.getModel()).get(0).getTowers().availabilityModifier(-5);
        ModelTest.getIslandsList(controller.getModel()).get(2).addStudent(new Student(Colour.green));
        ModelTest.getIslandsList(controller.getModel()).get(2).addStudent(new Student(Colour.green));
        ModelTest.getIslandsList(controller.getModel()).get(2).addStudent(new Student(Colour.green));

        users.get(p1).setCardPos(5);
        users.get(p2).setCardPos(7);
        users.get(p3).setCardPos(1);
        controller.cardsPhase();

        users.get(p3).setMnPos(2);
        controller.moveMN(p3);
    }

    /**
     * In this case we test a situation that should never occur: a player has more towers than he should have.
     * In this case we modify the number of towers manually, and then we try to give back an extra tower from an MN
     * movement. In this case the game ends and this is checked with an exception.
     */
    @Test
    void toomanytowerstest() throws Exception{
        ModelTest.getPlayers(controller.getModel()).get(1).addTeacher(new Teacher(Colour.red));
        ModelTest.getIslandsList(controller.getModel()).get(2).addStudent(new Student(Colour.red));
        ModelTest.getIslandsList(controller.getModel()).get(2).addStudent(new Student(Colour.red));
        ModelTest.getIslandsList(controller.getModel()).get(2).addStudent(new Student(Colour.red));
        ModelTest.getIslandsList(controller.getModel()).get(2).towersSwitcher(ModelTest.getPlayers(controller.getModel()).get(2).getTowers());
        ModelTest.getPlayers(controller.getModel()).get(2).getTowers().availabilityModifier( 8 - ModelTest.getPlayers(controller.getModel()).get(2).getTowers().availabilityChecker());

        users.get(p1).setCardPos(3);
        users.get(p2).setCardPos(8);
        users.get(p3).setCardPos(5);
        controller.cardsPhase();

        users.get(p1).setMnPos(2);

        try {
            controller.moveMN(p1);
            fail();
        }catch (RuntimeException r){}
    }

    /**
     * In this test we check the correctness of the method chooseCloud; in first instance we fill the clouds, then we
     * empty the players' entrance so that we have total control on which students will be present in the entrance
     * after the chooseCloud. After that every player choose a cloud and is also simulated a wrong chose.
     * Finally, we check that the method has worked flawlessly with asserts.
     */
    @Test
    void chooseCloud() throws Exception
    {
        controller.decorateModel(controller.getModel());
        controller.cloudsFilling();
        List<Colour> students1, students2, students3;
        students1 = ModelTest.getCloudsList(controller.getModel()).get(0).getStudentsColours();
        students2 = ModelTest.getCloudsList(controller.getModel()).get(1).getStudentsColours();
        students3 = ModelTest.getCloudsList(controller.getModel()).get(2).getStudentsColours();
        for(int i=0; i<9; i++){
            ModelTest.getPlayers(controller.getModel()).get(0).entranceEmptier(ModelTest.getPlayers(controller.getModel()).get(0).getStudents().get(0));
            ModelTest.getPlayers(controller.getModel()).get(1).entranceEmptier(ModelTest.getPlayers(controller.getModel()).get(1).getStudents().get(0));
            ModelTest.getPlayers(controller.getModel()).get(2).entranceEmptier(ModelTest.getPlayers(controller.getModel()).get(2).getStudents().get(0));
        }
        users.get(p1).setCloudPos(1);
        users.get(p2).setCloudPos(0);

        Thread thread= new Thread(() -> {
            while (true)
            {
                users.get(p3).setCharacterCardID(6);
                users.get(p3).activationCardRequest();
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p3).setCloudPos(1);
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p3).setCloudPos(5);
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p3).setCloudPos(2);
            }
        });
        thread.start();
        controller.chooseCloud(p1);
        controller.chooseCloud(p2);
        controller.chooseCloud(p3);
        thread.interrupt();

        for(int i=0; i<3; i++){
            assertTrue(controller.getModel().getStudents(p1).get(i) == students2.get(i));
            assertTrue(controller.getModel().getStudents(p2).get(i) == students1.get(i));
            assertTrue(controller.getModel().getStudents(p3).get(i) == students3.get(i));
        }
    }

    /**
     * In this particular test we simulate a case where we arrive in the phase og choosing a cloud, but with a
     * player's entrance full. This is something that should never happen, so it's a fatal error caught
     * with an exception.
     */
    @Test
    void fullEntranceException() throws ConnectionErrorException, InterruptedException {
        controller.cloudsFilling();
        users.get(p1).setCloudPos(1);
        try{
            controller.chooseCloud(p1);
            fail();
        }catch (RuntimeException r){}
    }

    /**
     * In this test we simulate lots of possible cases of the MoveStudent method: correct choices, wrong choices,
     * corner cases, exceptions... To make this possible we simulate various rounds of the match where players
     * move students and by also manipulating values with setter methods. All the phases of this test are commented
     * inside the code as it's very complex and long.
     */
    @Test
    void moveStudents() throws Exception
    {
        /*
        * In the first part of the test we need to know which students have been randomly added to a player's entrance
        * in the creation of the match; we achieve this with a getter method. Then, we simulate a player making
        * various choices ih moving his students, some correct and some wrong.
        */

        List<Colour> colours= controller.getModel().getStudents(p1);
        Thread thread= new Thread(() -> {
                while (true)
                {
                    users.get(p1).setCharacterCardID(6);
                    users.get(p1).activationCardRequest();
                    users.get(p1).setTarget(true);
                    users.get(p1).setStudColour(colours.remove(0));
                    try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                    users.get(p1).setTarget(false);
                    users.get(p1).setStudColour(Colour.red);
                    users.get(p1).setIslandPos(15);
                    try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                    users.get(p1).setTarget(false);
                    users.get(p1).setStudColour(colours.remove(0));
                    users.get(p1).setIslandPos(3);
                    try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                    users.get(p1).setTarget(true);
                    users.get(p1).setStudColour(colours.remove(0));
                    try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                    users.get(p1).setTarget(true);
                    users.get(p1).setStudColour(colours.remove(0));
                    try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                }
        });
        thread.start();
        controller.moveStudents(p1);
        thread.interrupt();

        /*
        * Now, what we want to do is to obtain an exception which should never happen: there are no more students
        * in the entrance. This should never happen because the entrance obtains students from the clouds, so if the
        * students finish the first entities to notice it, are the clouds or eventually some character cards, but
        * never the entrance.
        * To achieve this I simulate an impossible match where a player moves his students but without calling
        * the chooseCloud method with which he would refill his entrance.
        */

        Thread thread1= new Thread(() -> {
            while (true)
            {
                users.get(p1).setTarget(true);
                users.get(p1).setStudColour(colours.remove(0));
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p1).setTarget(false);
                users.get(p1).setStudColour(colours.remove(0));
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p1).setCharacterCardID(6);
                users.get(p1).activationCardRequest();
                users.get(p1).setIslandPos(3);
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p1).setTarget(true);
                users.get(p1).setStudColour(colours.remove(0));
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p1).setTarget(true);
                users.get(p1).setStudColour(colours.remove(0));
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
            }
        });

        /*
        * I repeat the same thing as there are 9 students in the entrance in a match with 3 players and a player can
        * move 4 students per round. As the students would have ended before the last choice I add manually 2 students
        * to the player's entrance so that he finishes his students in the middle of the moveStudents and when the
        * controller tries to get the last student gets an error.
        */

        thread1.start();
        controller.moveStudents(p1);
        thread1.interrupt();

        List<Student> student = new ArrayList<>();
        Thread thread2= new Thread(() -> {
            while (true)
            {
                users.get(p1).setTarget(true);
                users.get(p1).setStudColour(colours.remove(0));
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                for (int i=0; i<2; i++)
                    student.add(new Student(Colour.red));
                try {
                    controller.getModel().entranceFiller(p1, student);
                } catch (NoSuchPlayerException | FullEntranceException e) {
                    throw new RuntimeException(e);
                }
                users.get(p1).setTarget(false);
                users.get(p1).setStudColour(Colour.red);
                users.get(p1).setIslandPos(3);
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p1).setTarget(true);
                users.get(p1).setStudColour(Colour.green);
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p1).setTarget(true);
                users.get(p1).setStudColour(Colour.red);
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p1).setTarget(true);
                users.get(p1).setStudColour(Colour.red);
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
            }
        });

        thread2.start();
        try {
            controller.moveStudents(p1);
            fail();
        }catch (RuntimeException r){}
        thread2.interrupt();

        /*
        * In this last part of the test we simulate a case in which a player completely fills one of his classrooms
        * (in this case is the red classroom). To achieve this we manipulate the game, and we add students so that the
        * player has 9 of them. Then, we add the 10th in the moveStudents phase so that the classroom is full,
        * and then we try to add the 11th which is rejected. Everything is checked with exceptions and assert methods.
        */
        int stud = ModelTest.getPlayers(controller.getModel()).get(1).getStudentNum(Colour.red);
        for(int i=0; i< 9-stud; i++)
            controller.getModel().addStudentDashboard(p1, new Student(Colour.red));

        student.clear();
        student.add(new Student(Colour.red));
        student.add(new Student(Colour.red));
        student.add(new Student(Colour.green));
        student.add(new Student(Colour.blue));
        student.add(new Student(Colour.blue));

        ModelTest.getPlayers(controller.getModel()).get(1).entranceFiller(student);

        int yellow = ModelTest.getPlayers(controller.getModel()).get(1).getStudentNum(Colour.yellow);
        int green = ModelTest.getPlayers(controller.getModel()).get(1).getStudentNum(Colour.green);
        int blue = ModelTest.getPlayers(controller.getModel()).get(1).getStudentNum(Colour.blue);
        int pink = ModelTest.getPlayers(controller.getModel()).get(1).getStudentNum(Colour.pink);

        Thread thread3= new Thread(() -> {
            while (true)
            {
                users.get(p1).setTarget(true);
                users.get(p1).setStudColour(Colour.red);
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p1).setTarget(false);
                users.get(p1).setStudColour(Colour.green);
                users.get(p1).setIslandPos(3);
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p1).setTarget(true);
                users.get(p1).setStudColour(Colour.red);
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p1).setTarget(true);
                users.get(p1).setStudColour(Colour.blue);
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p1).setTarget(true);
                users.get(p1).setStudColour(Colour.blue);
            }
        });

        thread3.start();
        controller.moveStudents(p1);
        thread3.interrupt();

        assertEquals(yellow, ModelTest.getPlayers(controller.getModel()).get(1).getStudentNum(Colour.yellow));
        assertEquals(10, ModelTest.getPlayers(controller.getModel()).get(1).getStudentNum(Colour.red));
        assertEquals(green, ModelTest.getPlayers(controller.getModel()).get(1).getStudentNum(Colour.green));
        assertEquals(blue + 2, ModelTest.getPlayers(controller.getModel()).get(1).getStudentNum(Colour.blue));
        assertEquals(pink, ModelTest.getPlayers(controller.getModel()).get(1).getStudentNum(Colour.pink));
    }

    /**
     * In this test we simulate the creation of a match and a first round of that with some correct and wrong choices
     * by the players.
     */
    @Test
    void executionTest() throws Exception{
        List<Colour> colours= controller.getModel().getStudents(p1);
        System.out.flush();

        users.get(p1).setCardPos(1);
        users.get(p2).setCardPos(2);
        users.get(p3).setCardPos(3);
        users.get(p1).setMnPos(1);
        users.get(p1).setCloudPos(1);

        Thread thread= new Thread(() -> {
            {
                try{ Thread.sleep(6000); } catch (InterruptedException e){ return; }
                users.get(p1).setTarget(true);
                users.get(p1).setStudColour(colours.remove(0));
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p1).setTarget(false);
                users.get(p1).setStudColour(colours.remove(0));
                users.get(p1).setIslandPos(3);
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p1).setTarget(true);
                users.get(p1).setStudColour(colours.remove(0));
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p1).setTarget(true);
                users.get(p1).setStudColour(colours.remove(0));
                try{ Thread.sleep(2000); } catch (InterruptedException e){ return; }
                controller.interrupt();
            }
        });

        System.setOut(new PrintStream(new OutputStream()
        {
            public void close() {}
            public void flush() {}
            public void write(byte[] b) {}
            public void write(byte[] b, int off, int len) {}
            public void write(int b) {}
        }
        ));
        thread.start();
        controller.start();
        controller.join();
        thread.interrupt();
    }

    public static List<String> getUIDsList(Controller controller) { return new ArrayList<>(controller.uIDsList); }
}