package it.polimi.ingsw.Model.ModelAndDecorators;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.CharacterCard.CharacterCard;
import it.polimi.ingsw.Model.CharacterCard.CharacterCard5;
import it.polimi.ingsw.Model.Exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class Model {
    protected List<Teacher> teachersList;
    protected List<Island> islandsList;
    protected List<Cloud> cloudsList;
    protected List<Player> playersList;
    protected MotherNature motherNature;
    protected List<CharacterCard> characterCardList;
    protected int unusedCoins;
    protected CharacterCard5 card5;

    public Model(List<String> uIDs, boolean expertMode)
    {
        teachersList=new ArrayList<>();
        islandsList=new ArrayList<>();
        cloudsList=new ArrayList<>();
        playersList=new ArrayList<>();
        // creation of players and towers
        Stack<Towers> towersList= new Stack<>();
        for(ColourT c: ColourT.values())
            towersList.push(new Towers(c, 1));
        for(String uID: uIDs)
            playersList.add(new Player(uID, towersList.pop()));
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
        // expert mode
        unusedCoins=0;
        if(expertMode)
        {
            characterCardList=new ArrayList<>();
            unusedCoins=20-playersList.size();
            . // finish implementation
        }
    }

    public Model(Model model)
    {
        teachersList=new ArrayList<>(model.teachersList);
        islandsList=new ArrayList<>(model.islandsList);
        cloudsList=new ArrayList<>(model.cloudsList);
        playersList=new ArrayList<>(model.playersList);
        characterCardList=new ArrayList<>(model.characterCardList);
        motherNature=model.motherNature;
        unusedCoins=model.unusedCoins;
        card5=model.card5;
    }

    public synchronized void payCard(String uID, int cardID) throws NoSuchPlayerException, NoSuchCardException,
                                                       cardPaymentException
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
        player.pay(price);
        unusedCoins+=price;
    }

    public synchronized void teacherDominance(String uID, Colour colour) throws TooManyTeachersException,
                                                              TeacherAlreadyInException,
                                                              NoSuchTeacherException,
                                                              NoSuchPlayerException
    {
        if(colour==null || uID==null) throw new NullPointerException();

        Player player1 = null;
        Player player2;
        int num1, num2;
        Teacher teacher = null;

        for(Player p : playersList)
            if(p.getuID().equals(uID))
                player1 = p;
        if(player1==null)
            throw new NoSuchPlayerException();

        if (!player1.checkTeacherPresence(colour)){
            for(Teacher t : teachersList)
                if(t.getColour() == colour)
                    teacher = t;
            player2 = teacher.getCurrentPos();
            if(player2==null) {
                player1.addTeacher(teacher);
                teacher.setNewPos(player1);
            }
            else {
                num1 = player1.getStudentNum(colour);
                num2 = player2.getStudentNum(colour);
                if (num1 > num2) {
                    player1.addTeacher(player2.removeTeacher(colour));
                    teacher.setNewPos(player1);
                }
            }
        }
    }

    public synchronized void addStudentDashboard(String uID, Student student) throws NoSuchPlayerException,
                                                                        FullClassException
    {
        if(uID==null) throw new NullPointerException();

        Player tmp = null;
        for (Player p : playersList)
            if (p.getuID().equals(uID))
                tmp = p;
        if(tmp==null)
            throw new NoSuchPlayerException();
        tmp.addStudent(student);
    }

    public synchronized void addStudentIsland(int index, Student student) throws IndexOutOfBoundsException
    {
        if(index>islandsList.size() || index<0)
            throw new IndexOutOfBoundsException();
        islandsList.get(index).addStudent(student);
    }

    public synchronized void cloudsFiller(int n){
        for(Cloud cloud : cloudsList){
            cloud.cloudFiller(n);
        }
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
    }

    protected synchronized void islandDominance(Island island) throws FullTowersException,
                                                                      RunOutOfTowersException,
                                                                      EmptyException, LinkFailedException
    {
        List<Colour> islandColoursList=island.getStudentsColours();
        HashMap<Colour, Integer> coloursMap=new HashMap<>();
        Player dominantPlayer=null;
        boolean drawFlag=false;
        int pPoints=0, maxPPoints=0;

        if(island.equals(motherNature.getCurrentPos()) && !island.getInhibitionFlag())
        {
            for(Colour c: Colour.values())
            {
                coloursMap.put(c, Integer.valueOf(0));
            }

            for(Colour c: islandColoursList)
            {
                //here "coloursMap.get(c)" is equivalent to
                //"Integer.valueOf(coloursMap.get(c).intValue())"
                coloursMap.replace(c, coloursMap.get(c)+1);
            }

            for(Player p : playersList)
            {
                for(Teacher t : teachersList)
                {
                    if(p.checkTeacherPresence(t.getColour()))
                    {
                        pPoints += coloursMap.get(t.getColour());
                    }
                }

                if (island.getNumTowers() != 0){
                    if(island.getTowersColour()==p.getTowers().getColour()) {
                        pPoints += island.getNumTowers();
                    }
                }

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

            if(drawFlag==false)
                island.towersSwitcher((dominantPlayer.getTowers()));
        }
        else
        {
            if(island.getInhibitionFlag())
            {
                island.setInhibitionFlag(false);
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
        for(int i=0; i<islandsList.size(); i++)
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

    public synchronized void activateInhibitionFlag(Island island, CharacterCard5 card5)
    {
        this.card5=card5;
        island.setInhibitionFlag(true);
    }
    public synchronized boolean getInhibitionFlag(Island island){
        return island.getInhibitionFlag();
    }
    public synchronized void giveBackInhibitionFlag() { card5.giveBackInhibitionFlag(); }
    public synchronized void studentsSwap(String uID, Colour entranceStudentColour, Colour classroomStudentColour)
                                        throws NoSuchStudentException, EmptyException, FullEntranceException,
                                               FullClassException, NoSuchPlayerException
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
    }
}
