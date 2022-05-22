package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Exceptions.CardActivatedException;
import it.polimi.ingsw.Controller.Exceptions.ConnectionErrorException;
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
    private final List<Integer> chosenClouds; //list used to remember the indexes of the cloud already chosen by another
                                        //player in this round

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
        chosenClouds=new ArrayList<>();
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
            while(!endGame)
            {
                cloudsFilling();
                cardsPhase();
                for(String player: uIDsList)
                {
                    model.setCurrentPlayer(player);
                    moveStudents(player);
                    moveMN(player);
                    if(!endGame)
                        chooseCloud(player);
                }
                chosenClouds.clear();
            }
        }
        catch(ConnectionErrorException e)
        {
            model.errorMessage("A player disconnected", true);
        }
        catch (Exception e)
        {
            System.out.println("An exception occurred"+e.getClass());
            model.errorMessage("An unexpected error occurred", true);
        }
        model.endGame();
        System.out.println("Closing game");
    }
    void cloudsFilling()
    {
        int tmp=uIDsList.size()%2;
        // if there are 2 or 4 players the clouds must be filled with 3 students
        // if there are 3 players the clouds must be filled with 4 students
        try {
            model.cloudsFiller(tmp*4 + (1-tmp)*3);
        }catch(RunOutOfStudentsException e)
        { endGame=true; }
    }
    void cardsPhase() throws InterruptedException, NoSuchPlayerException, ConnectionErrorException
    {
        List<String> order=new ArrayList<>();  // returning List
        // SortedMap used to save player and his card round value in order, if there is more than
        // one player which played the same card it is saved (in order of first played to last one)
        // in the list contained in the value of the map
        SortedMap<Integer, List<String>> playersOrder=new TreeMap<>();
        int pos= -1;
        List<Integer> playerCards;
        for(String currPlayer:uIDsList)
        {
            pos= -1;
            model.setCurrentPlayer(currPlayer);
            playerCards= model.getCardsRoundValues(currPlayer);
            if(playerCards.size()==1)
                endGame=true;
            while (pos==-1)
            {
                pos = usersData.get(currPlayer).getCardPos();
                if(pos<0 || pos>=playerCards.size())
                {
                    pos=-1;
                    model.errorMessage("There is no card with the chosen index", false);
                }
            }
            // check if the card was already played by someone else in this round
            boolean finished= false;    // flag used to end the external loop
            boolean noOtherCard= true;  // flag used to end the internal loop
            while(playersOrder.containsKey(playerCards.get(pos)) && !finished)
            {
                noOtherCard=true;
                while (noOtherCard)
                {
                    for(int i: playerCards)
                        if(!playersOrder.containsKey(i))
                            noOtherCard=false;
                    if(noOtherCard)
                    {
                        playersOrder.get(model.cardDiscarder(currPlayer, pos).getRoundValue()).add(currPlayer);
                        noOtherCard=false;
                        finished=true;
                    }
                    else
                    {
                        model.errorMessage("This card was already chosen by someone else in this turn, "
                                        +"please choose another one", false);
                        pos = usersData.get(currPlayer).getCardPos();
                    }
                }
            }
            if(!finished)   // if this card wasn't played by anyone else
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
    void moveStudents(String uID) throws InterruptedException, ConnectionErrorException
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
                    {
                        target = usersData.get(uID).getTarget();
                    }
                    colour = usersData.get(uID).getStudentColour();
                }
                catch (CardActivatedException e)
                {
                    if (!cardActivated)
                    {
                        cardActivated=true;
                        try {
                            model.activateCard(uID, usersData.get(uID), this);
                        }
                        catch (Exception e1)
                        {
                            model.errorMessage("An error occurred during the activation of the card, "
                                        +"please try again or move on", false);
                            cardActivated=false;
                        }
                    }
                    else
                    {
                        model.errorMessage("Another card was already activated in this turn", false);
                    }
                }
            }
            if(target)
            {
                try {
                    model.addStudentDashboard(uID, model.entranceEmptier(uID, colour));
                }
                catch(NoSuchStudentException e)
                {
                    model.errorMessage("No such student with that colour in your entrance", false);
                    i--;
                }
                catch(FullClassException e)
                {
                    model.errorMessage("The classroom is full!", false);
                    i--;
                }
                catch(Exception e)
                {
                    throw new RuntimeException();
                }
            }
            else
            {
                index=-1;
                while (index==-1)
                {
                    try {
                        index= usersData.get(uID).getIslandPos();
                    }
                    catch (CardActivatedException e)
                    {
                        if (!cardActivated)
                        {
                            cardActivated=true;
                            try {
                                model.activateCard(uID, usersData.get(uID), this);
                            }
                            catch (Exception e1)
                            {
                                model.errorMessage("An error occurred during the activation of the card, "
                                        +"please try again or move on", false);
                                cardActivated=false;
                            }
                        }
                        else
                        {
                            model.errorMessage("Another card was already activated in this turn", false);
                        }
                    }
                }
                try {
                    model.addStudentIsland(index, model.entranceEmptier(uID, colour));
                }
                catch(NoSuchStudentException e)
                {
                    model.errorMessage("No such student with that colour in your entrance", false);
                    i--;
                }
                catch(IndexOutOfBoundsException e)
                {
                    model.errorMessage("No such island with that index", false);
                    i--;
                }
                catch(Exception e)
                {
                    throw new RuntimeException();
                }
            }
        }
    }
    void moveMN(String uID) throws InterruptedException, EmptyException, NoSuchPlayerException, ConnectionErrorException
    {
        boolean badChoice=true; // flag used in case the user asked for an unacceptable movement of mother
                                // nature, in this case we ask again to choose a movement
        while(badChoice)
        {
            badChoice=false;
            int newPos=-1;
            while (newPos==-1)
            {
                try {
                    newPos = usersData.get(uID).getMnPos();
                }
                catch (CardActivatedException e)
                {
                    if (!cardActivated)
                    {
                        cardActivated=true;
                        try {
                            model.activateCard(uID, usersData.get(uID), this);
                        }
                        catch (Exception e1)
                        {
                            model.errorMessage("An error occurred during the activation of the card, "
                                    +"please try again or move on", false);
                            cardActivated=false;
                        }
                    }
                    else
                    {
                        model.errorMessage("Another card was already activated in this turn", false);
                    }
                }
            }
            int oldPos= model.getCurrPosMN();
            try
            {
                if(newPos==oldPos)
                    throw new IllegalMNMovementException();
                int delta_pos;
                if(newPos>oldPos)
                    delta_pos= newPos-oldPos;
                else
                    delta_pos= model.getNumIslands()-oldPos+newPos;
                if(delta_pos > model.getLastCardValue(uID) )
                    throw new IllegalMNMovementException();
                try {
                    model.moveMN(delta_pos);
                }catch(RunOutOfTowersException e)
                {
                    endGame=true;
                }
                catch(IndexOutOfBoundsException e)
                {
                    throw new IllegalMNMovementException();
                }
                catch(Exception e)
                {
                    throw new RuntimeException();
                }
                if(model.getNumIslands()<=3)
                    endGame=true;
            }catch(IllegalMNMovementException e)
            {
                model.errorMessage("Unacceptable new mother nature position, please choose again",
                                    false);
                badChoice=true;
            }
        }
    }
    void chooseCloud(String uID) throws InterruptedException, ConnectionErrorException
    {
        boolean badChoice=true; // flag used in case the user asked for an unacceptable could index,
                                // in this case we ask again to choose a cloud
        while(badChoice)
        {
            badChoice=false;
            int index=-1;
            while (index==-1)
            {
                try {
                    index = usersData.get(uID).getCloudPos();
                    if(chosenClouds.contains(index))
                    {
                        model.errorMessage("This cloud was already taken, please choose again",
                                            false);
                        index=-1;   // repeat the loop and the choice
                    }
                    else
                        chosenClouds.add(index);
                }
                catch (CardActivatedException e)
                {
                    if (!cardActivated)
                    {
                        cardActivated=true;
                        try {
                            model.activateCard(uID, usersData.get(uID), this);
                        }
                        catch (Exception e1)
                        {
                            model.errorMessage("An error occurred during the activation of the card, "
                                    +"please try again or move on", false);
                            cardActivated=false;
                        }
                    }
                    else
                    {
                        model.errorMessage("Another card was already activated in this turn", false);
                    }
                }
            }
            try {
                model.cloudEmptier(uID, index);
            }catch(NoSuchPlayerException|FullEntranceException e)
            {
                throw new RuntimeException();
            }
            catch(IndexOutOfBoundsException e)
            {
                model.errorMessage("There is no cloud with that index, please choose again", false);
                badChoice=true;
            }
        }

        if(decorationFlag) {
            model = new Model(model);
            decorationFlag=false;
        }
        cardActivated=false;
    }
    public void notifyDisconnection()
    {
        for(DataBuffer dataBuffer: usersData.values())
            dataBuffer.setErrorStatus();
    }
}
