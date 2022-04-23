package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Exceptions.IllegalMNMovementException;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;

import java.util.*;

public class Controller
{
    private List<String> uIDsList;
    private final Map<String, DataBuffer> usersData;
    private Model model;
    boolean decorationFlag;

    public Controller(List<String> uIDs, boolean expertMode)
    {
        model= new Model(uIDs, expertMode);
        uIDsList= new ArrayList<>(uIDs);
        decorationFlag=false;
        usersData=new HashMap<>();
        for(String s: uIDsList)
            usersData.put(s, new DataBuffer(s));
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
    public synchronized List<String> cardsPhase(List<String> uIDsOrder) throws NoSuchPlayerException
    {
        List<String> order=new ArrayList<>();  // returning List
        Map<String, Integer> posCards= new HashMap<>();  // used to map a player to his
                                                         // played card position
        // SortedMap used to save player and his card round value in order
        SortedMap<Integer, String> playersOrder=new TreeMap<>();
        int pos=-1;
        int cardValue=0;
        for(String s: uIDsOrder)
        {
            while(pos==-1)
            {
                try {
                    pos = usersData.get(s).getCardPos();
                } catch (EmptyException e) {
                    try {
                        usersData.get(s).wait();
                    } catch (InterruptedException ignored) {}
                }
            }
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
    public synchronized void moveStudents(String uID) throws NoSuchStudentException, EmptyException,
                                            FullClassException, NoSuchPlayerException, TooManyTeachersException,
                                            TeacherAlreadyInException, NoSuchTeacherException
    {
        int n=uIDsList.size()%2;  // n=number of students that can be moved
        n=n*4 + (1-n)*3;       // n=3 if number of players is 2 or 4
                               // n=4 if number of players is 3
        Boolean target=null;
        Colour colour=null;
        int index=-1;
        for(int i=0; i<n; i++)
        {
            while(colour==null || target==null)
            {
                try {
                    if(target==null)
                        target = usersData.get(uID).getTarget();
                    colour = usersData.get(uID).getStudentColour();
                } catch (EmptyException e) {
                    try {
                        usersData.get(uID).wait();
                    } catch (InterruptedException ignored) {}
                }
            }

            if(target)
                model.addStudentDashboard(uID, model.entranceEmptier(uID, colour));
            else
            {
                while(index==-1)
                {
                    try {
                        index = usersData.get(uID).getIslandPos();
                    } catch (EmptyException e) {
                        try {
                            usersData.get(uID).wait();
                        } catch (InterruptedException ignored) {}
                    }
                }
                model.addStudentIsland(index, model.entranceEmptier(uID, colour));
            }
        }
    }
    public synchronized void moveMN(String uID) throws NoSuchPlayerException, IllegalMNMovementException,
                                                       FullTowersException, RunOutOfTowersException,
                                                       EmptyException, LinkFailedException
    {
        int pos=-1;

        while (pos==-1)
        {
            try {
                pos=usersData.get(uID).getMnPos();
            } catch (EmptyException e){
                try {
                    usersData.get(uID).wait();
                } catch (InterruptedException ignored) {}
            }
        }

        int delta_pos=pos - model.getCurrPosMN();
        if(delta_pos > model.getLastCardValue(uID) )
            throw new IllegalMNMovementException();
        if(delta_pos == 0)
            throw new IllegalMNMovementException();
        model.moveMN(delta_pos);
    }
    public synchronized void chooseCloud(String uID) throws FullEntranceException,
                                                            NoSuchPlayerException
    {
        int index=-1;

        while (index==-1)
        {
            try {
                index=usersData.get(uID).getCloudPos();
            } catch (EmptyException e){
                try {
                    usersData.get(uID).wait();
                } catch (InterruptedException ignored) {}
            }
        }

        model.cloudEmptier(uID, index);

        if(decorationFlag) {
            model = new Model(model);
            decorationFlag=false;
        }
    }
}
