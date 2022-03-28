package it.polimi.ingsw;

import java.util.List;

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

    public void cloudEmptier(String uID, int i_cloud){}

    public StandardCard cardDiscarder(String uID, int pos){}

    public void entranceFiller(String uID, List<Student> students){}

    public Student entranceEmptier(String uID, Colour c){}

    public List<Colour> getStudents(String uID){}

    public int getCurrPosMN(){
        int x = 0;
        Island position = motherNature.getCurrentPos();
        for(int i = 0; i < islandsList.size(); i++){
            if(position == islandsList.get(i))
                x = i;
        }
        return x;
    }

    public void moveMN(int deltaPos){}

    public void handleCard(int index){}
}
