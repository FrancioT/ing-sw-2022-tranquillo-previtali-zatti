package it.polimi.ingsw;

public class Teacher {
    private Player currentPos;
    private final Colour colour;

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
