package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.EmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataBufferTest
{

    static String p1= "Francio";
    static String p2= "Zatto";
    List<Colour> lista1, lista2;
    DataBuffer dataBufferP1, dataBufferP2;

    @Test
    void testAllDataBuffer() throws Exception
    {
        Colour rosso = Colour.red;
        dataBufferP1 = new DataBuffer(p1);
        dataBufferP2 = new DataBuffer(p2);
        lista1 = new ArrayList<>();
        lista2 = new ArrayList<>();

        assertEquals(p1,dataBufferP1.getUID());
        assertEquals(p2,dataBufferP2.getUID());
        dataBufferP1.setCardPos(1);
        dataBufferP2.setCardPos(2);
        assertEquals(1,dataBufferP1.getCardPos());
        assertEquals(2,dataBufferP2.getCardPos());

        dataBufferP1.setTarget(true);
        dataBufferP2.setTarget(false);
        assertTrue(dataBufferP1.getTarget());
        assertFalse(dataBufferP2.getTarget());

        dataBufferP1.setStudColour(rosso);
        assertEquals(rosso, dataBufferP1.getStudentColour());

        dataBufferP1.setIslandPos(3);
        assertEquals(3, dataBufferP1.getIslandPos());

        dataBufferP1.setMnPos(5);
        assertEquals(5, dataBufferP1.getMnPos());

        dataBufferP1.setCloudPos(1);
        assertEquals(1, dataBufferP1.getCloudPos());

        lista2.add(Colour.blue);
        lista2.add(Colour.yellow);
        lista1 = lista2;
        dataBufferP1.setStudentsColours(lista2);
        assertEquals(lista1, dataBufferP1.getStudentsColours());
    }

}