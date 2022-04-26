package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Exceptions.CardActivatedException;
import it.polimi.ingsw.Controller.Exceptions.IllegalMNMovementException;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;

import java.util.*;

public class Controller
{
    private final List<String> uIDsList;
    private final Map<String, DataBuffer> usersData;
    private Model model;
    private boolean decorationFlag;
    private boolean cardActivated;

    public Controller(Map<String, DataBuffer> uIDs, boolean expertMode)
    {
        model= new Model(new ArrayList<>(uIDs.keySet()), expertMode);
        uIDsList= new ArrayList<>(uIDs.keySet());
        decorationFlag=false;
        usersData=new HashMap<>(uIDs);
        cardActivated=false;
    }
    public synchronized Model getModel() { return model; }
    public synchronized void decorateModel(Model model)
    {
        this.model=model;
        decorationFlag=true;
    }
    public synchronized void cloudsFilling()
    {
        int tmp=uIDsList.size()%2;
        // if there are 2 or 4 players the clouds must be filled with 3 students
        // if there are 3 players the clouds must be filled with 4 students
        model.cloudsFiller(tmp*4 + (1-tmp)*3);
    }
    public synchronized List<String> cardsPhase(List<String> uIDsOrder) throws NoSuchPlayerException,
                                                                               InterruptedException
    {
        List<String> order=new ArrayList<>();  // returning List
        Map<String, Integer> posCards= new HashMap<>();  // used to map a player to his
                                                         // played card position
        // SortedMap used to save player and his card round value in order
        SortedMap<Integer, String> playersOrder=new TreeMap<>();
        int pos;
        int cardValue;
        for(String s: uIDsOrder)
        {
            pos= usersData.get(s).getCardPos();
            posCards.put(s, Integer.valueOf(pos));
        }
        for(String s:uIDsOrder)
        {
            cardValue=model.cardDiscarder(s, posCards.get(s)).getRoundValue();
            playersOrder.put(cardValue, s);
        }
        // add players uIDs in proper order thanks to the iterator of TreeMap
        // which iterate in the ascending order of the keys
        for(String s: playersOrder.values())
            order.add(s);

        return order;
    }
    public synchronized void moveStudents(String uID) throws Exception
    {
        int n=uIDsList.size()%2;  // n=number of students that can be moved
        n=n*4 + (1-n)*3;       // n=3 if number of players is 2 or 4
                               // n=4 if number of players is 3
        Boolean target;
        Colour colour;
        int index=-1;
        for(int i=0; i<n; i++)
        {
            target=null;
            colour=null;
            while (target==null || colour==null)
            {
                try {
                    if(target==null)
                        target = usersData.get(uID).getTarget();
                    colour = usersData.get(uID).getStudentColour();
                } catch (CardActivatedException e) {
                    if (!cardActivated)
                    {
                        cardActivated=true;
                        model.activateCard(uID, usersData.get(uID), this);
                    }
                    else
                        throw new CardActivatedException();    //////////////////////////////////////////////////////////////////////
                        // here you have to notify the client that he can't activate the card another time
                        // instead of throwing an exception
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
                }
            }
            if(target)
                model.addStudentDashboard(uID, model.entranceEmptier(uID, colour));
            else
            {
                while (index==-1)
                {
                    try {
                        index= usersData.get(uID).getIslandPos();
                    } catch (CardActivatedException e) {
                        if (!cardActivated)
                        {
                            cardActivated=true;
                            model.activateCard(uID, usersData.get(uID), this);
                        }
                        else
                            throw new CardActivatedException();    //////////////////////////////////////////////////////////////////////
                        // here you have to notify the client that he can't activate the card another time
                        // instead of throwing an exception
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    }
                }
                model.addStudentIsland(index, model.entranceEmptier(uID, colour));
            }
        }
    }
    public synchronized void moveMN(String uID) throws Exception
    {
        int newPos=-1;
        while (newPos==-1)
        {
            try {
                newPos = usersData.get(uID).getMnPos();
            } catch (CardActivatedException e){
                if (!cardActivated)
                {
                    cardActivated=true;
                    model.activateCard(uID, usersData.get(uID), this);
                }
                else
                    throw new CardActivatedException();    //////////////////////////////////////////////////////////////////////
                // here you have to notify the client that he can't activate the card another time
                // instead of throwing an exception
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////
            }
        }
        int oldPos= model.getCurrPosMN();
        if(newPos==oldPos)
            throw new IllegalMNMovementException();
        int delta_pos;
        if(newPos>oldPos)
            delta_pos= newPos-oldPos;
        else
            delta_pos= model.getNumIslands()-oldPos+newPos;
        if(delta_pos > model.getLastCardValue(uID) )
            throw new IllegalMNMovementException();
        model.moveMN(delta_pos);
    }
    public synchronized void chooseCloud(String uID) throws Exception
    {
        int index=-1;
        while (index==-1)
        {
            try {
                index = usersData.get(uID).getCloudPos();
            } catch (CardActivatedException e){
                if (!cardActivated)
                {
                    cardActivated=true;
                    model.activateCard(uID, usersData.get(uID), this);
                }
                else
                    throw new CardActivatedException();    //////////////////////////////////////////////////////////////////////
                // here you have to notify the client that he can't activate the card another time
                // instead of throwing an exception
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////
            }
        }
        model.cloudEmptier(uID, index);

        if(decorationFlag) {
            model = new Model(model);
            decorationFlag=false;
        }
        cardActivated=false;
    }
}
