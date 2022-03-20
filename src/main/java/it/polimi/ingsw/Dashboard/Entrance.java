package it.polimi.ingsw.Dashboard;
import java.util.*;
import it.polimi.ingsw.*;

class Entrance
{
    private List<Student> students;

    Entrance()
    {
        students=new ArrayList<>();
    }
    void entranceFiller(List<Student> students) throws FullEntranceException
    {
        for(Student s: students)
        {
            if(this.students.size()>9)
                throw new FullEntranceException();
            else
                this.students.add(s.clone());
        }
    }
    Student entranceEmptier(Colour c) throws EmptyException, UnexistingException
    {
        for(Student s: students)
        {
            if(students.size()==0)
                throw new EmptyException();
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
