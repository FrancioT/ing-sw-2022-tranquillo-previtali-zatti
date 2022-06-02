package it.polimi.ingsw.Model.ModelAndDecorators;
import it.polimi.ingsw.ClientsHandler.Messages.ExceptionMessage;
import it.polimi.ingsw.ClientsHandler.Messages.ModelMessage;
import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.CharacterCard.*;
import it.polimi.ingsw.Model.Exceptions.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.stream.Collectors;

public class Model {
    protected List<Teacher> teachersList;
    protected List<Island> islandsList;
    protected List<Cloud> cloudsList;
    protected List<Player> playersList;
    protected MotherNature motherNature;
    protected List<CharacterCard> characterCardList;
    protected int unusedCoins;
    protected CharacterCard5 card5;
    protected Player currentPlayer;
    protected Phase phase;
    protected final PropertyChangeSupport support;

    /**
     * Main constructor of the model
     * @param uIDs a list of the players nicknames with no repetition (each name must be unique)
     * @param expertMode flag which indicates if the game is in expert mode or not
     */
    public Model(List<String> uIDs, boolean expertMode)
    {
        int characterCardNum=12;
        phase= Phase.choose_card;
        support= new PropertyChangeSupport(this);
        teachersList=new ArrayList<>();
        islandsList=new ArrayList<>();
        cloudsList=new ArrayList<>();
        playersList=new ArrayList<>();
        // creation of players and towers
        Stack<Towers> towersList= new Stack<>();
        int mod=uIDs.size()%2;
        for(ColourT c: ColourT.values())
            towersList.push(new Towers(c, mod));
        for(int i=0; i<uIDs.size(); i++)
        {
            Towers towers= towersList.pop();
            playersList.add(new Player(uIDs.get(i), towers, this));
            if(uIDs.size()==4 && i%2==0)
                towersList.push(towers);
        }
        currentPlayer=playersList.get(0);
        // creation of islands and mother nature
        Bag bag=new Bag();
        islandsList.add(new Island(true, this));
        for(int i=1; i<12; i++)
        {
            if (i != 6)
            {
                try{ islandsList.add(new Island(bag.randomExtraction(), this)); }
                catch(RunOutOfStudentsException e) { throw new RuntimeException(); }
            }
            else
                islandsList.add(new Island(false, this));
        }
        motherNature=new MotherNature(islandsList.get(0));
        // creation of clouds and bag
        bag=new Bag(bag);
        for(int i=0; i<uIDs.size(); i++)
            cloudsList.add(new Cloud(bag));
        // creation of teachers
        for(Colour c: Colour.values())
            teachersList.add(new Teacher(c));

        //initial filling of Entrances
        List<Student> entranceStudents = new ArrayList<>();
        int mode=uIDs.size()%2;
        for(String uID : uIDs){
            for(int i = 0; i < 7*(1-mode)+9*mode; i++)
            {
                try{ entranceStudents.add(bag.randomExtraction()); }
                catch(RunOutOfStudentsException e) { throw new RuntimeException(); }
            }
            try { entranceFiller(uID, entranceStudents); }
            catch (FullEntranceException | NoSuchPlayerException e)
            { throw new RuntimeException("Failed while filling entrances"); }
            entranceStudents.clear();
        }

        // expert mode
        unusedCoins=0;
        characterCardList=new ArrayList<>();
        if(expertMode)
        {
            unusedCoins=20-playersList.size();
            List<CharacterCard> cardListTmp= new ArrayList<>();
            try{ cardListTmp.add(new CharacterCard1(bag)); }
            catch(RunOutOfStudentsException e) { throw new RuntimeException(); }
            cardListTmp.add(new CharacterCard2());
            cardListTmp.add(new CharacterCard3());
            cardListTmp.add(new CharacterCard4());
            cardListTmp.add(new CharacterCard5());
            cardListTmp.add(new CharacterCard6());
            try{ cardListTmp.add(new CharacterCard7(bag)); }
            catch(RunOutOfStudentsException e) { throw new RuntimeException(); }
            cardListTmp.add(new CharacterCard8());
            cardListTmp.add(new CharacterCard9());
            cardListTmp.add(new CharacterCard10());
            try{ cardListTmp.add(new CharacterCard11(bag)); }
            catch(RunOutOfStudentsException e) { throw new RuntimeException(); }
            cardListTmp.add(new CharacterCard12(bag, new ArrayList<>(playersList)));
            int randIndex1= (int)Math.floor(Math.random()*characterCardNum);
            int randIndex2, randIndex3;
            characterCardList.add(cardListTmp.get(randIndex1));
            do randIndex2= (int)Math.floor(Math.random()*characterCardNum);
            while(randIndex2==randIndex1);
            characterCardList.add(cardListTmp.get(randIndex2));
            do randIndex3= (int)Math.floor(Math.random()*characterCardNum);
            while(randIndex3==randIndex1 || randIndex3==randIndex2);
            characterCardList.add(cardListTmp.get(randIndex3));
        }
    }

    /**
     * Constructor used in the decorator pattern by the character cards
     * @param model
     */
    public Model(Model model)
    {
        teachersList=new ArrayList<>(model.teachersList);
        islandsList=new ArrayList<>(model.islandsList);
        cloudsList=new ArrayList<>(model.cloudsList);
        playersList=new ArrayList<>(model.playersList);
        characterCardList=new ArrayList<>(model.characterCardList);
        motherNature=model.motherNature;
        currentPlayer=model.currentPlayer;
        unusedCoins=model.unusedCoins;
        card5=model.card5;
        support=model.support;
    }

    /**
     * This method is used to detract the coins from the player after a card's activation
     * @param uID nickname of the player who activated the card
     * @param cardID id of the card activated
     * @throws NoSuchPlayerException thrown if the nickname passed doesn't correspond to any player's nickname
     * @throws NoSuchCardException thrown if the card's id passed doesn't correspond to any card present in
     * this game
     * @throws CardPaymentException thrown if the payment to the card failed due to the player's lack of coins
     */
    public synchronized void payCard(String uID, int cardID) throws NoSuchPlayerException, NoSuchCardException,
                                                                    CardPaymentException
    {
        if(characterCardList.size()==0) throw new IllegalAccessError();
        if(uID==null) throw new NullPointerException();

        Player player = null;
        for (Player p : playersList)
            if (p.getuID().equals(uID))
                player = p;
        if(player==null)
            throw new NoSuchPlayerException();
        int chardPos=-1;
        for(CharacterCard c: characterCardList)
            if(c.getCardID()==cardID)
                chardPos=characterCardList.indexOf(c);
        if(chardPos==-1)
            throw new NoSuchCardException();

        int price=characterCardList.get(chardPos).getPrice();
        player.pay(price);
        if(!characterCardList.get(chardPos).getOverprice())
            price--;
        unusedCoins+=price;
    }

    /**
     * This method checks if the teacher of the specified colour must be moved to the passed player
     * @param player player who can obtain the control over the specified teacher
     * @param colour colour of the teacher
     * @throws TooManyTeachersException thrown if the specified player already has all the teachers and the method
     * tries to add another one
     * @throws TeacherAlreadyInException thrown if the method tries to move the teacher to the specified player, but
     * he already has that teacher
     * @throws NoSuchTeacherException thrown if there is no teacher for the specified colour
     */
    protected synchronized void teacherDominance(Player player, Colour colour) throws TooManyTeachersException,
                                                                                      TeacherAlreadyInException,
                                                                                      NoSuchTeacherException
    {
        if(colour==null || player==null) throw new NullPointerException();

        Player player2;
        int num1, num2;
        Teacher teacher = null;

        if (!player.checkTeacherPresence(colour) && player.getStudentNum(colour)>0){
            for(Teacher t : teachersList)
                if(t.getColour() == colour)
                    teacher = t;
            if(teacher==null)
                throw new NoSuchTeacherException();
            player2 = teacher.getCurrentPos();
            if(player2==null) {
                player.addTeacher(teacher);
                teacher.setNewPos(player);
            }
            else {
                num1 = player.getStudentNum(colour);
                num2 = player2.getStudentNum(colour);
                if (num1 > num2) {
                    player.addTeacher(player2.removeTeacher(colour));
                    teacher.setNewPos(player);
                }
            }
        }
    }

    /**
     * This method add a student to the classrooms of the specified player
     * @param uID nickname of the player
     * @param student student who must be added
     * @throws NoSuchPlayerException thrown if the nickname passed doesn't correspond to any player's nickname
     * @throws FullClassException thrown if the classroom representing that colour is already full of students of
     * that colour
     * @throws TooManyTeachersException thrown by the teacherDominance method called after having added the student
     * @throws TeacherAlreadyInException thrown by the teacherDominance method called after having added the student
     * @throws NoSuchTeacherException thrown by the teacherDominance method called after having added the student
     */
    public synchronized void addStudentDashboard(String uID, Student student)
                                                        throws NoSuchPlayerException, FullClassException,
                                                        TooManyTeachersException, TeacherAlreadyInException,
                                                        NoSuchTeacherException
    {
        if(uID==null) throw new NullPointerException();

        Player tmp = null;
        for (Player p : playersList)
            if (p.getuID().equals(uID))
                tmp = p;
        if(tmp==null)
            throw new NoSuchPlayerException();
        tmp.addStudent(student);
        teacherDominance(tmp, student.getColour());

        ModelMessage message= new ModelMessage(characterCardList.size()!=0, null, null,
                                               new ArrayList<>(playersList), null, currentPlayer.getuID(),
                                               unusedCoins, false, phase);
        notify(message);
    }

    /**
     * This method add a student to a specified island
     * @param index position of the island
     * @param student student who must be added
     * @throws IndexOutOfBoundsException thrown if there is no island for the passed index
     */
    public synchronized void addStudentIsland(int index, Student student) throws IndexOutOfBoundsException
    {
        if(index>islandsList.size() || index<0)
            throw new IndexOutOfBoundsException();
        islandsList.get(index).addStudent(student);

        ModelMessage message= new ModelMessage(characterCardList.size()!=0, new ArrayList<>(islandsList),
                                               null, null, null, currentPlayer.getuID(),
                                               unusedCoins, false, phase);
        notify(message);
    }

    /**
     * This method fills the clouds with the specified number of students, who are taken from the bag
     * @param students_num number of students who must be added to all the clouds
     * @throws RunOutOfStudentsException thrown if the bag, from which we extract the students, is empty
     */
    public synchronized void cloudsFiller(int students_num) throws RunOutOfStudentsException
    {
        for(Cloud cloud : cloudsList){
            cloud.cloudFiller(students_num);
        }
        ModelMessage message= new ModelMessage(characterCardList.size()!=0, null,
                                               new ArrayList<>(cloudsList), null, null, currentPlayer.getuID(),
                                               unusedCoins, false, phase);
        notify(message);
    }

    /**
     * This method moves the all the students from the specified cloud to the specified player
     * @param uID nickname of the player
     * @param i_cloud index of the cloud
     * @throws FullEntranceException thrown if the entrance of the player is already full
     * @throws NoSuchPlayerException thrown if the nickname passed doesn't correspond to any player's nickname
     */
    public synchronized void cloudEmptier(String uID, int i_cloud) throws FullEntranceException,
                                                                          NoSuchPlayerException
    {
        if(uID==null) throw new NullPointerException();
        if(i_cloud>cloudsList.size() || i_cloud<0) throw new IndexOutOfBoundsException();

        Player tmp = null;
        for (Player p : playersList)
            if (p.getuID().equals(uID))
                tmp = p;
        if(tmp==null)
            throw new NoSuchPlayerException();
        cloudsList.get(i_cloud).cloudEmptier(tmp);

        List<Player> player_updated= new ArrayList<>();
        player_updated.add(tmp);
        ModelMessage message= new ModelMessage(characterCardList.size()!=0, null,
                                               new ArrayList<>(cloudsList), player_updated, null,
                                               currentPlayer.getuID(), unusedCoins, false, phase);
        notify(message);
    }

    /**
     * This method is used to move a card from the hand of the player to the discarded stack
     * @param uID nickname of the player
     * @param pos position of the card to discard
     * @return the card discarded
     * @throws NoSuchPlayerException thrown if the nickname passed doesn't correspond to any player's nickname
     */
    public synchronized StandardCard cardDiscarder(String uID, int pos) throws NoSuchPlayerException
    {
        if(uID==null) throw new NullPointerException();

        Player player=null;
        StandardCard s;
        for (Player p: playersList)
            if (p.getuID().equals(uID))
                player = p;
        if(player==null)
            throw new NoSuchPlayerException();
        s = player.cardDiscarder(pos);

        List<Player> player_updated= new ArrayList<>();
        player_updated.add(player);
        ModelMessage message= new ModelMessage(characterCardList.size()!=0,null,
                                               null, player_updated, null, currentPlayer.getuID(),
                                               unusedCoins, false, phase);
        notify(message);
        return s;
    }

    /**
     * This method remove from the entrance of the specified player a student
     * @param uID nickname of the player
     * @param colour colour of the student who must be removed
     * @return the student removed
     * @throws EmptyException thrown if the entrance of the specified player is empty
     * @throws NoSuchStudentException thrown if there is no student with the passed colour in the player's entrance
     * @throws NoSuchPlayerException thrown if the nickname passed doesn't correspond to any player's nickname
     */
    public synchronized Student entranceEmptier(String uID, Colour colour) throws EmptyException,
                                                                             NoSuchStudentException,
                                                                             NoSuchPlayerException
    {
        if(uID==null) throw new NullPointerException();

        Player player=null;
        Student s;
        for (Player p : playersList)
            if (p.getuID().equals(uID))
                player = p;
        if(player==null)
            throw new NoSuchPlayerException();
        s = player.entranceEmptier(colour);

        List<Player> player_updated= new ArrayList<>();
        player_updated.add(player);
        ModelMessage message= new ModelMessage(characterCardList.size()!=0, null,
                                               null, player_updated, null, currentPlayer.getuID(),
                                               unusedCoins, false, phase);
        notify(message);
        return s;
    }

    /**
     * This method returns a list of colour which represent the colours of the students in the specified
     * player's entrance
     * @param uID nickname of the player
     * @return the colours of the player's student in the entrance
     * @throws NoSuchPlayerException thrown if the nickname passed doesn't correspond to any player's nickname
     */
    public synchronized List<Colour> getStudents(String uID) throws NoSuchPlayerException
    {
        if(uID==null) throw new NullPointerException();

        Player player = null;
        for (Player p : playersList)
            if (p.getuID().equals(uID))
                player = p;
        if(player==null)
            throw new NoSuchPlayerException();
        return player.getStudents();
    }

    /**
     * This method returns the island's position in which is present mother nature
     * @return the island index
     */
    public synchronized int getCurrPosMN(){
        Island position = motherNature.getCurrentPos();
        return islandsList.indexOf(position);
    }

    /**
     * This method move mother nature from the current island to a new one with the specified distance and calls the
     * method to calculate the dominance
     * @param deltaPos distance of the island in which we want to move mother nature
     * @throws FullTowersException thrown by the islandDominance method called after having moved mother nature
     * @throws RunOutOfTowersException thrown by the islandDominance method called after having moved mother nature
     * @throws EmptyException thrown by the islandDominance method called after having moved mother nature
     * @throws LinkFailedException thrown by the islandDominance method called after having moved mother nature
     */
    public synchronized void moveMN(int deltaPos) throws FullTowersException,
                                                         RunOutOfTowersException,
                                                         EmptyException, LinkFailedException
    {
        if(deltaPos<0) throw new IndexOutOfBoundsException();

        Island island = motherNature.getCurrentPos();
        int i = islandsList.indexOf(island);
        i = (i + deltaPos)%islandsList.size();
        motherNature.jumpNextPos(islandsList.get(i));
        islandDominance(islandsList.get(i));

        ModelMessage message= new ModelMessage(characterCardList.size()!=0, new ArrayList<>(islandsList),
                                               null, new ArrayList<>(playersList), null,currentPlayer.getuID(),
                                               unusedCoins, false, phase);
        notify(message);
    }

    /**
     * This method calculate the influence of each player over a specified island and puts the most influential
     * player's towers, if there is one, over this island
     * @param island island of which the method calculate the influence
     * @throws FullTowersException thrown by the method towersSwitcher of island
     * @throws RunOutOfTowersException thrown by the method towersSwitcher of island
     * @throws EmptyException thrown if the island was modified during the execution of this method and was
     * emptied from every tower
     * @throws LinkFailedException thrown by the method towersSwitcher of island
     */
    protected synchronized void islandDominance(Island island) throws FullTowersException,
                                                                      RunOutOfTowersException,
                                                                      EmptyException, LinkFailedException
    {
        if(island==null) throw new NullPointerException();
        List<Colour> islandColoursList=island.getStudentsColours();
        Map<Colour, Integer> coloursMap=new HashMap<>();
        Player dominantPlayer=null;
        boolean drawFlag=true;
        int pPoints=0, maxPPoints=0;

        if(island==motherNature.getCurrentPos() && !island.getInhibition())
        {
            for(Colour c: Colour.values())
                coloursMap.put(c, Integer.valueOf(0));

            for(Colour c: islandColoursList)
            {
                //here "coloursMap.get(c)" is equivalent to
                //"Integer.valueOf(coloursMap.get(c).intValue())"
                coloursMap.replace(c, coloursMap.get(c)+1);
            }

            for(Player p : playersList)
            {
                for(Teacher t : teachersList)
                    if(p.checkTeacherPresence(t.getColour()))
                        pPoints += coloursMap.get(t.getColour());

                if (island.getNumTowers() != 0)
                    if(island.getTowersColour()==p.getTowers().getColour())
                        pPoints += island.getNumTowers();

                if(pPoints==maxPPoints)
                    drawFlag=true;

                if(pPoints>maxPPoints)
                {
                    maxPPoints=pPoints;
                    dominantPlayer=p;
                    drawFlag=false;
                }

                pPoints=0;
            }

            if(!drawFlag)
                island.towersSwitcher((dominantPlayer.getTowers()));
        }
        else
        {
            if(island.getInhibition())
            {
                island.subInhibition();
                this.giveBackInhibitionFlag();
            }
            else
                throw new IllegalArgumentException();  // the passed Island doesn't have the
                                                       // mother nature on it
        }
    }

    /**
     * This method checks if there are some island with the same towers on them, next to each other.
     * If there are some, those islands are linked and the set of island is changed
     * @throws LinkFailedException thrown by the islandLinker method of the class Island
     */
    public synchronized void checkIslandLinking() throws LinkFailedException
    {
        Island island1;
        Island island2;
        for(int i=0; i<islandsList.size()-1; i++)
        {
            island1=islandsList.get(i);
            island2=islandsList.get(i+1);
            if(island1.getNumTowers()!=0 && island2.getNumTowers()!=0)
            {
                try
                {
                    if (island1.getTowersColour() == island2.getTowersColour())
                    {
                        Island unifiedIsland= island1.islandsLinker(island2);
                        motherNature.jumpNextPos(unifiedIsland);
                        islandsList.remove(island2);
                        islandsList.set(i, unifiedIsland);
                        i--;
                    }
                } catch (EmptyException e) { throw new LinkFailedException(); }
            }
        }
        // check the last and first islands
        island1=islandsList.get(0);
        island2=islandsList.get(islandsList.size()-1);
        if(island1.getNumTowers()!=0 && island2.getNumTowers()!=0)
        {
            try
            {
                if (island1.getTowersColour() == island2.getTowersColour())
                {
                    Island unifiedIsland= island1.islandsLinker(island2);
                    motherNature.jumpNextPos(unifiedIsland);
                    islandsList.remove(island2);
                    islandsList.set(0, unifiedIsland);
                }
            } catch (EmptyException e) { throw new LinkFailedException(); }
        }
    }

    /**
     * This method returns the number of position mother nature is allowed to make thanks to the card discarded
     * by the specified player
     * @param uID nickname of the player
     * @return the last discarded card's mother nature value
     * @throws NoSuchPlayerException thrown if the nickname passed doesn't correspond to any player's nickname
     * @throws EmptyException thrown if the player didn't discard any card
     */
    public synchronized int getLastCardValue(String uID) throws NoSuchPlayerException, EmptyException
    {
        if(uID==null) throw new NullPointerException();

        Player player = null;
        for (Player p : playersList)
            if (p.getuID().equals(uID))
                player = p;
        if(player==null)
            throw new NoSuchPlayerException();
        return player.getLastCardMNValue();
    }

    /**
     * This method (used only when the game is in expert mode and there is the character card 5) adds
     * an inhibition tile to the specified island and requires the character card 5 as a parameter in order to
     * give back the inhibition tile once they are consumed
     * @param islandPos position of the island
     * @param card5 character card 5
     */
    public synchronized void addInhibition(int islandPos, CharacterCard5 card5)
    {
        if(islandPos<0 || islandPos>=islandsList.size()) throw new IndexOutOfBoundsException();
        if(card5==null) throw new NullPointerException();
        this.card5=card5;
        islandsList.get(islandPos).addInhibition();
    }

    /**
     * This method gives back an inhibition tile to the character card 5
     * @throws IllegalAccessError thrown if there is no character card 5 in the game or if we never used an
     * inhibition tile
     */
    protected synchronized void giveBackInhibitionFlag()
    {
        if(card5==null)
            throw new IllegalAccessError();
        card5.giveBackInhibitionFlag();
    }

    /**
     * This method swaps 2 students between the enatrance and the classroom of a specified player.
     * It's used in expert mode by the character card 10
     * @param uID nickname of the player
     * @param entranceStudentColour colour of the student in the entrance that must be moved to the classroom
     * @param classroomStudentColour colour of the student in the classroom that must be moved to the entrance
     * @throws NoSuchStudentException thrown if there is no student with that colour either in the entrance or
     * in the classroom
     * @throws EmptyException thrown if the entrance is empty when this method was called
     * @throws FullEntranceException thrown if the entrance was filled during this method execution before the
     * student swapped was added to the entrance
     * @throws FullClassException thrown if the classroom, corresponding to the colour of the entranceStudent
     * who must be swapped, is already full
     * @throws NoSuchPlayerException thrown if the nickname passed doesn't correspond to any player's nickname
     * @throws NoSuchTeacherException thrown by the method teacherDominance called after the swapping
     * @throws TeacherAlreadyInException thrown by the method teacherDominance called after the swapping
     * @throws TooManyTeachersException thrown by the method teacherDominance called after the swapping
     */
    public synchronized void studentsSwap(String uID, Colour entranceStudentColour, Colour classroomStudentColour)
                                            throws NoSuchStudentException, EmptyException, FullEntranceException,
                                            FullClassException, NoSuchPlayerException, NoSuchTeacherException,
                                            TeacherAlreadyInException, TooManyTeachersException
    {
        if(entranceStudentColour==null || classroomStudentColour==null || uID==null)
            throw new NullPointerException();

        Player player = null;
        for (Player p : playersList)
            if (p.getuID().equals(uID))
                player = p;
        if(player==null)
            throw new NoSuchPlayerException();
        player.studentsSwap(entranceStudentColour, classroomStudentColour);
        teacherDominance(player, entranceStudentColour);
        // having removed a student from the classroom check if other players must take the
        // dominance on that teacher
        if(player.checkTeacherPresence(classroomStudentColour))
            for(Player p: playersList)
                teacherDominance(p, classroomStudentColour);
    }

    /**
     * This method checks if the player, who asked to activate the specified card, has enough coins to do it
     * @param uID nickname of the player
     * @param cardID id of the character card that is going to be activated
     * @return true if and only if the player has enough money to activate the card
     * @throws NoSuchCardException thrown if there is no character card in this game with the passed id
     * @throws NoSuchPlayerException thrown if the nickname passed doesn't correspond to any player's nickname
     */
    public synchronized boolean checkEnoughMoney(String uID, int cardID) throws NoSuchCardException,
                                                                                NoSuchPlayerException
    {
        if(uID==null) throw new NullPointerException();

        Player player = null;
        for (Player p : playersList)
            if (p.getuID().equals(uID))
                player = p;
        if(player==null)
            throw new NoSuchPlayerException();
        int chardPos=-1;
        for(CharacterCard c: characterCardList)
            if(c.getCardID()==cardID)
                chardPos=characterCardList.indexOf(c);
        if(chardPos==-1)
            throw new NoSuchCardException();

        int price=characterCardList.get(chardPos).getPrice();
        return price <= player.getCoins();
    }

    /**
     * This method adds to the entrance of the specified player the list of students passed as a parameter
     * @param uID nickname of the player
     * @param studentsList list of students that will be added to the entrance
     * @throws NoSuchPlayerException thrown if the nickname passed doesn't correspond to any player's nickname
     * @throws FullEntranceException thrown if the entrance doesn't have enough space to add all the passed
     * students
     */
    public synchronized void entranceFiller(String uID, List<Student> studentsList) throws NoSuchPlayerException,
                                                                                           FullEntranceException
    {
        if(uID==null) throw new NullPointerException();
        Player player = null;
        for (Player p : playersList)
            if (p.getuID().equals(uID))
                player = p;
        if(player==null)
            throw new NoSuchPlayerException();

        player.entranceFiller(studentsList);
    }

    /**
     * @return the number of islands existing in the game at this moment
     */
    public synchronized int getNumIslands() { return islandsList.size(); }

    /**
     * This method activate the effect of the specified character card
     * @param uID nickname of the player
     * @param dataBuffer structure that contains the data necessary for the character card activation
     * @param controller the controller is a parameter required by the character card for its activation
     * @throws Exception thrown if there was any king of exception thrown during the cativation of the character
     * card
     */
    public synchronized void activateCard(String uID, DataBuffer dataBuffer, Controller controller) throws Exception
    {
        if(uID==null || dataBuffer==null || controller==null)
            throw new NullPointerException();
        if(!dataBuffer.getUID().equals(uID))
            throw new IllegalArgumentException();

        int cardID=dataBuffer.getCharacterCardID();
        int index=-1;
        for(int i=0; i<characterCardList.size(); i++)
            if(characterCardList.get(i).getCardID()==cardID)
                index=i;
        if(index==-1)
            throw new NoSuchCardException();
        characterCardList.get(index).handle(uID, dataBuffer, controller);

        List<CharacterCard> cc_updated= new ArrayList<>();
        cc_updated.add(characterCardList.get(index));
        ModelMessage message= new ModelMessage(true, new ArrayList<>(islandsList),
              new ArrayList<>(cloudsList), new ArrayList<>(playersList), cc_updated, currentPlayer.getuID(),
              unusedCoins, false, phase);
        notify(message);
    }

    /**
     * @param uID nickname of the player
     * @return a list containing the round values of all the cards in the hand of the specified player
     * @throws NoSuchPlayerException thrown if the nickname passed doesn't correspond to any player's nickname
     */
    public synchronized List<Integer> getCardsRoundValues(String uID) throws NoSuchPlayerException
    {
        if(uID==null) throw new NullPointerException();
        Player player = null;
        for (Player p : playersList)
            if (p.getuID().equals(uID))
                player = p;
        if(player==null)
            throw new NoSuchPlayerException();

        return player.getHandCards().stream().map(StandardCard::getRoundValue).collect(Collectors.toList());
    }

    /**
     * This method removes a coin (if there is at least one) from the unused coins in the model
     * @throws NoMoreCoinsException thrown if there are no more coins available
     */
    public synchronized void getCoin() throws NoMoreCoinsException
    {
        if(unusedCoins<=0)
            throw new NoMoreCoinsException();
        else
            unusedCoins--;
    }

    /**
     * This method returns the number of students of the specified colour contained in the classroom of the
     * passed player
     * @param uID nickname of the player
     * @param colour the colour of the students
     * @return the number of students
     * @throws NoSuchPlayerException thrown if the nickname passed doesn't correspond to any player's nickname
     */
    public synchronized int getStudentsNum(String uID, Colour colour) throws NoSuchPlayerException {
        if(uID==null) throw new NullPointerException();
        Player player = null;
        for (Player p : playersList)
            if (p.getuID().equals(uID))
                player = p;
        if(player==null)
            throw new NoSuchPlayerException();
        return player.getStudentNum(colour);
    }

    /**
     * This method sets the current player to the one passed as an argument
     * @param uID nickname of the player
     * @throws NoSuchPlayerException thrown if the nickname passed doesn't correspond to any player's nickname
     */
    public void setCurrentPlayer(String uID) throws NoSuchPlayerException
    {
        if(uID==null) throw new NullPointerException();
        Player player = null;
        for (Player p : playersList)
            if (p.getuID().equals(uID))
                player = p;
        if(player==null)
            throw new NoSuchPlayerException();
        currentPlayer= player;
        ModelMessage message= new ModelMessage(characterCardList.size()!=0, null,
                                               null, null, null, currentPlayer.getuID(),
                                               unusedCoins, false, phase);
        notify(message);
    }

    /**
     * This method sets the phase to the one passed as an argument
     * @param newPhase the new phase
     */
    public void setPhase(Phase newPhase)
    {
        this.phase= newPhase;
        ModelMessage message= new ModelMessage(characterCardList.size()!=0, null,
                                                null, null, null, currentPlayer.getuID(),
                                                unusedCoins, false, newPhase);
        notify(message);
    }

    /**
     * This method notifies all the players of the game ending and send them the last model view
     */
    public void endGame()
    {
        ModelMessage message= new ModelMessage(characterCardList.size()!=0, new ArrayList<>(islandsList),
                                               new ArrayList<>(cloudsList), new ArrayList<>(playersList),
                                               new ArrayList<>(characterCardList), currentPlayer.getuID(),
                                               unusedCoins, true, phase);
        notify(message);
    }

    /**
     * This method notifies all the player of an error
     * @param errorMessage the error message that the players will see
     * @param isFatal a flag which represents the gravity of the error, when true all the connections with
     *                the players are closed
     */
    public void errorMessage(String errorMessage, boolean isFatal)
    {
        ModelMessage message= new ModelMessage(new ExceptionMessage(errorMessage, isFatal));
        notify(message);
    }

    /**
     * This method adds a listener to the list of listeners to the model modifications
     * @param listener the listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        if(listener==null)
            throw new NullPointerException();
        support.addPropertyChangeListener(listener);

        // send to client the first model
        ModelMessage message= new ModelMessage(characterCardList.size()!=0, new ArrayList<>(islandsList),
                                               new ArrayList<>(cloudsList), new ArrayList<>(playersList),
                                               new ArrayList<>(characterCardList), currentPlayer.getuID(),
                                               unusedCoins, false, phase);
        listener.propertyChange(new PropertyChangeEvent(this, "ModelModifications",
                                                        null, message));
    }

    /**
     * This method notify all the listeners (the players) sending the model view passed as a parameter.
     * Its used only when the model is modified, notifying the players of the current model view
     * @param message the current model view
     */
    protected void notify(ModelMessage message)
    {
        if(message==null)
            throw new NullPointerException();
        support.firePropertyChange("ModelModifications", null, message);
    }
}
