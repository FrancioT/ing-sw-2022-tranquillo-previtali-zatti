package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.ColourT;
import it.polimi.ingsw.Model.Exceptions.EmptyException;
import it.polimi.ingsw.Model.Exceptions.NoSuchPlayerException;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.Exceptions.RunOutOfStudentsException;
import it.polimi.ingsw.Model.ModelAndDecorators.ModelTest;
import it.polimi.ingsw.Model.Student;
import it.polimi.ingsw.RemoteView.RemoteView;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCard9Test
{
    @Test
    public void CharacterCard9Handle() throws Exception
    {
        String player1="Aldo";
        String player2="Giovanni";
        String player3="Giacomo";
        CharacterCard9 card = new CharacterCard9();
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

        try{
            card.handle(null, null, null);
            fail();
        }catch (NullPointerException n){};

        try {
            card.handle("Aldo", new DataBuffer(player1), controller);
            fail();
        }catch (NotEnoughMoneyException n){};

        try {
            card.handle("Ajeje", new DataBuffer(player1), controller);
            fail();
        }catch (NoSuchPlayerException n){};

        for(int i=0; i<9; i++){
            controller.getModel().addStudentDashboard("Giacomo", new Student(Colour.red));
            controller.getModel().addStudentDashboard("Aldo", new Student(Colour.blue));
        }

        controller.getModel().addStudentDashboard("Aldo", new Student(Colour.green));

        controller.getModel().addStudentIsland(6, new Student(Colour.red));
        controller.getModel().addStudentIsland(6, new Student(Colour.red));
        controller.getModel().addStudentIsland(6, new Student(Colour.blue));
        controller.getModel().addStudentIsland(6, new Student(Colour.green));

        DataBuffer data = new DataBuffer("Giacomo");
        data.setStudColour(Colour.green);

        card.handle("Giacomo", data, controller);

        controller.getModel().moveMN(6);

        assertTrue(ModelTest.getIslandsList(controller.getModel()).get(6).getTowersColour() == ColourT.black);

        controller.getModel().addStudentIsland(6, new Student(Colour.blue));
        controller.getModel().addStudentIsland(6, new Student(Colour.blue));

        controller.getModel().moveMN(4);
        controller.getModel().moveMN(((6 - controller.getModel().getCurrPosMN()) + 12) %12);

        assertTrue(ModelTest.getIslandsList(controller.getModel()).get(6).getTowersColour() == ColourT.black);

        controller.getModel().addStudentIsland(6, new Student(Colour.blue));

        controller.getModel().moveMN(4);
        controller.getModel().moveMN(((6 - controller.getModel().getCurrPosMN()) + 12) %12);

        assertTrue(ModelTest.getIslandsList(controller.getModel()).get(6).getTowersColour() == ColourT.white);

        controller.getModel().addStudentIsland(0, new Student(Colour.red));
        controller.getModel().addStudentIsland(0, new Student(Colour.red));
        controller.getModel().addStudentIsland(0, new Student(Colour.blue));

        DataBuffer dataBuffer = new DataBuffer("Aldo");
        dataBuffer.setIslandPos(0);
        card5.handle("Aldo", dataBuffer, controller);

        controller.getModel().moveMN(((0 - controller.getModel().getCurrPosMN()) + 12) %12);

        try {
            ModelTest.getIslandsList(controller.getModel()).get(0).getTowersColour();
            fail();
        }catch (EmptyException e){};
    }

    @Test
    public void printerTest() throws RunOutOfStudentsException
    {
        CharacterCard9 card9 = new CharacterCard9();
        System.setOut(new PrintStream(new OutputStream()
        {
            public void close() {}
            public void flush() {}
            public void write(byte[] b) {}
            public void write(byte[] b, int off, int len) {}
            public void write(int b) {}
        }
        ));
        card9.ccPrinter();
    }
}