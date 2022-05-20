package it.polimi.ingsw.ClientsHandler.Messages.CharacterCardMessages;

import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Colour;

import java.util.ArrayList;
import java.util.List;

public class Card7_10Data extends CardMessage
{
    private final List<Colour> studColours;
    static final long serialVersionUID= 30103L;

    public Card7_10Data(String uID, int cardID, List<Colour> studentsColours)
    {
        super(uID, cardID);
        if(studentsColours==null)
            throw new NullPointerException();
        this.studColours=new ArrayList<>(studentsColours);
    }
    @Override
    public void handle(DataBuffer dataBuffer)
    {
        super.checkDataBuffer(dataBuffer);
        synchronized (dataBuffer)
        {
            dataBuffer.setCharacterCardID(cardID);
            dataBuffer.setStudentsColours(studColours);
        }
    }
}
