package it.polimi.ingsw.Dashboard;
import java.util.*;
import it.polimi.ingsw.*;
import it.polimi.ingsw.Exceptions.*;

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
    Student entranceEmptier(Colour c) throws EmptyException, NoSuchStudentException
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
        throw new NoSuchStudentException();
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
