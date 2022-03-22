package it.polimi.ingsw;

public class Island extends Tile{
    private Towers towers;
    private int numTowers;
    private boolean motherNatureFlag;
    private boolean inhibitionFlag;

    public void islandsLinker(){}
    public Colour dominance(){}
    public void towersSwitcher(Towers t){}

    public void setMotherNatureFlag()
    {
        if(motherNatureFlag==false)
        {
            motherNatureFlag=true;
        }
        else
        {
            motherNatureFlag=true;
        }
    }

    @Override
    public void removeStudents(){
        //add exception because in island we can't remove students
        throw
    }
}
