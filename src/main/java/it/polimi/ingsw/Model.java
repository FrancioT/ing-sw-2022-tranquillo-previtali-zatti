package it.polimi.ingsw;
import it.polimi.ingsw.Exceptions.*;
import java.util.List;
import java.util.Objects;

public class Model {
    private List<Teacher> teachersList;
    private List<Island> islandsList;
    private List<Cloud> cloudsList;
    private List<Player> playersList;
    private MotherNature motherNature;
    private List<CharacterCard> characterCardList;
    private int unusedCoins;

    public void cloudsFiller(int n){
        for(Cloud cloud : cloudsList){
            cloud.cloudFiller(n);
        }
    }

    public void teacherDominance(String uID, Colour c){
        Player player1 = null;
        Player player2;
        int num1, num2;
        Teacher teacher = null;

        for(Player p : playersList)
            if(p.getuID().equals(uID))
                player1 = p;

        if (!player1.checkTeacherPresence(c)){
            for(Teacher t : teachersList)
                if(t.getColour() == c)
                    teacher = t;
            player2 = teacher.getCurrentPos();
            num1 = player1.getStudentNum(c);
            num2 = player2.getStudentNum(c);
            if(num1 > num2){
                player1.addTeacher(player2.removeTeacher(c));
                teacher.setNewPos(player1);
            }
        }
    }

    public void cloudEmptier(String uID, int i_cloud) throws FullEntranceException {
        Player tmp = null;
        for (Player p : playersList){
            if (p.getuID().equals(uID))
                tmp = p;
        }
        assert tmp != null;
        cloudsList.get(i_cloud).cloudEmptier(tmp);
    }

    public StandardCard cardDiscarder(String uID, int pos){
        StandardCard s = null;
        for (Player p : playersList){
            if (p.getuID().equals(uID))
                s = p.cardDiscarder(pos);
        }
        return s;
    }

    public void entranceFiller(String uID, List<Student> students) throws FullEntranceException {
        for (Player p : playersList){
            if (p.getuID().equals(uID))
                p.entranceFiller(students);
        }
    }

    public Student entranceEmptier(String uID, Colour c) throws EmptyException, UnexistingException {
        Student s = null;
        for (Player p : playersList){
            if (p.getuID().equals(uID))
                s = p.entranceEmptier(c);
        }
        return s;
    }

    public List<Colour> getStudents(String uID){
        Player tmp = null;
        for (Player p : playersList){
            if (p.getuID().equals(uID))
                tmp = p;
        }
        return tmp.getStudents();
    }

    public int getCurrPosMN(){
        Island position = motherNature.getCurrentPos();
        return islandsList.indexOf(position);
    }

    public void moveMN(int deltaPos){
        Island island = motherNature.getCurrentPos();
        int i = islandsList.indexOf(island);
        i = i + deltaPos;
        motherNature.jumpNextPos(islandsList.get(i));
    }

    private void islandDominance(Island island){}

    public void handleCard(int index){}
}
