package it.polimi.ingsw.Dashboard;

import it.polimi.ingsw.Colour;
import it.polimi.ingsw.Exceptions.EmptyException;
import it.polimi.ingsw.Exceptions.FullEntranceException;
import it.polimi.ingsw.Exceptions.UnexistingException;
import it.polimi.ingsw.Student;

import java.util.ArrayList;
import java.util.List;

class Entrance
{
    private List<Student> students;

    Entrance()
    {
        students=new ArrayList<>();
    }
    void entranceFiller(List<Student> students) throws FullEntranceException
    {
        if(this.students.size()+students.size() > 9)
            throw new FullEntranceException();

        this.students.addAll(students);
    }
    Student entranceEmptier(Colour c) throws EmptyException, UnexistingException
    {
        if(students.size()==0)
            throw new EmptyException();
        for(Student s: students)
        {
            if(s.getColour()==c)
            {
                students.remove(s);
                return s;
            }
        }
        throw new UnexistingException();
    }
    List<Colour> getStudents()
    {
        List<Colour> studColours=new ArrayList<>();
        for(Student s: students)
        {
            studColours.add(s.getColour());
        }
        return studColours;
    }
}
