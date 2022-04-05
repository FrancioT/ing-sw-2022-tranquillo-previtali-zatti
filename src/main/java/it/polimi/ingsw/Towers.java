package it.polimi.ingsw;

import it.polimi.ingsw.Exceptions.FullTowersException;
import it.polimi.ingsw.Exceptions.RunOutOfTowersException;

public class Towers {
    private int availableTowers;
    private final ColourT colour;

    public Towers(ColourT colour, int mod)  //mod=1 (there are 2 or 4 players)
                                                // mod=0 (there are 3 players)
    {
        this.colour=colour;
        availableTowers=mod*8+(1-mod)*6;
    }

    public int availabilityChecker()
    {
        return availableTowers;
    }

    public void availabilityModifier(int delta) throws FullTowersException, RunOutOfTowersException
    {
        if(availableTowers + delta < 0)
        {
            throw new RunOutOfTowersException();
        }

        if(availableTowers + delta > 8)
        {
            throw new FullTowersException();
        }

        if((availableTowers + delta >= 0) && (availableTowers + delta <= 8))
        {
            availableTowers = availableTowers + delta;
        }
    }

    public ColourT getColour() { return colour; }
}
