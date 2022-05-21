package it.polimi.ingsw.Model.Dashboard;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Student;
import it.polimi.ingsw.Model.Teacher;
import it.polimi.ingsw.Model.Towers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dashboard implements Serializable
{
    private final Entrance entrance;
    private final Classrooms classrooms;
    private final Towers towers;
    static final long serialVersionUID= 80210L;

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
        Student stud1 = classrooms.removeStudent(classroomStudentColour);
        Student stud2;
        try{
            stud2 = entrance.entranceEmptier(entranceStudentColour);
        }catch (Exception e)
        {
            classrooms.addStudent(stud1);
            throw e;
        }
        classrooms.addStudent(stud2);
        List<Student> tmp=new ArrayList<>();
        tmp.add(stud1);
        entrance.entranceFiller(tmp);
    }

    public Student removeStudentClassroom(Colour colour) throws NoSuchStudentException {
        return classrooms.removeStudent(colour);
    }

    public void dashboardPrinter()
    {
        final String sBlue = "\u001B[34m";
        final String sRed = "\u001B[31m";
        final String sGreen = "\u001B[32m";
        final String sYellow = "\u001B[33m";
        final String sPink = "\u001B[35m";
        final String space = " ";

        final String cRESET = "\u001B[0m";

        Map<Colour, Integer> coloursMap=new HashMap<>();

        for(Colour c: Colour.values())
            coloursMap.put(c, Integer.valueOf(0));

        for(Colour c1: this.entrance.getStudents())
        {
            coloursMap.replace(c1, coloursMap.get(c1)+1);
        }

        System.out.println(sBlue + coloursMap.get(Colour.blue)
                + space + sRed + coloursMap.get(Colour.red)
                + space + sGreen + coloursMap.get(Colour.green)
                + space + sYellow + coloursMap.get(Colour.yellow)
                + space + sPink + coloursMap.get(Colour.pink) + cRESET);

        System.out.println("and in your classroom you have ");

        this.classrooms.classroomPrinter();

        System.out.print("With "+towers.availabilityChecker()+" ");
        this.towers.towersPrinter();
    }
}
