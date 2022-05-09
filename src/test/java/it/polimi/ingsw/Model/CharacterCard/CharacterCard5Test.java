package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.ColourT;
import it.polimi.ingsw.Model.Exceptions.EmptyException;
import it.polimi.ingsw.Model.Exceptions.NoInhibitionFlagsAvailable;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.ModelTest;
import it.polimi.ingsw.Model.Student;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCard5Test
{
    @Test
    public void CharacterCard5Handle() throws Exception
    {
        String player1 = "Aldo";
        String player2 = "Giovanni";
        String player3 = "Giacomo";
        CharacterCard5 card = new CharacterCard5();
        List<CharacterCard> cards = new ArrayList<>();
        cards.add(card);
        Map<String, DataBuffer> uIDs = new HashMap<>();
        uIDs.put(player1, new DataBuffer(player1));
        uIDs.put(player2, new DataBuffer(player2));
        uIDs.put(player3, new DataBuffer(player3));

        Controller controller = new Controller(uIDs, true);
        ModelTest.changeCard(controller.getModel(), cards);

        try{
            card.handle(null, null, null);
            fail();
        }catch (NullPointerException n){};

        try {
            card.handle("Giovanni", new DataBuffer(player1), controller);
            fail();
        }catch (NotEnoughMoneyException n){};


        for(int i=0; i<9; i++){
            controller.getModel().addStudentDashboard("Giacomo", new Student(Colour.pink));
            controller.getModel().addStudentDashboard("Giacomo", new Student(Colour.green));
            controller.getModel().addStudentDashboard("Giacomo", new Student(Colour.blue));
            controller.getModel().addStudentDashboard("Giacomo", new Student(Colour.yellow));
            controller.getModel().addStudentDashboard("Aldo", new Student(Colour.red));
        }

        for(int i=0; i<6; i++)
            controller.getModel().addStudentDashboard("Aldo", new Student(Colour.pink));

        assertTrue(ModelTest.getPlayers(controller.getModel()).get(0).checkTeacherPresence(Colour.blue));
        assertTrue(ModelTest.getPlayers(controller.getModel()).get(1).checkTeacherPresence(Colour.red));

        for(int i=0; i<5; i++)
            controller.getModel().addStudentIsland(1, new Student(Colour.red));

        controller.getModel().moveMN(1);

        assertTrue(ModelTest.getIslandsList(controller.getModel()).get(1).getTowersColour() == ColourT.white);

        for(int i=0; i<10; i++)
            controller.getModel().addStudentIsland(1, new Student(Colour.blue));

        controller.getModel().moveMN(5);

        DataBuffer data= new DataBuffer("Giacomo");
        data.setIslandPos(1);
        card.handle("Giacomo", data, controller);

        controller.getModel().moveMN(((1 - controller.getModel().getCurrPosMN()) + 12) %12);
        assertTrue(ModelTest.getIslandsList(controller.getModel()).get(1).getTowersColour() == ColourT.white);
        controller.getModel().moveMN(5);
        controller.getModel().moveMN(((1 - controller.getModel().getCurrPosMN()) + 12) %12);
        assertTrue(ModelTest.getIslandsList(controller.getModel()).get(1).getTowersColour() == ColourT.black);

        for(int i=0; i<3; i++){
            data.setIslandPos(1);
            card.handle("Giacomo", data, controller);
    }
        DataBuffer data1 = new DataBuffer("Aldo");
        data1.setIslandPos(1);
        card.handle("Aldo", data1, controller);
        data1.setIslandPos(1);

        try {
            card.handle("Aldo", data1, controller);
            fail();
        }catch (NoInhibitionFlagsAvailable n){};
    }
}