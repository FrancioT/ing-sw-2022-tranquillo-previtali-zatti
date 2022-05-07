package it.polimi.ingsw.ClientsHandler.Messages;

import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Colour;

public class StudentToIsland extends Message
{
    private final Colour studColour;
    private final int islandPos;
    StudentToIsland(String uID, Colour studentColour, int islandPos)
    {
        super(uID);
        if(studentColour==null)
            throw new NullPointerException();
        if(islandPos<0)
            throw new IndexOutOfBoundsException();
        studColour=studentColour;
        this.islandPos=islandPos;
    }
    @Override
    protected void handle(DataBuffer dataBuffer)
    {
        super.checkDataBuffer(dataBuffer);
        synchronized (dataBuffer)
        {
            dataBuffer.setIslandPos(islandPos);
            dataBuffer.setStudColour(studColour);
            dataBuffer.setTarget(false);
        }
    }
}
