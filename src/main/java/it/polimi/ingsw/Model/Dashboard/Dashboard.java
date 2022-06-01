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

    /**
     * Constructor of a player's dashboard
     * @param towers the towers that are assigned to the player
     */
    public Dashboard(Towers towers)
    {
        classrooms=new Classrooms();
        entrance=new Entrance();
        this.towers=towers;
    }

    /**
     * Method to add a list of students in a player's entrance
     * @param students the list of students to add
     * @throws FullEntranceException Exception thrown if the entrance is full
     */
    public void entranceFiller(List<Student> students) throws FullEntranceException
    {
        entrance.entranceFiller(students);
    }

    /**
     * Method to remove a student of a selected colour from a player's entrance
     * @param c the colour of the student to remove
     * @return the student (of the colour chosen) removed
     * @throws EmptyException Exception thrown if the entrance is empty
     * @throws NoSuchStudentException Exception thrown if there isn't a student of the selected colour in the entrance
     */
    public Student entranceEmptier(Colour c) throws EmptyException, NoSuchStudentException
    {
        return entrance.entranceEmptier(c);
    }

    /**
     * Method to know the colours of the students in a player's entrance
     * @return
     */
    public List<Colour> getStudents()
    {
        return entrance.getStudents();
    }

    /**
     * Method to add a student in a player's classroom
     * @param student the student to add
     * @throws FullClassException Exception thrown if the classroom where the student had to be added is full
     */
    public void addStudent(Student student) throws FullClassException
    {
        classrooms.addStudent(student);
    }

    /**
     * Method to add a teacher to a player's classroom
     * @param teacher the teacher to add
     * @throws TooManyTeachersException Exception thrown if all the teachers are already in the player's classrooms
     * @throws TeacherAlreadyInException Exception thrown if the selected teacher is already in the player's classroom
     */
    public void addTeacher(Teacher teacher) throws TooManyTeachersException, TeacherAlreadyInException
    {
        classrooms.addTeacher(teacher);
    }

    /**
     * Method to remove a teacher from a player's classroom
     * @param colour the colour of the teacher to remove
     * @return the teacher of the selected color
     * @throws NoSuchTeacherException Exception thrown if the teacher of the requested color is not
     *                                in the player's classroom
     */
    public Teacher removeTeacher(Colour colour) throws NoSuchTeacherException
    {
        return classrooms.removeTeacher(colour);
    }

    /**
     * Method to know how many students of a selected color there are in a player's classroom
     * @param colour the colour of the students whose number you want to know
     * @return the number of students which are in the player's classroom
     */
    public int getStudentNum(Colour colour)
    {
        return classrooms.getStudentNum(colour);
    }

    /**
     * Method to know if a teacher of a requested colour is in a player's classroom
     * @param colour the colour of the teacher
     * @return true if the teacher is in the classroom or false if it is not
     */
    public boolean checkTeacherPresence(Colour colour)
    {
        return classrooms.checkTeacherPresence(colour);
    }

    /**
     * Method to know which towers there are in a dashboard
     * @return the towers
     */
    public Towers getDTowers() { return towers; }

    /**
     * Method to swap a pair of students between classroom and entrance; the first colour is the student
     * to move from entrance to classroom, the second is the one to be moved from classroom to entrance
     * @param entranceStudentColour student to remove from entrance
     * @param classroomStudentColour student to remove from classroom
     * @throws NoSuchStudentException Exception thrown if one of the requested students is not available
     * @throws EmptyException Exception thrown if the entrance is empty
     * @throws FullEntranceException Exception thrown if the entrance is full
     * @throws FullClassException Exception thrown if the classroom is full
     */
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
        try {
            classrooms.addStudent(stud2);
        }catch (FullClassException e)
        {
            classrooms.addStudent(stud1);
            List<Student> entranceStudent=new ArrayList<>();
            entranceStudent.add(stud2);
            entrance.entranceFiller(entranceStudent);
            throw e;
        }
        List<Student> entranceStudent=new ArrayList<>();
        entranceStudent.add(stud1);
        entrance.entranceFiller(entranceStudent);
    }

    /**
     * Method to remove a student from a classroom
     * @param colour the colour of the student you want to remove
     * @return the student removed
     * @throws NoSuchStudentException Exception thrown if the requested colour is not available
     */
    public Student removeStudentClassroom(Colour colour) throws NoSuchStudentException {
        return classrooms.removeStudent(colour);
    }

    /**
     * Method to print the infos about a player's dashboard
     */
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
