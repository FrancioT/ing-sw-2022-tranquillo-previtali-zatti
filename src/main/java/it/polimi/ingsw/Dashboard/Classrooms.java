package it.polimi.ingsw.Dashboard;
import it.polimi.ingsw.*;
import it.polimi.ingsw.Exceptions.*;

import java.util.ArrayList;
import java.util.List;

class Classrooms
{
    private List<List<Student>> classrooms;
    private List<Teacher> teachers;

    Classrooms()
    {
        classrooms=new ArrayList<>();
        for(Colour c:Colour.values())
        {
            classrooms.add(new ArrayList<>());
        }
        teachers = new ArrayList<>();
    }
    public void addStudent(Student student) throws FullClassException
    {
        if(student==null) throw new NullPointerException();

        for(List<Student> l:classrooms)
        {
            if(!l.isEmpty())
            {
                if (l.get(0).getColour() == student.getColour())
                {
                    if(l.size()>10) throw new FullClassException();
                    l.add(student);
                    return;
                }
            }
        }
        for(List<Student> l:classrooms)
        {
            if(l.isEmpty())
            {
                l.add(student);
                return;
            }
        }
        throw new IllegalArgumentException(); // it should never get till this point
    }
    public void addTeacher(Teacher teacher) throws TooManyTeachersException, TeacherAlreadyInException
    {
        if(teacher==null) throw new NullPointerException();

        if(teachers.size()>=5)
            throw new TooManyTeachersException();
        if(teachers.contains(teacher))
            throw new TeacherAlreadyInException();
        teachers.add(teacher);
    }
    public Teacher removeTeacher(Colour colour) throws NoSuchTeacherException
    {
        if(colour==null) throw new NullPointerException();

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
        if(colour==null) throw new NullPointerException();

        for(List<Student> l:classrooms)
        {
            if(l.size()!=0)
                if(l.get(0).getColour()==colour)
                    return l.size();
        }
        return 0;
    }
    public boolean checkTeacherPresence(Colour colour)
    {
        if(colour==null) throw new NullPointerException();

        for(Teacher t:teachers)
        {
            if(t.getColour()==colour)
                return true;
        }
        return false;
    }
}
