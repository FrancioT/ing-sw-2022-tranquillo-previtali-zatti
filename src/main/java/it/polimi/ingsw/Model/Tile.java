package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.List;

public class Tile {
    protected List<Student> studentsList;

    public Tile() { studentsList=new ArrayList<>(); }

    public void addStudent(Student student)
    {
        if(student==null) throw new NullPointerException();

        studentsList.add(student);
    }

    public List<Colour> getStudentsColours()
    {
        List<Colour> colours=new ArrayList<>();
        for(Student s: studentsList)
        {
            colours.add(s.getColour());
        }
        return colours;
    }
}
