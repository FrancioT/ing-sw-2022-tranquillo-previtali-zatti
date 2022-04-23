package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Model.ModelAndDecorators.ModelTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCard4Test {
    @Test
    public void CharacterCard4Handle() throws Exception
    {
        String player1="Aldo";
        String player2="Giovanni";
        String player3="Giacomo";
        CharacterCard4 card = new CharacterCard4();
        List<String> uIDs = new ArrayList<>();
        uIDs.add(player1);
        uIDs.add(player2);
        uIDs.add(player3);

        Controller controller = new Controller(uIDs, true);
        ModelTest.changeCard(controller.getModel(), card);
        card.handle(player3, null, controller);
        controller.getModel().cardDiscarder(player3, 2);
        assertEquals(controller.getModel().getLastCardValue(player3), 4);
    }
}