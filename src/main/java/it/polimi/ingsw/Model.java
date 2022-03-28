package it.polimi.ingsw;
import it.polimi.ingsw.Exceptions.*;

import java.util.List;
import java.util.Objects;

public class Model {
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

    public void teacherDominance(String uID, Colour c){}

    public void cloudEmptier(String uID, int i_cloud) throws FullEntranceException {
        Player tmp = null;
        for (Player p : playersList){
            if (p.getuID().equals(uID))
                tmp = p;
        }
        assert tmp != null;
        cloudsList.get(i_cloud).cloudEmptier(tmp.getPlayerDashboard());
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

    public void handleCard(int index){}
}
