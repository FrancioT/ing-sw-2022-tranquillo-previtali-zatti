package it.polimi.ingsw.Model;

import java.io.Serializable;

public class Teacher implements Serializable
{
    transient private Player currentPos;
    private final Colour colour;
    static final long serialVersionUID= 70001L;

    /**
     * Constructor of teacher
     * @param c the teacher's colour
     */
    public Teacher(Colour c)
    {
        colour=c;
        currentPos=null;
    }

    /**
     * @return the teacher's current position (which is a player or null if not positioned yet)
     */
    public Player getCurrentPos(){
        return currentPos;
    }

    /**
     * @return the teacher's color
     */
    public Colour getColour()
    {
        return colour;
    }

    /**
     * Method to set the new position of a teacher
     * @param player the player that has earned the teacher
     */
    public void setNewPos(Player player)
    {
        if(player==null) throw new NullPointerException();

        currentPos=player;
    }
}
