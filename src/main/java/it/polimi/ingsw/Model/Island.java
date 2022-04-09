package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Exceptions.EmptyException;
import it.polimi.ingsw.Model.Exceptions.FullTowersException;
import it.polimi.ingsw.Model.Exceptions.LinkFailedException;
import it.polimi.ingsw.Model.Exceptions.RunOutOfTowersException;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;

import java.util.ArrayList;
import java.util.List;

public class Island extends Tile
{
    private Towers towers;
    private int numTowers;
    private boolean motherNatureFlag;
    private boolean inhibitionFlag;
    private final Model model;

    public Island(boolean MN_presence, Model model)
    {
        super();
        inhibitionFlag=false;
        motherNatureFlag=MN_presence;
        numTowers=0;
        this.model=model;
    }
    public Island(Student student, Model model)
    {
        super();
        addStudent(student);
        inhibitionFlag=false;
        motherNatureFlag=false;
        numTowers=0;
        this.model=model;
    }
    public Island(List<Student> students, Towers towers, int numT,
                  boolean inhibitionFlag, Model model)
    {
        super();
        this.towers=towers;
        numTowers=numT;
        motherNatureFlag=true;
        this.inhibitionFlag=inhibitionFlag;
        this.model=model;
        for(Student s: students)
            addStudent(s);
    }

    public Island islandsLinker(Island island) throws LinkFailedException
    {
        if(island==null) throw new NullPointerException();

        try
        {
            if (getTowersColour() != island.getTowersColour())
                throw new LinkFailedException();
        }catch (EmptyException e)
        {
            throw new LinkFailedException();
        }
        if(this.inhibitionFlag && island.inhibitionFlag)
            model.giveBackInhibitionFlag();
        List<Student> tmp1=new ArrayList<>(studentsList);
        tmp1.addAll(island.studentsList);
        return new Island(tmp1, towers, numTowers + island.numTowers,
              island.inhibitionFlag || inhibitionFlag, this.model);
    }

    public int getNumTowers() { return numTowers; }

    public ColourT getTowersColour() throws EmptyException
    {
        if(numTowers!=0)
            return towers.getColour();
        else
            throw new EmptyException();
    }

    public void towersSwitcher(Towers newTowers) throws FullTowersException, RunOutOfTowersException,
                                                        LinkFailedException
    {
        if(newTowers==null) throw new NullPointerException();

        if(numTowers==0)
        {
            towers = newTowers;
            numTowers=1;
            newTowers.availabilityModifier(-1);
        }
        towers.availabilityModifier(numTowers);
        newTowers.availabilityModifier(-numTowers);
        towers=newTowers;
        model.checkIslandLinking();
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

    public boolean isMotherNatureFlag() { return motherNatureFlag; }

    public void setInhibitionFlag(boolean setPresence) { inhibitionFlag=setPresence; }
    public boolean getInhibitionFlag() { return inhibitionFlag; }
}
