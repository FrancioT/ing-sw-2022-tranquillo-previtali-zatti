package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Exceptions.CardActivatedException;
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
    private final List<Colour> studentsColours;
    private boolean activationCardRequest;
    private int characterCardID;

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
        activationCardRequest=false;
        characterCardID=-1;
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
    public synchronized boolean getTarget() throws InterruptedException, CardActivatedException
    {
        while(!target.isPresent() && !activationCardRequest)
            wait();
        if(activationCardRequest)
        {
            activationCardRequest = false;
            throw new CardActivatedException();
        }
        boolean returnValue=target.get();
        target=Optional.empty();
        return returnValue;
    }
    public synchronized void setTarget(boolean value)
    {
        target=Optional.of(value);
        notifyAll();
    }
    public synchronized Colour getStudentColour() throws InterruptedException, CardActivatedException
    {
        while(!studColour.isPresent() && !activationCardRequest)
            wait();
        if(activationCardRequest)
        {
            activationCardRequest = false;
            throw new CardActivatedException();
        }
        Colour returnValue=studColour.get();
        studColour=Optional.empty();
        return returnValue;
    }
    public synchronized void setStudColour(Colour colour)
    {
        studColour=Optional.of(colour);
        notifyAll();
    }
    public synchronized int getIslandPos() throws InterruptedException, CardActivatedException
    {
        while(islandPos==-1 && !activationCardRequest)
            wait();
        if(activationCardRequest)
        {
            activationCardRequest = false;
            throw new CardActivatedException();
        }
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
    public synchronized int getMnPos() throws InterruptedException, CardActivatedException
    {
        while(mnPos==-1 && !activationCardRequest)
            wait();
        if(activationCardRequest)
        {
            activationCardRequest = false;
            throw new CardActivatedException();
        }
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
    public synchronized int getCloudPos() throws InterruptedException, CardActivatedException
    {
        while(cloudPos==-1 && !activationCardRequest)
            wait();
        if(activationCardRequest)
        {
            activationCardRequest = false;
            throw new CardActivatedException();
        }
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
    public synchronized void activationCardRequest()
    {
        activationCardRequest=true;
        notifyAll();
    }
    public synchronized int getCharacterCardID() throws InterruptedException, CardActivatedException
    {
        while(characterCardID==-1 && !activationCardRequest)
            wait();
        if(activationCardRequest)
        {
            activationCardRequest = false;
            throw new CardActivatedException();
        }
        int returnValue=characterCardID;
        characterCardID=-1;
        return returnValue;
    }
    public synchronized void setCharacterCardID(int id)
    {
        if(id<0)
            throw new IllegalArgumentException();
        characterCardID=id;
        notifyAll();
    }
}
