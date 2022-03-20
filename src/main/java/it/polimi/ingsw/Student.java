package it.polimi.ingsw;

public class Student {
    private colour colour;

    Student(colour c)
    {
        colour=c;
    }

    public it.polimi.ingsw.colour getColour() {
        return colour;
    }

    public Student clone()
    {
        return new Student(colour);
    }
}
