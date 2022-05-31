package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.Exceptions.RunOutOfStudentsException;
import it.polimi.ingsw.Model.ModelAndDecorators.ModelTest;
import it.polimi.ingsw.RemoteView.RemoteView;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCard4Test {

    /**
     * This test in first instance creates a controller, which creates a model with the charactercard I need.
     * Once the card is created I check if it has the correct values.
     * Then, I test all the cases in which it fails (e.g. not enough money, null pointer).
     * Finally, I activate it correctly and check if the value that has been modified is correct.
     */
    @Test
    public void CharacterCard4Handle() throws Exception
    {
        String player1="Aldo";
        String player2="Giovanni";
        String player3="Giacomo";
        CharacterCard4 card = new CharacterCard4();
        List<CharacterCard> cards = new ArrayList<>();
        cards.add(card);
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

        card.overPrice = true;

        try {
            card.handle("Giovanni", null, controller);
            fail();
        }catch (NotEnoughMoneyException n){};

        card.overPrice = false;

        card.handle(player3, null, controller);
        controller.getModel().cardDiscarder(player3, 2);
        assertEquals(controller.getModel().getLastCardValue(player3), 4);
    }

    /**
     * test the printer methods
     */
    @Test
    public void printerTest() throws RunOutOfStudentsException
    {
        CharacterCard4 card4 = new CharacterCard4();
        System.setOut(new PrintStream(new OutputStream()
        {
            public void close() {}
            public void flush() {}
            public void write(byte[] b) {}
            public void write(byte[] b, int off, int len) {}
            public void write(int b) {}
        }
        ));
        card4.ccPrinter();
    }
}