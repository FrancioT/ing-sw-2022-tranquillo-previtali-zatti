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
    public synchronized int getCardPos()
    {
        while(cardPos==-1)
            try { wait(); } catch (InterruptedException ignored){}
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
    public synchronized boolean getTarget()
    {
        while(!target.isPresent())
            try { wait(); } catch (InterruptedException ignored){}
        boolean returnValue=target.get();
        target=Optional.empty();
        return returnValue;
    }
    public synchronized void setTarget(boolean value)
    {
        target=Optional.of(value);
        notifyAll();
    }
    public synchronized Colour getStudentColour()
    {
        while(!studColour.isPresent())
            try { wait(); } catch (InterruptedException ignored){}
        Colour returnValue=studColour.get();
        studColour=Optional.empty();
        return returnValue;
    }
    public synchronized void setStudColour(Colour colour)
    {
        studColour=Optional.of(colour);
        notifyAll();
    }
    public synchronized int getIslandPos()
    {
        while(islandPos==-1)
            try { wait(); } catch (InterruptedException ignored){}
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
    public synchronized int getMnPos()
    {
        while(mnPos==-1)
            try { wait(); } catch (InterruptedException ignored){}
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
    public synchronized int getCloudPos()
    {
        while(cloudPos==-1)
            try { wait(); } catch (InterruptedException ignored){}
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
    public synchronized List<Colour> getStudentsColours()
    {
        while(studentsColours.size()==0)
            try { wait(); } catch (InterruptedException ignored){}
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
