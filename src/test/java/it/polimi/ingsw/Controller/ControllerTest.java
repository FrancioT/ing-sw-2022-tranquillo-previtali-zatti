package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Exceptions.IllegalMNMovementException;
import it.polimi.ingsw.Model.Colour;
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
        controller=new Controller(users, true);
    }
    @Test
    void cloudsFilling()
    {
        controller.cloudsFilling();
    }

    @Test
    void cardsPhase() throws Exception
    {
        users.get(p1).setCardPos(4);
        users.get(p2).setCardPos(1);
        users.get(p3).setCardPos(8);
        List<String> orderedPlayers= new ArrayList<>();
        orderedPlayers.add(p2);
        orderedPlayers.add(p3);
        orderedPlayers.add(p1);
        controller.cardsPhase();
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
        cardsPhase();
        users.get(p1).setMnPos(2);
        users.get(p2).setMnPos(3);
        users.get(p3).setMnPos(8);
        controller.moveMN(p1);
        controller.moveMN(p2);
        controller.moveMN(p3);
        users.get(p1).setMnPos(5);
        assertThrows(IllegalMNMovementException.class, ()->controller.moveMN(p1));
        users.get(p1).setMnPos(8);
        assertThrows(IllegalMNMovementException.class, ()->controller.moveMN(p1));
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
}