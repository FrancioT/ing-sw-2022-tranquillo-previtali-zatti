package it.polimi.ingsw.Dashboard;
import java.util.*;

class Entrance
{
    private List<Student> students;

    void entranceFiller(List<Student> students) throws FullEntranceException
    {
        for(Student s: students)
        {
            if(this.students.size()>9)
                throw new FullEntranceException;
            else
                this.students.add(s.clone());
        }
    }
    Student entranceEmptier(Colour c) throws EmptyException
    {
        for(Student s: students)
        {
            if(s.value())
        }
    }
}
