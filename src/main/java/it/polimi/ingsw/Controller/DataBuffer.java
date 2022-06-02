package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Exceptions.CardActivatedException;
import it.polimi.ingsw.Controller.Exceptions.ConnectionErrorException;
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
    private boolean errorStatus;

    /**
     * Constructor of dataBuffer, everything is initialized to null or to values that would be rejected if accessed
     * before the player interaction
     * @param uID the uid of the player who owns this databuffer
     */
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
        errorStatus=false;
    }

    public synchronized String getUID() { return uID; }
    public synchronized int getCardPos() throws InterruptedException, ConnectionErrorException
    {
        while(cardPos==-1 && !errorStatus)
            wait();
        if(errorStatus)
            throw new ConnectionErrorException();
        int returnValue= cardPos;
        cardPos=-1;
        return returnValue;
    }
    public synchronized void setCardPos(int pos)
    {
        if(pos<0)
            return;
        cardPos=pos;
        notifyAll();
    }

    /**
     * @return true if student has to be moved on dashboard, false if it has to be moved on an island
     */
    public synchronized boolean getTarget() throws InterruptedException, CardActivatedException, ConnectionErrorException
    {   // target==true -> dashboard       target==false -> island
        while(!target.isPresent() && !activationCardRequest && !errorStatus)
            wait();
        if(activationCardRequest)
        {
            activationCardRequest = false;
            throw new CardActivatedException();
        }
        if(errorStatus)
            throw new ConnectionErrorException();
        boolean returnValue=target.get();
        target=Optional.empty();
        return returnValue;
    }

    /**
     * @param value true if student has to be moved on dashboard, false if it has to be moved on an island
     */
    public synchronized void setTarget(boolean value)
    {
        target=Optional.of(value);
        notifyAll();
    }
    public synchronized Colour getStudentColour() throws InterruptedException, CardActivatedException, ConnectionErrorException
    {
        while(!studColour.isPresent() && !activationCardRequest && !errorStatus)
            wait();
        if(activationCardRequest)
        {
            activationCardRequest = false;
            throw new CardActivatedException();
        }
        if(errorStatus)
            throw new ConnectionErrorException();
        Colour returnValue=studColour.get();
        studColour=Optional.empty();
        return returnValue;
    }

    /**
     * Method to set a single student's color
     * @param colour a student's color
     */
    public synchronized void setStudColour(Colour colour)
    {
        studColour=Optional.of(colour);
        notifyAll();
    }
    public synchronized int getIslandPos() throws InterruptedException, CardActivatedException, ConnectionErrorException
    {
        while(islandPos==-1 && !activationCardRequest && !errorStatus)
            wait();
        if(activationCardRequest)
        {
            activationCardRequest = false;
            throw new CardActivatedException();
        }
        if(errorStatus)
            throw new ConnectionErrorException();
        int returnValue=islandPos;
        islandPos=-1;
        return returnValue;
    }
    public synchronized void setIslandPos(int pos)
    {
        if(pos<0)
            return;
        islandPos=pos;
        notifyAll();
    }
    public synchronized int getMnPos() throws InterruptedException, CardActivatedException, ConnectionErrorException
    {
        while(mnPos==-1 && !activationCardRequest && !errorStatus)
            wait();
        if(activationCardRequest)
        {
            activationCardRequest = false;
            throw new CardActivatedException();
        }
        if(errorStatus)
            throw new ConnectionErrorException();
        int returnValue=mnPos;
        mnPos=-1;
        return returnValue;
    }
    public synchronized void setMnPos(int pos)
    {
        if(pos<0)
            return;
        mnPos=pos;
        notifyAll();
    }
    public synchronized int getCloudPos() throws InterruptedException, CardActivatedException, ConnectionErrorException
    {
        while(cloudPos==-1 && !activationCardRequest && !errorStatus)
            wait();
        if(activationCardRequest)
        {
            activationCardRequest = false;
            throw new CardActivatedException();
        }
        if(errorStatus)
            throw new ConnectionErrorException();
        int returnValue=cloudPos;
        cloudPos=-1;
        return returnValue;
    }
    public synchronized void setCloudPos(int pos)
    {
        if(pos<0)
            return;
        cloudPos=pos;
        notifyAll();
    }
    public synchronized List<Colour> getStudentsColours() throws InterruptedException, ConnectionErrorException
    {
        while(studentsColours.size()==0 && !errorStatus)
            wait();
        if(errorStatus)
            throw new ConnectionErrorException();
        List<Colour> returnList= new ArrayList<>(studentsColours);
        studentsColours.clear();
        return returnList;
    }

    /**
     * Method to set a list of students' colours
     * @param colours a list of colours
     */
    public synchronized void setStudentsColours(List<Colour> colours)
    {
        studentsColours.addAll(colours);
        notifyAll();
    }

    /**
     * Method called when a player tries to activate a character card; what this method does is setting a flag to true
     * so that every other method will interrupt to resolve the effect of the card
     */
    public synchronized void activationCardRequest()
    {
        activationCardRequest=true;
        notifyAll();
    }
    public synchronized int getCharacterCardID() throws InterruptedException, CardActivatedException, ConnectionErrorException
    {
        while(characterCardID==-1 && !activationCardRequest && !errorStatus)
            wait();
        if(activationCardRequest)
        {
            activationCardRequest = false;
            throw new CardActivatedException();
        }
        if(errorStatus)
            throw new ConnectionErrorException();
        int returnValue=characterCardID;
        characterCardID=-1;
        return returnValue;
    }
    public synchronized void setCharacterCardID(int id)
    {
        if(id<0)
            return;
        characterCardID=id;
        notifyAll();
    }

    /**
     * Method called when a player disconnects to notify all others players
     */
    public synchronized void setErrorStatus()
    {
        errorStatus=true;
        notifyAll();
    }

    /**
     * Method to clear the databuffer to avoid that some data that could cause any problems remains inside it
     */
    public synchronized void clear()
    {
        cardPos=-1;
        target=Optional.empty();
        studColour=Optional.empty();
        islandPos=-1;
        mnPos=-1;
        cloudPos=-1;
        studentsColours.clear();
        activationCardRequest=false;
        characterCardID=-1;
        errorStatus=false;
    }
}
