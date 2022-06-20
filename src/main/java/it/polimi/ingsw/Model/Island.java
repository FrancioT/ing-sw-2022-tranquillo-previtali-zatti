package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Exceptions.*;

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
    static final long serialVersionUID= 80100L;

    /**
     * Constructor for the islands where there will be no students at the start of the game (the island with MN, and
     * the one opposite to it)
     * @param MN_presence true if MN has to be put on it, false if not
     */
    public Island(boolean MN_presence)
    {
        super();
        inhibitionCounter=0;
        motherNatureFlag=MN_presence;
        numTowers=0;
    }

    /**
     * Constructor for the island that will have students at the start of the game
     * @param student student that needs to be added
     */
    public Island(Student student)
    {
        super();
        addStudent(student);
        inhibitionCounter=0;
        motherNatureFlag=false;
        numTowers=0;
    }

    /**
     * Method used to create an island after the linking of two
     * @param students list of students to be added
     * @param towers towers that are on the island
     * @param numT number of towers to be added
     * @param inhibitionCounter number of inhibition tiles on the island
     * @param mnFlag true if MN is on the island, false if not
     */
    private Island(List<Student> students, Towers towers, int numT,
                  int inhibitionCounter, boolean mnFlag)
    {
        super();
        this.towers=towers;
        numTowers=numT;
        motherNatureFlag=mnFlag;
        this.inhibitionCounter=inhibitionCounter;
        for(Student s: students)
            addStudent(s);
    }

    /**
     * Method to merge two islands in a single one. This method takes the values of the two islands that need to be
     * merged and add them up, then it passes them to a new island
     * @param island the island that we want to link to the one which calls the method
     * @return the new island created by the old 2
     * @throws LinkFailedException Exception thrown if the linking fails for some reasons
     */
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
                island.inhibitionCounter + this.inhibitionCounter,
                        this.motherNatureFlag || island.motherNatureFlag);
    }

    /**
     * @return the number of towers on the island
     */
    public int getNumTowers() { return numTowers; }

    /**
     * @return the colours of the towers on an island
     * @throws EmptyException Exception thrown if there are no towers on the island
     */
    public ColourT getTowersColour() throws EmptyException
    {
        if(numTowers!=0)
            return towers.getColour();
        else
            throw new EmptyException();
    }

    /**
     * Method that changes the tower on the island with a new one of a player that has obtained the dominance on the
     * island
     * @param newTowers the player's towers
     * @throws FullTowersException Exception thrown if the towers of a player are more than the possible number
     * @throws RunOutOfTowersException Exception thrown if a player has finished its towers
     */
    public void towersSwitcher(Towers newTowers) throws FullTowersException, RunOutOfTowersException
    {
        if(newTowers==null) throw new NullPointerException();

        if (numTowers == 0) {
            towers = newTowers;
            numTowers = 1;
            newTowers.availabilityModifier(-1);
        } else {
            towers.availabilityModifier(numTowers);
            towers = newTowers;
            newTowers.availabilityModifier(-numTowers);
        }
    }

    /**
     * Set the MN presence flag to true
     */
    public void setMotherNature() { motherNatureFlag=true; }

    /**
     * Set the MN presence flag to false
     */
    public void removeMotherNature() { motherNatureFlag=false; }

    /**
     * @return true if MN is on the island, false if not
     */
    public boolean isMotherNatureFlag() { return motherNatureFlag; }

    /**
     * increases the number of inhibition tiles on the island
     */
    public void addInhibition() { inhibitionCounter++; }

    /**
     * decreases the number of the inhibition tiles on the island
     */
    public void subInhibition()
    {
        if(inhibitionCounter<1)
            throw new IllegalAccessError("Tried to remove inhibition tile from island, " +
                                        "but there wasn't any");
        inhibitionCounter--;
    }

    /**
     * @return true if there is at least one inhibition tile on the island, false if not
     */
    public boolean getInhibition() { return inhibitionCounter>0; }

    /**
     * @return number of the inhibition tiles on the island
     */
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

    /**
     * Method to print infos of islands
     * @param expertModeFlag true if expert mode, false if standard mode
     */
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
