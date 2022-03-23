package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

public class Teacher {
    private Player currentPos;
    private final Colour colour;

    Teacher(Colour c)
    {
        colour=c;
    }
    public Player getCurrentPos(){
        return currentPos;
    }
    public Colour getColour()
    {
        return colour;
    }
    public void setNewPos(Player player) { currentPos=player; }
}
