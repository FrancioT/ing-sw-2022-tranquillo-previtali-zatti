package it.polimi.ingsw;

public class Student {
    private final Colour colour;

    Student(Colour c)
    {
        colour=c;
    }

    public Colour getColour() {
        return colour;
    }

    public Student clone()
    {
        return new Student(colour);
    }
}
