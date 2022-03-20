package it.polimi.ingsw;

import java.util.List;

public class Teacher {
    private Player currentPos;
    private Colour colour;
    private static List<Teacher> teachers;
    private static boolean createdFlag=false;

    Teacher(Colour c)
    {
        colour=c;
    }

    public Player getCurrentPos(){
        return currentPos;
    }

    public static List<Teacher> teachersCreator()
    {
        if(createdFlag==false)
        {
            for(Colour c: Colour.values())
            {
                teachers.add(new Teacher(c));
            }
            createdFlag=true;
        }
        return teachers;
    }
}
