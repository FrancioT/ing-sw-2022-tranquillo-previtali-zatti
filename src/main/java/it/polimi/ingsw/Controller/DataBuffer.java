package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.EmptyException;

import java.util.Optional;

public class DataBuffer
{
    private final String uID;
    private int cardPos;
    private Optional<Boolean> target;
    private Optional<Colour> studColour;
    private int islandPos;
    private int mnPos;
    private int cloudPos;

    public DataBuffer(String uID)
    {
        this.uID=uID;
        cardPos=-1;
        target=Optional.empty();
        studColour=Optional.empty();
        islandPos=-1;
        mnPos=-1;
        cloudPos=-1;
    }

    public synchronized String getUID() { return uID; }
    public synchronized int getCardPos() throws EmptyException
    {
        if(cardPos==-1)
            throw new EmptyException();
        int returnValue= cardPos;
        cardPos=-1;
        return returnValue;
    }
    public synchronized void setCardPos(int pos)
    {
        if(pos<0)
            throw new IllegalArgumentException();
        cardPos=pos;
        notifyAll();
    }
    public synchronized boolean getTarget() throws EmptyException
    {
        if(!target.isPresent())
            throw new EmptyException();
        boolean returnValue=target.get();
        target=Optional.empty();
        return returnValue;
    }
    public synchronized void setTarget(boolean value)
    {
        target=Optional.of(value);
        notifyAll();
    }
    public synchronized Colour getStudentColour() throws EmptyException
    {
        if(!studColour.isPresent())
            throw new EmptyException();
        Colour returnValue=studColour.get();
        studColour=Optional.empty();
        return returnValue;
    }
    public synchronized void setStudColour(Colour colour)
    {
        studColour=Optional.of(colour);
        notifyAll();
    }
    public synchronized int getIslandPos() throws EmptyException
    {
        if(islandPos==-1)
            throw new EmptyException();
        int returnValue=islandPos;
        islandPos=-1;
        return returnValue;
    }
    public synchronized void setIslandPos(int pos)
    {
        if(pos<0)
            throw new IllegalArgumentException();
        islandPos=pos;
        notifyAll();
    }
    public synchronized int getMnPos() throws EmptyException
    {
        if(mnPos==-1)
            throw new EmptyException();
        int returnValue=mnPos;
        mnPos=-1;
        return returnValue;
    }
    public synchronized void setMnPos(int pos)
    {
        if(pos<0)
            throw new IllegalArgumentException();
        mnPos=pos;
        notifyAll();
    }
    public synchronized int getCloudPos() throws EmptyException
    {
        if(cloudPos==-1)
            throw new EmptyException();
        int returnValue=cloudPos;
        cloudPos=-1;
        return returnValue;
    }
    public synchronized void setCloudPos(int pos)
    {
        if(pos<0)
            throw new IllegalArgumentException();
        cloudPos=pos;
        notifyAll();
    }
}
