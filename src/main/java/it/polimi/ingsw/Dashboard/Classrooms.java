package it.polimi.ingsw.Dashboard;
import it.polimi.ingsw.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class Classrooms
{
    private List<Student> []classrooms;
    private List<Teacher> teachers;

    Classrooms()
    {
        int n_colours=0;
        for(Colour c:Colour.values())
        {
            n_colours++;
        }
        classrooms = new List[n_colours];
        for(int i=0; i<n_colours;i++)
        {
            classrooms[i]=new ArrayList<>();
        }
        teachers = new ArrayList<>();
    }
    public void addStudent(Student student)
    {
        for(List l:classrooms)
        {
            if(l.isEmpty()==true)
            {
                l.add(student);
                return;
            }
            else if(((Student)l.get(0)).getColour()==student.getColour())
            {
                l.add(student);
                return;
            }
        }
    }
    public void addTeacher(Teacher teacher) throws TooManyTeachersException, TeacherAlreadyInException
    {
        if(teachers.size()>=5)
            throw new TooManyTeachersException();
        if(teachers.contains(teacher))
            throw new TeacherAlreadyInException();
        teachers.add(teacher);
    }
    public Teacher removeTeacher(Colour colour) throws NoSuchTeacherException
    {
        for(Teacher t:teachers)
        {
            if(t.getColour()==colour)
            {
                teachers.remove(t);
                return t;
            }
        }
        throw new NoSuchTeacherException();
    }
    public int getStudentNum(Colour colour)
    {
        for(List l:classrooms)
        {
            if(((Student)l.get(0)).getColour()==colour)
                return l.size();
        }
        return 0;
    }
    public boolean checkTeacherPresence(Colour colour)
    {
        for(Teacher t:teachers)
        {
            if(t.getColour()==colour)
                return true;
        }
        return false;
    }
}
