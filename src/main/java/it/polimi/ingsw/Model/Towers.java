package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Exceptions.FullTowersException;
import it.polimi.ingsw.Model.Exceptions.RunOutOfTowersException;

import java.io.Serializable;

public class Towers implements Serializable
{
    private int availableTowers;
    private final ColourT colour;
    boolean mode;
    static final long serialVersionUID= 80213L;

    /**
     * Constructor of towers, the mod parameter is necessary to know how many towers there will be available for each
     * player in the game
     * @param colour the towers' color
     * @param mod the mod parameter (mod = 0 for 2/4 players; mod = 1 for 3 players)
     */
    public Towers(ColourT colour, int mod)  //mod=0 (there are 2 or 4 players)
                                            // mod=1 (there are 3 players)
    {
        this.colour=colour;
        availableTowers=mod*6+(1-mod)*8;
        mode = mod == 0;
    }

    /**
     * @return how many towers are still on the dashboard, available to be moved on islands
     */
    public int availabilityChecker()
    {
        return availableTowers;
    }

    /**
     * @param delta the number of towers that has to be added
     * @throws FullTowersException Exception thrown if the maximum number of towers is surpassed
     * @throws RunOutOfTowersException Exception thrown when a player finishes his available towers
     */
    public void availabilityModifier(int delta) throws FullTowersException, RunOutOfTowersException
    {
        if(availableTowers + delta <= 0)
        {
            availableTowers=0;
            throw new RunOutOfTowersException();
        }

        if(mode) {
            if (availableTowers + delta > 8) {
                throw new FullTowersException();
            }
        }

        if(!mode) {
            if (availableTowers + delta > 6) {
                throw new FullTowersException();
            }
        }

        if((availableTowers + delta >= 0) && (((mode) && (availableTowers + delta <= 8)) || ((!mode) && (availableTowers + delta <= 6))))
        {
            availableTowers = availableTowers + delta;
        }
    }

    /**
     * @return the towers' color
     */
    public ColourT getColour() { return colour; }

    /**
     * method to print the towers' info
     */
    public void towersPrinter(){

        final String tBlack = "\u001B[30m";
        final String tWhite = "\u001B[37m";
        final String tGrey = "\u001B[38m";

        final String tBlack_BACKGROUND = "\u001B[40m";
        final String tWhite_BACKGROUND = "\u001B[47m";

        final String cRESET = "\u001B[0m";

        switch (this.colour) {
            case black:
                System.out.println(tBlack_BACKGROUND + tWhite + this.colour.toString() + " towers" + cRESET + ".");
                break;
            case white:
                System.out.println(tWhite_BACKGROUND + tBlack + this.colour.toString() + " towers" + cRESET + ".");
                break;
            case grey:
                System.out.println(tWhite_BACKGROUND + tGrey + this.colour.toString() + " towers" + cRESET + ".");
                break;
            default:
                break;
        }
        System.out.println();
    }
}


