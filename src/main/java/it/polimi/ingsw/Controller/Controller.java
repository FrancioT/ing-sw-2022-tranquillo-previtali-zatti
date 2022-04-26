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
    private boolean decorationFlag;

    public Controller(Map<String, DataBuffer> uIDs, boolean expertMode)
    {
        model= new Model(new ArrayList<>(uIDs.keySet()), expertMode);
        uIDsList= new ArrayList<>(uIDs.keySet());
        decorationFlag=false;
        usersData=new HashMap<>(uIDs);
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
        int pos=-1;
        int cardValue=-1;
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
    public synchronized void moveStudents(String uID) throws NoSuchStudentException, EmptyException,
                                            FullClassException, NoSuchPlayerException, TooManyTeachersException,
                                            TeacherAlreadyInException, NoSuchTeacherException, InterruptedException
    {
        int n=uIDsList.size()%2;  // n=number of students that can be moved
        n=n*4 + (1-n)*3;       // n=3 if number of players is 2 or 4
                               // n=4 if number of players is 3
        Boolean target=null;
        Colour colour=null;
        int index=-1;
        for(int i=0; i<n; i++)
        {
            target= usersData.get(uID).getTarget();
            colour = usersData.get(uID).getStudentColour();
            if(target)
                model.addStudentDashboard(uID, model.entranceEmptier(uID, colour));
            else
            {
                index= usersData.get(uID).getIslandPos();
                model.addStudentIsland(index, model.entranceEmptier(uID, colour));
            }
        }
    }
    public synchronized void moveMN(String uID) throws NoSuchPlayerException, IllegalMNMovementException,
                                                       FullTowersException, RunOutOfTowersException,
                                                       EmptyException, LinkFailedException, InterruptedException
    {
        int newPos= usersData.get(uID).getMnPos();
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
    public synchronized void chooseCloud(String uID) throws FullEntranceException, NoSuchPlayerException,
                                                            InterruptedException
    {
        int index= usersData.get(uID).getCloudPos();
        model.cloudEmptier(uID, index);

        if(decorationFlag) {
            model = new Model(model);
            decorationFlag=false;
        }
    }
}
