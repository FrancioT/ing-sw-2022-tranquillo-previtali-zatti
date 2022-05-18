package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Exceptions.CardActivatedException;
import it.polimi.ingsw.Controller.Exceptions.IllegalMNMovementException;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;
import it.polimi.ingsw.RemoteView.RemoteView;

import java.util.*;

public class Controller extends Thread
{
    private boolean endGame;
    private List<String> uIDsList;
    private final Map<String, DataBuffer> usersData;
    private Model model;
    private boolean decorationFlag;
    private boolean cardActivated;

    public Controller(Map<String, DataBuffer> users, boolean expertMode, List<RemoteView> views)
    {
        endGame=false;
        uIDsList= new ArrayList<>(users.keySet());
        model= new Model(new ArrayList<>(uIDsList), expertMode);
        for(RemoteView v: views)
            model.addPropertyChangeListener(v);
        decorationFlag=false;
        usersData=new HashMap<>(users);
        cardActivated=false;
    }
    public synchronized Model getModel() { return model; }
    public synchronized void decorateModel(Model model)
    {
        this.model=model;
        decorationFlag=true;
    }
    @Override
    public synchronized void run()
    {
        try {
            cloudsFilling();
            cardsPhase();
            for(String player: uIDsList)
            {
                model.setCurrentPlayer(player);
                moveStudents(player);
                moveMN(player);
            }
            if(endGame)
            { /*client.sendMessage("Game ended");*/ }  ///////////////////////////////////////////////////////////////////////
        }
        catch (Exception e) { /*client.sendMessage("Unexpected error!");*/ } /////////////////////////////////////////////////
    }
    void cloudsFilling()
    {
        int tmp=uIDsList.size()%2;
        // if there are 2 or 4 players the clouds must be filled with 3 students
        // if there are 3 players the clouds must be filled with 4 students
        model.cloudsFiller(tmp*4 + (1-tmp)*3);
    }
    void cardsPhase() throws NoSuchPlayerException, InterruptedException
    {
        List<String> order=new ArrayList<>();  // returning List
        // SortedMap used to save player and his card round value in order, if there is more than
        // one player which played the same card it is saved (in order of first played to last one)
        // in the list contained in the value of the map
        SortedMap<Integer, List<String>> playersOrder=new TreeMap<>();
        int pos;
        List<Integer> playerCards;
        boolean noOtherCard;
        for(String currPlayer:uIDsList)
        {
            model.setCurrentPlayer(currPlayer);
            noOtherCard=true;
            playerCards= model.getCardsRoundValues(currPlayer);
            if(playerCards.size()==1)
                endGame=true;
            pos= usersData.get(currPlayer).getCardPos();
            // check if the card was already played by someone else in this round
            if(playersOrder.containsKey(playerCards.get(pos)))
                while (noOtherCard)
                {
                    for(int i: playerCards)
                        if(!playersOrder.containsKey(i))
                            noOtherCard=false;
                    if(noOtherCard)
                    {
                        playersOrder.get(model.cardDiscarder(currPlayer, pos).getRoundValue()).add(currPlayer);
                        noOtherCard=false;
                    }
                    else
                    {
                        noOtherCard=true;  // repeats the loop until the player choose a good card
                        // client.send("chose another card!") ////////////////////////////////////////////////////////////////
                        pos = usersData.get(currPlayer).getCardPos();
                    }
                }
            else
            {
                List<String> tmp= new ArrayList<>();
                tmp.add(currPlayer);
                playersOrder.put(model.cardDiscarder(currPlayer, pos).getRoundValue(), tmp);
            }
        }
        // add players uIDs in proper order thanks to the iterator of TreeMap, which iterate in the ascending order
        // of the keys, and to the addAll which adds all element (at the end) in the old order of the collection
        for(List<String> s: playersOrder.values())
            order.addAll(s);
        uIDsList= order;
    }
    void moveStudents(String uID) throws Exception
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
                        throw new CardActivatedException();    ///////////////////////////////////////////////////////////////
                        // here you have to notify the client that he can't activate the card another time
                        // instead of throwing an exception
                        //////////////////////////////////////////////////////////////////////////////////////////////////////
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
                            throw new CardActivatedException();    ///////////////////////////////////////////////////////////
                        // here you have to notify the client that he can't activate the card another time
                        // instead of throwing an exception
                        //////////////////////////////////////////////////////////////////////////////////////////////////////
                    }
                }
                model.addStudentIsland(index, model.entranceEmptier(uID, colour));
            }
        }
    }
    void moveMN(String uID) throws Exception
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
                    throw new CardActivatedException();    ///////////////////////////////////////////////////////////////////
                // here you have to notify the client that he can't activate the card another time
                // instead of throwing an exception
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
    void chooseCloud(String uID) throws Exception
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
                    throw new CardActivatedException();    ///////////////////////////////////////////////////////////////////
                // here you have to notify the client that he can't activate the card another time
                // instead of throwing an exception
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
