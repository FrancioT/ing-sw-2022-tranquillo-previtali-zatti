package it.polimi.ingsw;

public class MotherNature {

    private Island currentPos;

    public MotherNature(Island initialPos) { currentPos=initialPos; }

    public Island getCurrentPos()
    {
        return currentPos;
    }

    public void jumpNextPos(Island island)
    {
        if(island==null) throw new NullPointerException();

        currentPos.setMotherNatureFlag(); /*sets the flag of the old island to false*/
        currentPos=island;
        currentPos.setMotherNatureFlag(); /*sets the flag of the new island to true*/
    }

}
