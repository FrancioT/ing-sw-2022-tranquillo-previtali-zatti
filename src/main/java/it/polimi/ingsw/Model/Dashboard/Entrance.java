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

    /**
     * Constructor of the entrance
     */
    Entrance()
    {
        students=new ArrayList<>();
    }

    /**
     * Method to add a list of students in the entrance
     * @param students the list of students to add
     * @throws FullEntranceException Exception thrown if the entrance is full
     */
    void entranceFiller(List<Student> students) throws FullEntranceException
    {
        if(students==null || students.contains(null)) throw new NullPointerException();

        if(this.students.size()+students.size() > 9)
            throw new FullEntranceException();

        this.students.addAll(students);
    }

    /**
     * Method to remove a student from the entrance
     * @param colour the colour of the student to remove
     * @return the student removed
     * @throws EmptyException Exception thrown if the entrance is empty
     * @throws NoSuchStudentException Exception thrown if there are no students of the requested color
     */
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

    /**
     * Method to know the color of the students in the entrance
     * @return a list of the student's color in the entrance
     */
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
