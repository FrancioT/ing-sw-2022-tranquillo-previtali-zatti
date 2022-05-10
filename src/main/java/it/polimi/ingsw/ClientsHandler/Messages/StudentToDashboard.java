package it.polimi.ingsw.ClientsHandler.Messages;

import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Colour;

class StudentToDashboard extends Message
{
    private final Colour studColour;
    static final long serialVersionUID= 30004L;

    StudentToDashboard(String uID, Colour studentColour)
    {
        super(uID);
        if(studentColour==null)
            throw new NullPointerException();
        studColour=studentColour;
    }
    @Override
    public void handle(DataBuffer dataBuffer)
    {
        super.checkDataBuffer(dataBuffer);
        synchronized (dataBuffer)
        {
            dataBuffer.setStudColour(studColour);
            dataBuffer.setTarget(true);
        }
    }
}
