package it.polimi.ingsw;

import it.polimi.ingsw.Exceptions.FullTowersException;

public class Towers {
    private int availableTowers;
    private final ColourT colour;

    public Towers(ColourT colour) { this.colour=colour; }

    public int availabilityChecker()
    {
        return availableTowers;
    }

    public void availabilityModifier(int delta) throws FullTowersException
    {
        if(availableTowers + delta < 0)
        {
            throw new FullTowersException();
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

    public ColourT getColour() { return colour; };
}
