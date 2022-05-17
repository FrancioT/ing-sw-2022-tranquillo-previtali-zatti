package it.polimi.ingsw.Model.ModelAndDecorators;
import it.polimi.ingsw.ClientsHandler.Messages.ModelMessage;
import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.CharacterCard.*;
import it.polimi.ingsw.Model.Exceptions.*;

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
    protected final PropertyChangeSupport support;

    public Model(List<String> uIDs, boolean expertMode)
    {
        int characterCardNum=12;
        support= new PropertyChangeSupport(this);
        teachersList=new ArrayList<>();
        islandsList=new ArrayList<>();
        cloudsList=new ArrayList<>();
        playersList=new ArrayList<>();
        currentPlayer=null;
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
        // creation of islands and mother nature
        Bag bag=new Bag();
        islandsList.add(new Island(true, this));
        for(int i=1; i<12; i++)
        {
            if (i != 6)
                islandsList.add(new Island(bag.randomExtraction(), this));
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
                entranceStudents.add(bag.randomExtraction());
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
            cardListTmp.add(new CharacterCard1(bag));
            cardListTmp.add(new CharacterCard2());
            cardListTmp.add(new CharacterCard3());
            cardListTmp.add(new CharacterCard4());
            cardListTmp.add(new CharacterCard5());
            cardListTmp.add(new CharacterCard6());
            cardListTmp.add(new CharacterCard7(bag));
            cardListTmp.add(new CharacterCard8());
            cardListTmp.add(new CharacterCard9());
            cardListTmp.add(new CharacterCard10());
            cardListTmp.add(new CharacterCard11(bag));
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

        // send to clients the first model
        ModelMessage message= new ModelMessage(expertMode, new ArrayList<>(islandsList), new ArrayList<>(cloudsList),
                new ArrayList<>(playersList), new ArrayList<>(characterCardList), currentPlayer.getuID(),
                unusedCoins, false);
        notify(message);
    }

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
                     unusedCoins, false);
        notify(message);
    }

    public synchronized void addStudentIsland(int index, Student student) throws IndexOutOfBoundsException
    {
        if(index>islandsList.size() || index<0)
            throw new IndexOutOfBoundsException();
        islandsList.get(index).addStudent(student);

        ModelMessage message= new ModelMessage(characterCardList.size()!=0, new ArrayList<>(islandsList),
                  null, null, null, currentPlayer.getuID(),
                    unusedCoins, false);
        notify(message);
    }

    public synchronized void cloudsFiller(int n){
        for(Cloud cloud : cloudsList){
            cloud.cloudFiller(n);
        }
        ModelMessage message= new ModelMessage(characterCardList.size()!=0, null,
                new ArrayList<>(cloudsList), null, null, currentPlayer.getuID(),
                unusedCoins, false);
        notify(message);
    }

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
                new ArrayList<>(cloudsList), player_updated, null, currentPlayer.getuID(),
                unusedCoins, false);
        notify(message);
    }

    public synchronized StandardCard cardDiscarder(String uID, int pos) throws NoSuchPlayerException
    {
        if(uID==null) throw new NullPointerException();

        Player tmp=null;
        StandardCard s;
        for (Player p: playersList)
            if (p.getuID().equals(uID))
                tmp = p;
        if(tmp==null)
            throw new NoSuchPlayerException();
        s = tmp.cardDiscarder(pos);

        List<Player> player_updated= new ArrayList<>();
        player_updated.add(tmp);
        ModelMessage message= new ModelMessage(characterCardList.size()!=0,null,
                null, player_updated, null, currentPlayer.getuID(),
                unusedCoins, false);
        notify(message);
        return s;
    }

    public synchronized Student entranceEmptier(String uID, Colour c) throws EmptyException,
                                                                             NoSuchStudentException,
                                                                             NoSuchPlayerException
    {
        if(uID==null) throw new NullPointerException();

        Player tmp=null;
        Student s;
        for (Player p : playersList)
            if (p.getuID().equals(uID))
                tmp = p;
        if(tmp==null)
            throw new NoSuchPlayerException();
        s = tmp.entranceEmptier(c);

        List<Player> player_updated= new ArrayList<>();
        player_updated.add(tmp);
        ModelMessage message= new ModelMessage(characterCardList.size()!=0, null,
                null, player_updated, null, currentPlayer.getuID(),
                unusedCoins, false);
        notify(message);
        return s;
    }

    public synchronized List<Colour> getStudents(String uID) throws NoSuchPlayerException
    {
        if(uID==null) throw new NullPointerException();

        Player tmp = null;
        for (Player p : playersList)
            if (p.getuID().equals(uID))
                tmp = p;
        if(tmp==null)
            throw new NoSuchPlayerException();
        return tmp.getStudents();
    }

    public synchronized int getCurrPosMN(){
        Island position = motherNature.getCurrentPos();
        return islandsList.indexOf(position);
    }

    public synchronized void moveMN(int deltaPos) throws FullTowersException,
                                                         RunOutOfTowersException,
                                                         EmptyException, LinkFailedException
    {
        if(deltaPos<=0) throw new IndexOutOfBoundsException();

        Island island = motherNature.getCurrentPos();
        int i = islandsList.indexOf(island);
        i = (i + deltaPos)%islandsList.size();
        motherNature.jumpNextPos(islandsList.get(i));
        islandDominance(islandsList.get(i));

        ModelMessage message= new ModelMessage(characterCardList.size()!=0, new ArrayList<>(islandsList),
                null, new ArrayList<>(playersList), null,currentPlayer.getuID(),
                unusedCoins, false);
        notify(message);
    }

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

    public synchronized int getLastCardValue(String uID) throws NoSuchPlayerException, EmptyException
    {
        if(uID==null) throw new NullPointerException();

        Player tmp = null;
        for (Player p : playersList)
            if (p.getuID().equals(uID))
                tmp = p;
        if(tmp==null)
            throw new NoSuchPlayerException();
        return tmp.getLastCardMNValue();
    }

    public synchronized void addInhibition(int islandPos, CharacterCard5 card5)
    {
        if(islandPos<0 || islandPos>=islandsList.size()) throw new IndexOutOfBoundsException();
        if(card5==null) throw new NullPointerException();
        this.card5=card5;
        islandsList.get(islandPos).addInhibition();
    }

    protected synchronized void giveBackInhibitionFlag() { card5.giveBackInhibitionFlag(); }

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

    public synchronized int getNumIslands() { return islandsList.size(); }
    public synchronized void activateCard(String uID, DataBuffer dataBuffer, Controller controller)
                                                                                         throws Exception
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
              unusedCoins, false);
        notify(message);
    }
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
    public synchronized void getCoin() throws NoMoreCoinsException
    {
        if(unusedCoins<=0)
            throw new NoMoreCoinsException();
        else
            unusedCoins--;
    }
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
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        if(listener==null)
            throw new NullPointerException();
        support.addPropertyChangeListener(listener);
    }
    protected void notify(ModelMessage message)
    {
        if(message==null)
            throw new NullPointerException();
        support.firePropertyChange("ModelModifications", null, message);
    }
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
    }
}
