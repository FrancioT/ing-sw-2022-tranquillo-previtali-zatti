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
    @Test
    void cloudsFilling() {
        controller.cloudsFilling();
        assertTrue(ModelTest.getCloudsList(controller.getModel()).get(0).getStudentsColours().size() == 4);
    }

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

        // aggiungere caso che si rimane con una carta uguale per almeno due persone
    }

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
    }

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

    @Test
    void chooseCloud() throws Exception
    {
        controller.decorateModel(controller.getModel());
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
    }

    @Test
    void fullEntranceException() throws ConnectionErrorException, InterruptedException {
        controller.cloudsFilling();
        users.get(p1).setCloudPos(1);
        try{
            controller.chooseCloud(p1);
            fail();
        }catch (RuntimeException r){}
    }

    @Test
    void moveStudents() throws Exception
    {
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
                for (int i=0; i<3; i++)
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
        }catch (RuntimeException r){}
        thread2.interrupt();

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
    }

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