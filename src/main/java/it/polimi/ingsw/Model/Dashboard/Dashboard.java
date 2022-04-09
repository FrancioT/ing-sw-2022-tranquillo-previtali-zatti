package it.polimi.ingsw.Model.Dashboard;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Student;
import it.polimi.ingsw.Model.Teacher;
import it.polimi.ingsw.Model.Towers;

import java.util.ArrayList;
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
    public void addStudent(Student student) throws FullClassException
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
    public Towers getDTowers() { return towers; }
    public void studentsSwap(Colour entranceStudentColour, Colour classroomStudentColour)
                                                         throws NoSuchStudentException, EmptyException,
                                                                FullEntranceException, FullClassException
    {
        Student stud1=classrooms.removeStudent(classroomStudentColour);
        Student stud2=entrance.entranceEmptier(entranceStudentColour);
        classrooms.addStudent(stud2);
        List<Student> tmp=new ArrayList<>();
        tmp.add(stud1);
        entrance.entranceFiller(tmp);
    }
}
