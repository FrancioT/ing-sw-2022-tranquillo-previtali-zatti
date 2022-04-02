package it.polimi.ingsw;
import it.polimi.ingsw.Exceptions.*;

import java.util.HashMap;
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

    public void moveMN(int deltaPos) throws FullTowersException {
        Island island = motherNature.getCurrentPos();
        int i = islandsList.indexOf(island);
        i = (i + deltaPos)%islandsList.size();
        motherNature.jumpNextPos(islandsList.get(i));
        islandDominance(islandsList.get(i));
    }

    private void islandDominance(Island island) throws FullTowersException {

        List<Colour> islandColoursList=island.getStudentsColours();
        HashMap<Colour, Integer> coloursMap=new HashMap<>();
        Player dominantPlayer=null;
        boolean drawFlag=false;
        int pPoints=0, maxPPoints=0;

        if(island.equals(motherNature.getCurrentPos())&&(island.getInhibitionFlag()==false))
        {
            for(Colour c: Colour.values())
            {
                coloursMap.put(c, Integer.valueOf(0));
            }

            for(Colour c: islandColoursList)
            {
                coloursMap.replace(c, (Integer.valueOf(coloursMap.get(c))).intValue()+1);
            }

            for(Player p : playersList)
            {
                for(Teacher t : teachersList)
                {
                    if(p.equals(t.getCurrentPos()))
                    {
                        pPoints=pPoints+coloursMap.get(t.getColour());
                    }
                }

                if(pPoints==maxPPoints)
                {
                    drawFlag=true;
                }

                if(pPoints>maxPPoints)
                {
                    maxPPoints=pPoints;
                    dominantPlayer=p;
                    drawFlag=false;
                }

                pPoints=0;
            }

            if(drawFlag==false)
            {
                island.towersSwitcher((dominantPlayer.getTowers()));
            }
        }
    }

    public void handleCard(int index){}
}
