package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Exceptions.IllegalMNMovementException;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.ModelAndDecorators.ModelTest;
import it.polimi.ingsw.RemoteView.RemoteView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @Test
    void moveStudents() throws Exception
    {
        List<Colour> colours= controller.getModel().getStudents(p1);
        Thread thread= new Thread(() -> {
                while (true)
                {
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
                }
        });
        thread.start();
        controller.moveStudents(p1);
        thread.interrupt();
    }

    @Test
    void moveMN() throws Exception
    {
        assertTrue(ModelTest.getMN(controller.getModel()).getCurrentPos() == ModelTest.getIslandsList(controller.getModel()).get(0));

        users.get(p1).setCardPos(4);
        users.get(p2).setCardPos(1);
        users.get(p3).setCardPos(8);
        controller.cardsPhase();

        users.get(p1).setMnPos(2);
        users.get(p2).setMnPos(3);

        controller.moveMN(p1);
        assertTrue(ModelTest.getMN(controller.getModel()).getCurrentPos() == ModelTest.getIslandsList(controller.getModel()).get(2));

        controller.moveMN(p2);
        assertTrue(ModelTest.getMN(controller.getModel()).getCurrentPos() == ModelTest.getIslandsList(controller.getModel()).get(3));

        Thread thread= new Thread(() -> {
            while (true)
            {
                users.get(p3).activationCardRequest();
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p3).setMnPos(10);
                try{ Thread.sleep(500); } catch (InterruptedException e){ return; }
                users.get(p3).setMnPos(5);
            }
        });
        thread.start();
        controller.moveMN(p3);
        assertTrue(ModelTest.getMN(controller.getModel()).getCurrentPos() == ModelTest.getIslandsList(controller.getModel()).get(5));
        thread.interrupt();
    }

    @Test
    void chooseCloud() throws Exception
    {
        controller.decorateModel(controller.getModel());
        users.get(p1).setCloudPos(1);
        users.get(p2).setCloudPos(0);
        users.get(p3).setCloudPos(2);
        controller.chooseCloud(p1);
        controller.chooseCloud(p2);
        controller.chooseCloud(p3);
    }
    public static List<String> getUIDsList(Controller controller) { return new ArrayList<>(controller.uIDsList); }
}