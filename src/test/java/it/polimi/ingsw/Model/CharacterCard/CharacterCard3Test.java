package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.ColourT;
import it.polimi.ingsw.Model.Exceptions.EmptyException;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.ModelTest;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Student;
import it.polimi.ingsw.RemoteView.RemoteView;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCard3Test
{
    @Test
    public void CharacterCard3Handle() throws Exception
    {
        String player1="Aldo";
        String player2="Giovanni";
        String player3="Giacomo";
        CharacterCard3 card = new CharacterCard3();
        CharacterCard5 card5 = new CharacterCard5();
        List<CharacterCard> cards = new ArrayList<>();
        cards.add(card);
        cards.add(card5);
        Map<String, DataBuffer> uIDs = new HashMap<>();
        uIDs.put(player1, new DataBuffer(player1));
        uIDs.put(player2, new DataBuffer(player2));
        uIDs.put(player3, new DataBuffer(player3));

        Controller controller = new Controller(uIDs, true, new ArrayList<RemoteView>());
        ModelTest.changeCard(controller.getModel(), cards);

        DataBuffer datas= new DataBuffer("Giacomo");

        try{
            card.handle(null, null, null);
            fail();
        }catch (NullPointerException n){};

        try {
            card.handle("Giovanni", datas, controller);
            fail();
        }catch (NotEnoughMoneyException n){};

        for(int i=0; i<6; i++)
            controller.getModel().addStudentDashboard("Aldo", new Student(Colour.green));

        DataBuffer data = new DataBuffer("Aldo");
        data.setIslandPos(6);

        controller.getModel().addStudentIsland(6, new Student(Colour.green));
        controller.getModel().addStudentIsland(6, new Student(Colour.pink));

        for(int i=0; i<9; i++)
            controller.getModel().addStudentDashboard("Giacomo", new Student(Colour.pink));

        card.handle("Aldo", data, controller);

        try {
            ModelTest.getIslandsList(controller.getModel()).get(6).getTowersColour();
            fail();
        }catch (EmptyException e){};

        controller.getModel().addStudentDashboard("Giacomo", new Student(Colour.blue));
        controller.getModel().addStudentDashboard("Aldo", new Student(Colour.red));

        assertTrue(ModelTest.getPlayers(controller.getModel()).get(0).checkTeacherPresence(Colour.blue));
        assertTrue(ModelTest.getPlayers(controller.getModel()).get(1).checkTeacherPresence(Colour.red));

        for(int i=0; i<5; i++)
            controller.getModel().addStudentIsland(1, new Student(Colour.red));

        for(int i=0; i<5; i++)
            controller.getModel().addStudentIsland(3, new Student(Colour.blue));

        controller.getModel().moveMN(1);

        assertTrue(ModelTest.getIslandsList(controller.getModel()).get(1).getTowersColour() == ColourT.white);

        try {
            ModelTest.getIslandsList(controller.getModel()).get(3).getTowersColour();
            fail();
        }catch (EmptyException e){};

        for(int i=0; i<10; i++)
            controller.getModel().addStudentIsland(1, new Student(Colour.blue));

        assertTrue(ModelTest.getIslandsList(controller.getModel()).get(1).getTowersColour() == ColourT.white);

        for(int i=0; i<7; i++)
            controller.getModel().addStudentIsland(3, new Student(Colour.red));

        controller.getModel().moveMN(2);

        assertTrue(ModelTest.getIslandsList(controller.getModel()).get(3).getTowersColour() == ColourT.white);

        controller.getModel().moveMN(6);

        for(int i=0; i<5; i++)
            controller.getModel().addStudentIsland(3, new Student(Colour.blue));

        datas.setIslandPos(3);
        card.handle("Giacomo", datas, controller);

        assertTrue(ModelTest.getIslandsList(controller.getModel()).get(3).getTowersColour() == ColourT.black);
        assertTrue(ModelTest.getIslandsList(controller.getModel()).get(1).getTowersColour() == ColourT.white);

        assertTrue(controller.getModel().getCurrPosMN() == 9);

        controller.getModel().moveMN(((1 - controller.getModel().getCurrPosMN()) + 12) %12);
        assertTrue(ModelTest.getIslandsList(controller.getModel()).get(1).getTowersColour() == ColourT.black);

        for(int i=0; i<9; i++){
            controller.getModel().addStudentDashboard("Aldo", new Student(Colour.pink));
            controller.getModel().addStudentDashboard("Aldo", new Student(Colour.red));
        }

        for(int i=0; i<3; i++)
            controller.getModel().addStudentDashboard("Aldo", new Student(Colour.green));

        data.setIslandPos(6);
        card5.handle("Aldo",data, controller);

        assertTrue(ModelTest.getIslandsList(controller.getModel()).get(6).getInhibition());

        data.setIslandPos(6);
        card.handle("Aldo", data, controller);

        assertFalse(ModelTest.getIslandsList(controller.getModel()).get(6).getInhibition());

        try {
            ModelTest.getIslandsList(controller.getModel()).get(6).getTowersColour();
            fail();
        }catch (EmptyException e){};

    }
}