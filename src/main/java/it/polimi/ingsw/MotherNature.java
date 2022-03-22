package it.polimi.ingsw;

public class MotherNature {

    private Island currentPos;

    public MotherNature(Island initialPos)
    {
        currentPos=initialPos;
    }

    public Island getCurrentPos()
    {
        return currentPos;
    }

    public void jumpNextPos(Island i)
    {
        currentPos.setMotherNatureFlag();
        currentPos=i;
        currentPos.setMotherNatureFlag();
    }

}
