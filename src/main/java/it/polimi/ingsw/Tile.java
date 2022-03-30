package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

public class Tile {
    protected List<Student> studentsList;

    public Tile() { studentsList=new ArrayList<>(); }

    public void addStudent(Student s){
        studentsList.add(s);
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
