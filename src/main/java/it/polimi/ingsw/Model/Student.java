package it.polimi.ingsw.Model;

import java.io.Serializable;

public class Student implements Serializable
{
    private final Colour colour;
    static final long serialVersionUID= 70000L;

    public Student(Colour c)
    {
        colour=c;
    }

    public Colour getColour() { return colour; }
}
