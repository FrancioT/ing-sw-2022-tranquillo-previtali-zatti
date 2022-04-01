package it.polimi.ingsw.Dashboard;
import it.polimi.ingsw.*;
import it.polimi.ingsw.Exceptions.*;

import java.util.List;

public class Dashboard
{
    private final Entrance entrance;
    private final Classrooms classrooms;
    private final Towers towers;

    public Dashboard(Towers towers)
    {
        classrooms=new Classrooms();
        entrance=new Entrance();
        this.towers=towers;
    }

    public void entranceFiller(List<Student> students) throws FullEntranceException
    {
        entrance.entranceFiller(students);
    }
    public Student entranceEmptier(Colour c) throws EmptyException, NoSuchStudentException
    {
        return entrance.entranceEmptier(c);
    }
    public List<Colour> getStudents()
    {
        return entrance.getStudents();
    }
    public void addStudent(Student student)
    {
        classrooms.addStudent(student);
    }
    public void addTeacher(Teacher teacher) throws TooManyTeachersException, TeacherAlreadyInException
    {
        classrooms.addTeacher(teacher);
    }
    public Teacher removeTeacher(Colour colour) throws NoSuchTeacherException
    {
        return classrooms.removeTeacher(colour);
    }
    public int getStudentNum(Colour colour)
    {
        return classrooms.getStudentNum(colour);
    }
    public boolean checkTeacherPresence(Colour colour)
    {
        return classrooms.checkTeacherPresence(colour);
    }
    public Towers getTower(){return towers;}
}
