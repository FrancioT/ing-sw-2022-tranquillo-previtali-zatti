package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Colour;

import java.util.ArrayList;
import java.util.List;
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
    private List<Colour> studentsColours;

    public DataBuffer(String uID)
    {
        this.uID=uID;
        cardPos=-1;
        target=Optional.empty();
        studColour=Optional.empty();
        islandPos=-1;
        mnPos=-1;
        cloudPos=-1;
        studentsColours= new ArrayList<>();
    }

    public synchronized String getUID() { return uID; }
    public synchronized int getCardPos() throws InterruptedException
    {
        while(cardPos==-1)
            wait();
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
    public synchronized boolean getTarget() throws InterruptedException
    {
        while(!target.isPresent())
            wait();
        boolean returnValue=target.get();
        target=Optional.empty();
        return returnValue;
    }
    public synchronized void setTarget(boolean value)
    {
        target=Optional.of(value);
        notifyAll();
    }
    public synchronized Colour getStudentColour() throws InterruptedException
    {
        while(!studColour.isPresent())
            wait();
        Colour returnValue=studColour.get();
        studColour=Optional.empty();
        return returnValue;
    }
    public synchronized void setStudColour(Colour colour)
    {
        studColour=Optional.of(colour);
        notifyAll();
    }
    public synchronized int getIslandPos() throws InterruptedException
    {
        while(islandPos==-1)
            wait();
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
    public synchronized int getMnPos() throws InterruptedException
    {
        while(mnPos==-1)
            wait();
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
    public synchronized int getCloudPos() throws InterruptedException
    {
        while(cloudPos==-1)
            wait();
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
    public synchronized List<Colour> getStudentsColours() throws InterruptedException
    {
        while(studentsColours.size()==0)
            wait();
        List<Colour> returnList= new ArrayList<>(studentsColours);
        studentsColours.clear();
        return returnList;
    }
    public synchronized void setStudentsColours(List<Colour> colours)
    {
        studentsColours.addAll(colours);
        notifyAll();
    }
}
