package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

public class Teacher {
    private Player currentPos;
    private final Colour colour;
    private static List<Teacher> teachers;
    private static boolean createdFlag=false;

    private Teacher(Colour c)
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

    public static List<Teacher> teachersCreator()
    {
        if(createdFlag==false)
        {
            teachers= new ArrayList<>();
            for(Colour c: Colour.values())
            {
                teachers.add(new Teacher(c));
            }
            createdFlag=true;
        }
        return teachers;
    }

    public void setNewPos(Player player) { currentPos=player; }
}
