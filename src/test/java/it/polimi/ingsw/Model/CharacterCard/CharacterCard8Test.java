package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.ColourT;
import it.polimi.ingsw.Model.Exceptions.EmptyException;
import it.polimi.ingsw.Model.Exceptions.NoSuchPlayerException;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.ModelTest;
import it.polimi.ingsw.Model.Student;
import it.polimi.ingsw.RemoteView.RemoteView;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCard8Test
{
    @Test
    public void CharacterCard8Handle() throws Exception {
        String player1 = "Aldo";
        String player2 = "Giovanni";
        String player3 = "Giacomo";
        CharacterCard8 card = new CharacterCard8();
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

        try {
            card.handle(null, null, null);
            fail();
        } catch (NullPointerException n) {
        }
        ;

        try {
            card.handle("Aldo", new DataBuffer(player1), controller);
            fail();
        } catch (NotEnoughMoneyException n) {
        }
        ;

        try {
            card.handle("Ajeje", new DataBuffer(player1), controller);
            fail();
        } catch (NoSuchPlayerException n) {
        }
        ;

        for (int i = 0; i < 9; i++) {
            controller.getModel().addStudentDashboard("Aldo", new Student(Colour.blue));
            controller.getModel().addStudentDashboard("Giacomo", new Student(Colour.red));
        }

        controller.getModel().addStudentIsland(6, new Student(Colour.red));
        controller.getModel().addStudentIsland(6, new Student(Colour.blue));

        card.handle("Giacomo", null, controller);

        controller.getModel().moveMN(((6 - controller.getModel().getCurrPosMN()) + 12) % 12);

        assertTrue(ModelTest.getIslandsList(controller.getModel()).get(6).getTowersColour() == ColourT.black);

        for (int i = 0; i < 3; i++)
            controller.getModel().addStudentIsland(6, new Student(Colour.blue));

        controller.getModel().moveMN(4);
        controller.getModel().moveMN(((6 - controller.getModel().getCurrPosMN()) + 12) % 12);

        assertTrue(ModelTest.getIslandsList(controller.getModel()).get(6).getTowersColour() == ColourT.black);

        controller.getModel().addStudentIsland(6, new Student(Colour.blue));

        controller.getModel().moveMN(4);
        controller.getModel().moveMN(((6 - controller.getModel().getCurrPosMN()) + 12) % 12);

        assertTrue(ModelTest.getIslandsList(controller.getModel()).get(6).getTowersColour() == ColourT.white);

        controller.getModel().addStudentIsland(0, new Student(Colour.red));
        controller.getModel().addStudentIsland(0, new Student(Colour.blue));

        DataBuffer data = new DataBuffer("Aldo");
        data.setIslandPos(0);
        card5.handle("Aldo", data, controller);

        controller.getModel().moveMN(((0 - controller.getModel().getCurrPosMN()) + 12) % 12);

        try {
            ModelTest.getIslandsList(controller.getModel()).get(0).getTowersColour();
            fail();
        }catch (EmptyException e){};
    }
}