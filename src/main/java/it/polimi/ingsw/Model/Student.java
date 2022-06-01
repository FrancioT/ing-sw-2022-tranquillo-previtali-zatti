package it.polimi.ingsw.Model;

import java.io.Serializable;

public class Student implements Serializable
{
    private final Colour colour;
    static final long serialVersionUID= 70000L;

    /**
     * Constructor of student
     * @param c the color of the student
     */
    public Student(Colour c)
    {
        colour=c;
    }

    /**
     * @return the student's colour
     */
    public Colour getColour() { return colour; }
}
