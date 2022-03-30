package it.polimi.ingsw;

import java.util.List;

public class Towers {
    private int availableTowers;
    private final ColourT colour;

    public Towers(ColourT colour) { this.colour=colour; }
    //public int availabilityChecker(){}
    //public void availabilityModifier(int delta){}
    public ColourT getColour() { return colour; };
}
