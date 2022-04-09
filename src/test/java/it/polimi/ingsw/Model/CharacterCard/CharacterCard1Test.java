package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Colour;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCard1Test {
    @Test
    public void CharacterCard1Colours(){
        CharacterCard1 card = new CharacterCard1(new Bag());

        assertEquals(card.getCardID(), 1);
        assertEquals(card.getPrice(), 1);

        for(int i = 0; i < 4; i++) {
            assertNotNull(card.studentsList.get(i));
        }

        List<Colour> colours = card.getColoursOnCard();
        for(int i = 0; i < colours.size(); i++){
            if(!colours.get(i).equals(card.studentsList.get(i).getColour()))
                fail();
        }
    }

    @Test
    public void CharacterCard1Handle() throws Exception {
        CharacterCard1 card = new CharacterCard1(new Bag());

        List<Colour> coloursOnCard = card.getColoursOnCard();
        Colour presentColour = null, absentColour = null;

        if (coloursOnCard.contains(Colour.yellow))
            presentColour = Colour.yellow;

        else
            absentColour = Colour.yellow;

        if(coloursOnCard.contains(Colour.blue))
            presentColour = Colour.blue;

        else
            absentColour = Colour.blue;

        if (coloursOnCard.contains(Colour.green))
            presentColour = Colour.green;

        else
            absentColour = Colour.green;

        if(coloursOnCard.contains(Colour.red))
            presentColour = Colour.red;

        else
            absentColour = Colour.red;

        if(coloursOnCard.contains(Colour.pink))
            presentColour = Colour.pink;

        else
            absentColour = Colour.pink;

        try {
            card.handle("test", absentColour,new Controller());
            fail();
        } catch (Exception n){}

        /* card.handle("test", presentColour, new Controller()); */ /* servirebbe creare tutto il game per testarlo*/
    }
}