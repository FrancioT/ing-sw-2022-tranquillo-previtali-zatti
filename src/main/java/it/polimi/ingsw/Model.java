package it.polimi.ingsw;
import it.polimi.ingsw.Exceptions.*;
import java.util.List;

public class Model {
    private List<Teacher> teachersList;
    private List<Island> islandsList;
    private List<Cloud> cloudsList;
    private List<Player> playersList;
    private MotherNature motherNature;
    private List<CharacterCard> characterCardList;
    private int unusedCoins;


    public void teacherDominance(String uID, Colour c) throws TooManyTeachersException,
                                                              TeacherAlreadyInException,
                                                              NoSuchTeacherException,
                                                              NoSuchPlayerException
    {
        Player player1 = null;
        Player player2;
        int num1, num2;
        Teacher teacher = null;

        for(Player p : playersList)
            if(p.getuID().equals(uID))
                player1 = p;
        if(player1==null)
            throw new NoSuchPlayerException();

        if (!player1.checkTeacherPresence(c)){
            for(Teacher t : teachersList)
                if(t.getColour() == c)
                    teacher = t;
            player2 = teacher.getCurrentPos();
            if(player2==null) {
                player1.addTeacher(teacher);
                teacher.setNewPos(player1);
            }
            else {
                num1 = player1.getStudentNum(c);
                num2 = player2.getStudentNum(c);
                if (num1 > num2) {
                    player1.addTeacher(player2.removeTeacher(c));
                    teacher.setNewPos(player1);
                }
            }
        }
    }

    public void addStudentDashboard(String uID, Student student) throws NoSuchPlayerException
    {
        Player tmp = null;
        for (Player p : playersList)
            if (p.getuID().equals(uID))
                tmp = p;
        if(tmp==null)
            throw new NoSuchPlayerException();
        tmp.addStudent(student);
    }

    public void addStudentIsland(int index, Student student) throws IndexOutOfBoundsException
    {
        if(index>islandsList.size())
            throw new IndexOutOfBoundsException();
        islandsList.get(index).addStudent(student);
    }

    public void cloudsFiller(int n){
        for(Cloud cloud : cloudsList){
            cloud.cloudFiller(n);
        }
    }

    public void cloudEmptier(String uID, int i_cloud) throws FullEntranceException,
                                                             NoSuchPlayerException
    {
        Player tmp = null;
        for (Player p : playersList)
            if (p.getuID().equals(uID))
                tmp = p;
        if(tmp==null)
            throw new NoSuchPlayerException();
        cloudsList.get(i_cloud).cloudEmptier(tmp);
    }

    public StandardCard cardDiscarder(String uID, int pos) throws NoSuchPlayerException
    {
        Player tmp=null;
        StandardCard s = null;
        for (Player p: playersList)
            if (p.getuID().equals(uID))
                tmp = p;
        if(tmp==null)
            throw new NoSuchPlayerException();
        s = tmp.cardDiscarder(pos);
        return s;
    }

    public Student entranceEmptier(String uID, Colour c) throws EmptyException, NoSuchStudentException,
                                                                NoSuchPlayerException
    {
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

    public List<Colour> getStudents(String uID) throws NoSuchPlayerException
    {
        Player tmp = null;
        for (Player p : playersList)
            if (p.getuID().equals(uID))
                tmp = p;
        if(tmp==null)
            throw new NoSuchPlayerException();
        return tmp.getStudents();
    }

    public int getCurrPosMN(){
        Island position = motherNature.getCurrentPos();
        return islandsList.indexOf(position);
    }

    public void moveMN(int deltaPos) throws EmptyException, FullTowersException {
        Island island = motherNature.getCurrentPos();
        int i = islandsList.indexOf(island);
        i = (i + deltaPos)%islandsList.size();
        motherNature.jumpNextPos(islandsList.get(i));
        islandDominance(islandsList.get(i));
    }

    private void islandDominance(Island island) throws FullTowersException, EmptyException {
        List<Colour> coloursListTmp=island.getStudentsColours();
        Player dominantPlayer=null;
        boolean drawFlag=false;
        int coloursPoints[]=new int[5];
        int playersPoints[]=new int[4];
        int pIndex = 0, pMax = 0;

        if(island.equals(motherNature.getCurrentPos())&&(island.getInhibitionFlag()==false))
        {
            for(int i=0; i<5; i++)
            {
                coloursPoints[i]=0;
                if(i<5)
                {
                    playersPoints[i]=0;
                }
            }

            for(Colour c : coloursListTmp)
            {
                if(c.equals(Colour.red))
                    coloursPoints[0]++;
                if(c.equals(Colour.blue))
                    coloursPoints[1]++;
                if(c.equals(Colour.pink))
                    coloursPoints[2]++;
                if(c.equals(Colour.yellow))
                    coloursPoints[3]++;
                if(c.equals(Colour.green))
                    coloursPoints[4]++;
            }

            for(Player p : playersList)
            {
                for(Teacher t : teachersList)
                {
                    if(p.equals(t.getCurrentPos()))
                    {
                        if((t.getColour()).equals(Colour.red))
                            playersPoints[pIndex]=playersPoints[pIndex]+coloursPoints[0];
                        if((t.getColour()).equals(Colour.blue))
                            playersPoints[pIndex]=playersPoints[pIndex]+coloursPoints[1];
                        if((t.getColour()).equals(Colour.pink))
                            playersPoints[pIndex]=playersPoints[pIndex]+coloursPoints[2];
                        if((t.getColour()).equals(Colour.yellow))
                            playersPoints[pIndex]=playersPoints[pIndex]+coloursPoints[3];
                        if((t.getColour()).equals(Colour.green))
                            playersPoints[pIndex]=playersPoints[pIndex]+coloursPoints[4];
                    }
                }
                pIndex++;
            }

            pIndex = 0;

            for(Player p: playersList)
            {
                if(playersPoints[pIndex]>pMax)
                {
                    pMax=playersPoints[pIndex];
                    dominantPlayer=p;
                }
                pIndex++;
            }

            pIndex = 0;

            for(Player p: playersList)
            {
                if((playersPoints[pIndex]==pMax)&&!(p.equals(dominantPlayer)))
                {
                    drawFlag=true;
                }
                pIndex++;
            }

            if(drawFlag==false)
            {
                if(island.getNumTowers()==0)
                {
                    island.towersSwitcher((dominantPlayer.getTowers()));
                }
                if(!(island.getTowersColour()).equals(((dominantPlayer.getTowers()).getColour())))
                {
                    pIndex=island.getNumTowers();

                    for(int i=0; i<pIndex; i++)
                    {
                        island.towersSwitcher((dominantPlayer.getTowers()));
                    }
                }
            }
        }
    }

    public void handleCard(int index){}
}
