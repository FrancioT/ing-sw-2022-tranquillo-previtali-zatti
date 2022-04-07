package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Exceptions.IllegalMNMovementException;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;
import it.polimi.ingsw.Model.Student;

import java.util.*;

public class Controller
{
    private List<String> uIDsList=new ArrayList<>();
    private Model model;
    boolean decorationFlag=false;

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
        int pos=0;
        int cardValue=0;
        for(String s: uIDsOrder)
        {

            //ricezione input da ogni giocatore

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
    public synchronized void moveStudents(String uID) throws NoSuchStudentException, EmptyException, FullClassException,
                                                NoSuchPlayerException, TooManyTeachersException,
                                                TeacherAlreadyInException, NoSuchTeacherException
    {
        int n=uIDsList.size()%2;  // n=number of students that can be moved
        n=n*4 + (1-n)*3;       // n=3 if number of players is 2 or 4
                               // n=4 if number of players is 3
        List<Colour> usedColours=new ArrayList<>();
        boolean target=true;
        Colour colour=null;
        int index=0;
        Student s;
        for(int i=0; i<n; i++)
        {

            // input di target, colore studente e nel caso indice isola

            if(target)
            {
                if(!usedColours.contains(colour))
                    usedColours.add(colour);
                model.addStudentDashboard(uID, model.entranceEmptier(uID, colour));
            }
            else
                model.addStudentIsland(index, model.entranceEmptier(uID, colour));
        }
        for(Colour c: usedColours)
            model.teacherDominance(uID, c);
    }
    public synchronized void moveMN(String uID) throws NoSuchPlayerException, IllegalMNMovementException,
                                          FullTowersException, RunOutOfTowersException, EmptyException
    {
        int pos=0;

        // input di nuova posizione madre natura

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
        int index=0;

        // input dell'indice dalla nuvola scelta

        model.cloudEmptier(uID, index);

        if(decorationFlag) {
            model = new Model(model);
            decorationFlag=false;
        }
    }
}
