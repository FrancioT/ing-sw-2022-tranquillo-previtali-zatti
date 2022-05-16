package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Exceptions.FullTowersException;
import it.polimi.ingsw.Model.Exceptions.RunOutOfTowersException;

import java.io.Serializable;

public class Towers implements Serializable
{
    private int availableTowers;
    private final ColourT colour;
    static final long serialVersionUID= 80213L;

    public Towers(ColourT colour, int mod)  //mod=0 (there are 2 or 4 players)
                                            // mod=1 (there are 3 players)
    {
        this.colour=colour;
        availableTowers=mod*6+(1-mod)*8;
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

    public void towersPrinter()
    {
        System.out.println("You have " + this.availableTowers + this.colour.toString() + "towers" + "\n");
    }
}


