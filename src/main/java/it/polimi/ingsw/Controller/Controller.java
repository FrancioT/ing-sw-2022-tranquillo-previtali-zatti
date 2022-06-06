package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Exceptions.CardActivatedException;
import it.polimi.ingsw.Controller.Exceptions.ConnectionErrorException;
import it.polimi.ingsw.Controller.Exceptions.IllegalMNMovementException;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;
import it.polimi.ingsw.Model.ModelAndDecorators.Phase;
import it.polimi.ingsw.Model.Student;
import it.polimi.ingsw.RemoteView.RemoteView;

import java.util.*;

public class Controller extends Thread
{
    private boolean endGame;   // flag used if an end game condition is met
    private boolean breakEnd;  // flag used if an end game condition which must instantly stop the game is met
    protected List<String> uIDsList;
    private final Map<String, DataBuffer> usersData;
    private Model model;
    private boolean decorationFlag;
    private boolean cardActivated;
    private final List<Integer> chosenClouds; // list used to remember the indexes of the cloud already chosen by
                                              // another player in this round

    /**
     * Constructor of controller
     * @param users is a Map with the players and their respective DataBuffer
     * @param expertMode true for expert mode, false for standard mode
     * @param views the players' views
     */
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

    /**
     * Method that decorate the model so that some character cards can be used
     * @param model the model on which the game is based
     */
    public synchronized void decorateModel(Model model)
    {
        this.model=model;
        decorationFlag=true;
    }

    /**
     * Method that starts the game and manages the phases of the match (e.g. cards phase, move students phase,  move MN
     * phase, choose cloud phase, ...). It also catches major exceptions that would make the game end.
     */
    @Override
    public synchronized void run()
    {
        try {
            while(!endGame)
            {
                model.setPhase(Phase.choose_card);
                cloudsFilling();
                cardsPhase();
                for(String player: uIDsList)
                {
                    model.setCurrentPlayer(player);
                    model.setPhase(Phase.move_students);
                    moveStudents(player);
                    model.setPhase(Phase.move_mother_nature);
                    if(breakEnd)
                        break;
                    moveMN(player);
                    if(!endGame)
                    {
                        model.setPhase(Phase.choose_cloud);
                        chooseCloud(player);
                    }
                    if(breakEnd)
                        break;
                }
                // clears all dataBuffers in order to remove the pre-move functionality
                for(DataBuffer dataBuffer: usersData.values())
                    dataBuffer.clear();
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

    /**
     * Method that fills the clouds with a precise number of random students
     */
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

    /**
     * Method that manages the phase where all the players have to play a card. This method in a first instance waits
     * for all the players to play a card and checks if the cards are acceptable; then, based on the value of the card,
     * it calculates the order of the players for the next phases of the round.
     */
    void cardsPhase() throws InterruptedException, NoSuchPlayerException, ConnectionErrorException
    {
        List<String> order=new ArrayList<>();  // returning List

        // SortedMap is used to save players and their card's round value in order; if there is more than
        // one player who played the same card it checks whether is the only card they can play; if it's their last
        // card it is accepted, otherwise the player that discarded it after has to change it.

        // in this first part the controller waits for the players to discard an acceptable card
        SortedMap<Integer, List<String>> playersOrder=new TreeMap<>();
        int pos= -1;
        List<Integer> playerCards;
        for(String currPlayer:uIDsList)
        {
            pos= -1;
            model.setCurrentPlayer(currPlayer);
            playerCards= model.getCardsRoundValues(currPlayer);
            // if the player has only one card the endgame phase is activated
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

            // check if the card was already played by someone else in this round and if there is no other choice;
            // depending on the case it accepts it, or it refuses it
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

    /**
     * Method that manages the move students phase. Firstly, it understands how many students need to be moved based
     * on how many players there are in the game. Then, it manages all the effective movements of the students, from an
     * entrance to a classroom or to an island
     * @param uID the player's uid who is doing the movements
     */
    void moveStudents(String uID) throws InterruptedException, ConnectionErrorException
    {
        int n=uIDsList.size()%2;  // n=number of students that can be moved
        n=n*4 + (1-n)*3;       // n=3 if number of players is 2 or 4
                               // n=4 if number of players is 3
        Boolean target;
        Colour colour;
        int index=-1;
        // for loop repeated until the player ends his possible moves
        for(int i=0; i<n; i++)
        {
            target=null;
            colour=null;
            // in this part the method understands which student the player wants to move and where he wants to move it
            while (target==null || colour==null)
            {
                try {
                    if(target==null)
                    {
                        target = usersData.get(uID).getTarget();
                    }
                    colour = usersData.get(uID).getStudentColour();
                }

                // in case a character card is activated this method temporarily stops and tries to activate the card;
                // then, it re-start from where it stopped
                catch (CardActivatedException e)
                {
                    if (!cardActivated)
                    {
                        cardActivated=true;
                        try {
                            model.activateCard(uID, usersData.get(uID), this);
                            if(model.getNumIslands()<=3)
                            {
                                breakEnd= true;
                                endGame=true;
                                return;
                            }
                        }
                        catch (RunOutOfTowersException e1) {
                            breakEnd= true;
                            endGame=true;
                            return;
                        }
                        catch (RunOutOfStudentsException e1) {
                            endGame=true;
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

            // here the method manages the case where the student needs to be moved in a classroom
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
                    // re-adding the student to the entrance
                    List<Student> removedStudent= new ArrayList<>();
                    removedStudent.add(new Student(colour));
                    try {
                        model.entranceFiller(uID, removedStudent);
                    }catch (NoSuchPlayerException|FullEntranceException e1){ throw new RuntimeException(); }
                    i--;
                }
                catch(Exception e)
                {
                    throw new RuntimeException();
                }
            }

            // in this part the method manages the case where the student needs to be moved on an island
            else
            {
                index=-1;
                while (index==-1)
                {
                    try {
                        index= usersData.get(uID).getIslandPos();
                    }
                    // in case a character card is activated this method temporarily stops and tries to activate the card;
                    // then, it re-start from where it stopped
                    catch (CardActivatedException e)
                    {
                        if (!cardActivated)
                        {
                            cardActivated=true;
                            try {
                                model.activateCard(uID, usersData.get(uID), this);
                                if(model.getNumIslands()<=3)
                                {
                                    breakEnd= true;
                                    endGame=true;
                                    return;
                                }
                            }
                            catch (RunOutOfTowersException e1) {
                                breakEnd= true;
                                endGame=true;
                                return;
                            }
                            catch (RunOutOfStudentsException e1) {
                                endGame=true;
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
                    // re-adding the student to the entrance
                    List<Student> removedStudent= new ArrayList<>();
                    removedStudent.add(new Student(colour));
                    try {
                        model.entranceFiller(uID, removedStudent);
                    }catch (NoSuchPlayerException|FullEntranceException e1){ throw new RuntimeException(); }
                    i--;
                }
                catch(Exception e)
                {
                    throw new RuntimeException();
                }
            }
        }
    }

    /**
     * This method manages the MN movement's phase; it waits for the player's choice and if acceptable moves MN,
     * otherwise notifies the player and waits for a correct value
     * @param uID the player's uid who is moving MN
     */
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

                // in case a character card is activated this method temporarily stops and tries to activate the card;
                // then, it re-start from where it stopped
                catch (CardActivatedException e)
                {
                    if (!cardActivated)
                    {
                        cardActivated=true;
                        try {
                            model.activateCard(uID, usersData.get(uID), this);
                            if(model.getNumIslands()<=3)
                            {
                                breakEnd= true;
                                endGame=true;
                                return;
                            }
                        }
                        catch (RunOutOfTowersException e1) {
                            breakEnd= true;
                            endGame=true;
                            return;
                        }
                        catch (RunOutOfStudentsException e1) {
                            endGame=true;
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

            // in this parte the method manages the MN's movement
            int oldPos= model.getCurrPosMN();
            try
            {
                if(newPos==oldPos && model.getNumIslands()>model.getLastCardValue(uID))
                    throw new IllegalMNMovementException();
                int delta_pos;
                if(newPos>=oldPos)
                    delta_pos= newPos-oldPos;
                else
                    delta_pos= model.getNumIslands()-oldPos+newPos;
                if(delta_pos > model.getLastCardValue(uID) )
                    throw new IllegalMNMovementException();
                try {
                    model.moveMN(delta_pos);
                }catch(RunOutOfTowersException e)
                {
                    breakEnd= true;
                    endGame=true;
                    return;
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
                {
                    breakEnd= true;
                    endGame=true;
                }
            }catch(IllegalMNMovementException e)
            {
                model.errorMessage("Unacceptable new mother nature position, please choose again",
                                    false);
                badChoice=true;
            }
        }
    }

    /**
     * Method that manages the chooseCloud phase
     * @param uID the player's uid who is making the choice
     */
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

                // in case a character card is activated this method temporarily stops and tries to activate the card;
                // then, it re-start from where it stopped
                catch (CardActivatedException e)
                {
                    if (!cardActivated)
                    {
                        cardActivated=true;
                        try {
                            model.activateCard(uID, usersData.get(uID), this);
                            if(model.getNumIslands()<=3)
                            {
                                breakEnd= true;
                                endGame=true;
                                return;
                            }
                        }
                        catch (RunOutOfTowersException e1) {
                            breakEnd= true;
                            endGame=true;
                            return;
                        }
                        catch (RunOutOfStudentsException e1) {
                            endGame=true;
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

            // in this part the method empties the cloud and fills the player's entrance
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

        // in this part (which is the end of the player's turn), in case he activated a character card which uses a
        // decorator, the method reset the model to its "standard" values
        if(decorationFlag) {
            model = new Model(model);
            decorationFlag=false;
        }
        cardActivated=false;
    }

    /**
     * Method that is used to notify the disconnection of a player
     */
    public void notifyDisconnection()
    {
        for(DataBuffer dataBuffer: usersData.values())
            dataBuffer.setErrorStatus();
    }
}
