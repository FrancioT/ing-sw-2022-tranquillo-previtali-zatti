package it.polimi.ingsw.Model;

public class Teacher
{
    transient private Player currentPos;
    private final Colour colour;
    static final long serialVersionUID= 70001L;

    public Teacher(Colour c)
    {
        colour=c;
        currentPos=null;
    }
    public Player getCurrentPos(){
        return currentPos;
    }
    public Colour getColour()
    {
        return colour;
    }
    public void setNewPos(Player player)
    {
        if(player==null) throw new NullPointerException();

        currentPos=player;
    }
}
