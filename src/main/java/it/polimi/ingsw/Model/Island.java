package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Island extends Tile implements Serializable
{
    transient private Towers towers;
    private int numTowers;
    private boolean motherNatureFlag;
    private int inhibitionCounter;
    transient private final Model model;
    static final long serialVersionUID= 80100L;

    public Island(boolean MN_presence, Model model)
    {
        super();
        inhibitionCounter=0;
        motherNatureFlag=MN_presence;
        numTowers=0;
        this.model=model;
    }
    public Island(Student student, Model model)
    {
        super();
        addStudent(student);
        inhibitionCounter=0;
        motherNatureFlag=false;
        numTowers=0;
        this.model=model;
    }
    private Island(List<Student> students, Towers towers, int numT,
                  int inhibitionCounter, Model model, boolean mnFlag)
    {
        super();
        this.towers=towers;
        numTowers=numT;
        motherNatureFlag=mnFlag;
        this.inhibitionCounter=inhibitionCounter;
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
        List<Student> tmp1=new ArrayList<>(studentsList);
        tmp1.addAll(island.studentsList);
        return new Island(tmp1, towers, numTowers + island.numTowers,
                island.inhibitionCounter + this.inhibitionCounter, this.model,
                          this.motherNatureFlag || island.motherNatureFlag);
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

        try {
            if (numTowers == 0) {
                towers = newTowers;
                numTowers = 1;
                newTowers.availabilityModifier(-1);
            } else {
                towers.availabilityModifier(numTowers);
                towers = newTowers;
                newTowers.availabilityModifier(-numTowers);
            }
        }catch (RunOutOfTowersException e)
        {
            model.checkIslandLinking();
            throw e;
        }
        model.checkIslandLinking();
    }

    public void setMotherNature() { motherNatureFlag=true; }
    public void removeMotherNature() { motherNatureFlag=false; }
    public boolean isMotherNatureFlag() { return motherNatureFlag; }
    public void addInhibition() { inhibitionCounter++; }
    public void subInhibition()
    {
        if(inhibitionCounter<1)
            throw new IllegalAccessError("Tried to remove inhibition tile from island, " +
                                        "but there wasn't any");
        inhibitionCounter--;
    }
    public boolean getInhibition() { return inhibitionCounter>0; }
    public int getInhibitionCounter() { return inhibitionCounter; }

    private void writeObject(ObjectOutputStream oos) throws IOException
    {
        oos.defaultWriteObject();
        if(numTowers!=0)
            oos.writeObject(towers.getColour());
    }
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException
    {
        ois.defaultReadObject();
        if(numTowers!=0)
            this.towers= new Towers((ColourT) ois.readObject(), 0);
        else
            this.towers= null;
    }

    public void islandPrinter(boolean expertModeFlag) {

        System.out.print("students list: ");
        this.tilePrinter();

        if(expertModeFlag)
            System.out.println("Inhibition counter is: " + this.getInhibitionCounter());

        if (motherNatureFlag)
            System.out.println("MotherNature is on this Island");

        if(numTowers!=0)
        {
            System.out.print("With "+numTowers+" ");
            this.towers.towersPrinter();
        }

        System.out.println();
    }
}
