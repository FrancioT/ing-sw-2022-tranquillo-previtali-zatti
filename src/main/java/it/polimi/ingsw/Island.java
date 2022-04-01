package it.polimi.ingsw;

import it.polimi.ingsw.Exceptions.EmptyException;
import it.polimi.ingsw.Exceptions.FullTowersException;
import it.polimi.ingsw.Exceptions.LinkFailedException;

import java.util.ArrayList;
import java.util.List;

public class Island extends Tile
{
    private Towers towers;
    private int numTowers;
    private boolean motherNatureFlag;
    private boolean inhibitionFlag;

    public Island(Student student)
    {
        super();
        addStudent(student);
        inhibitionFlag=false;
        motherNatureFlag=false;
        numTowers=0;
    }
    public Island(List<Student> students, Towers towers, int numT)
    {
        super();
        this.towers=towers;
        numTowers=numT;
        motherNatureFlag=true;
        inhibitionFlag=false;
        for(Student s: students)
        {
            addStudent(s);
        }
    }

    private List<Student> getStudents() { return new ArrayList<>(studentsList); }

    public Island islandsLinker(Island island) throws LinkFailedException
    {
        try
        {
            if (getTowersColour() != island.getTowersColour())
            {
                throw new LinkFailedException();
            }
        }catch (EmptyException e)
        {
            throw new LinkFailedException();
        }
        List<Student> tmp1=getStudents();
        tmp1.addAll(island.getStudents());
        return new Island(tmp1, towers, numTowers + island.getNumTowers());
    }

    public int getNumTowers() { return numTowers; }

    public ColourT getTowersColour() throws EmptyException
    {
        if(numTowers!=0)
            return towers.getColour();
        else
            throw new EmptyException();
    }

    public void towersSwitcher(Towers newTowers) throws FullTowersException {
        if(numTowers==0)
        {
            towers = newTowers;
            numTowers=1;
        }
        towers.availabilityModifier(numTowers);
        newTowers.availabilityModifier(-numTowers);
        towers=newTowers;
    }

    public void setMotherNatureFlag()
    {
        if(motherNatureFlag==false)
        {
            motherNatureFlag=true;
        }
        else
        {
            motherNatureFlag=false;
        }
    }

    public void setInhibitionFlag(boolean setPresence) { inhibitionFlag=setPresence; }
    public boolean getInhibitionFlag() { return inhibitionFlag; }
}
