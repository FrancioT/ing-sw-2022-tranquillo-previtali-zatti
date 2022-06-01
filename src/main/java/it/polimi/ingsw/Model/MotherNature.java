package it.polimi.ingsw.Model;

public class MotherNature {

    private Island currentPos;

    /**
     * Constructor of MN, it sets its starting position
     * @param initialPos the island where MN is added
     */
    public MotherNature(Island initialPos) { currentPos=initialPos; }

    /**
     * @return island where MN is on
     */
    public Island getCurrentPos()
    {
        return currentPos;
    }

    /**
     * Method to move MN from an island to another one
     * @param island island where MN needs to be moved
     */
    public void jumpNextPos(Island island)
    {
        if(island==null) throw new NullPointerException();

        currentPos.removeMotherNature(); /*sets the flag of the old island to false*/
        currentPos=island;
        currentPos.setMotherNature(); /*sets the flag of the new island to true*/
    }

}
