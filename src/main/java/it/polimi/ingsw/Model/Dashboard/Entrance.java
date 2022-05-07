package it.polimi.ingsw.Model.Dashboard;

import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.EmptyException;
import it.polimi.ingsw.Model.Exceptions.FullEntranceException;
import it.polimi.ingsw.Model.Exceptions.NoSuchStudentException;
import it.polimi.ingsw.Model.Student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Entrance implements Serializable
{
    private List<Student> students;
    static final long serialVersionUID= 80211L;

    Entrance()
    {
        students=new ArrayList<>();
    }
    void entranceFiller(List<Student> students) throws FullEntranceException
    {
        if(students==null || students.contains(null)) throw new NullPointerException();

        if(this.students.size()+students.size() > 9)
            throw new FullEntranceException();

        this.students.addAll(students);
    }
    Student entranceEmptier(Colour colour) throws EmptyException, NoSuchStudentException
    {
        if(colour==null) throw new NullPointerException();

        if(students.size()==0)
            throw new EmptyException();
        for(Student s: students)
        {
            if(s.getColour()==colour)
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
