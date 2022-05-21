package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Exceptions.FullTowersException;
import it.polimi.ingsw.Model.Exceptions.NoSuchStudentException;
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

    public void towersPrinter(){

        final String tBlack = "\u001B[30m";
        final String tWhite = "\u001B[37m";
        final String tGrey = "\u001B[38m";

        final String tBlack_BACKGROUND = "\u001B[40m";
        final String tWhite_BACKGROUND = "\u001B[47m";

        final String cRESET = "\u001B[0m";

        switch (this.colour) {
            case black:
                System.out.println(tWhite_BACKGROUND + tBlack + this.colour.toString() + " towers" + cRESET);
                break;
            case white:
                System.out.println(tBlack_BACKGROUND + tWhite + this.colour.toString() + " towers" + cRESET);
                break;
            case grey:
                System.out.println(tWhite_BACKGROUND + tGrey + this.colour.toString() + " towers" + cRESET);
                break;
            default:
                break;
                // Cambiare questa eccezione con una apposita per il colore delle torri
                // ALLA FINE HO TENUTO LO SWITCH PERCHE' SE NO NON RIESCO A STAMPARE BENE I COLORI
        }
    }
}


