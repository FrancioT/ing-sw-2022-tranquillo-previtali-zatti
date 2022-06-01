package it.polimi.ingsw.Model.Dashboard;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Student;
import it.polimi.ingsw.Model.Teacher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Classrooms implements Serializable
{
    private List<List<Student>> classrooms;
    private List<Teacher> teachers;
    static final long serialVersionUID= 80212L;

    /**
     * Constructor of the classrooms
     */
    Classrooms()
    {
        classrooms=new ArrayList<>();
        for(Colour c:Colour.values())
        {
            classrooms.add(new ArrayList<>());
        }
        teachers = new ArrayList<>();
    }

    /**
     * Method to add a student in the classroom
     * @param student the student to add in the classroom
     * @throws FullClassException exception thrown when the classroom chosen is full
     */
    void addStudent(Student student) throws FullClassException
    {
        if(student==null) throw new NullPointerException();

        for(List<Student> l:classrooms)
        {
            if(!l.isEmpty())
            {
                if (l.get(0).getColour() == student.getColour())
                {
                    if(l.size()>9) throw new FullClassException();
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

    /**
     * Method to remove a student from the classroom
     * @param colour the colour of the student that we want to remove
     * @return the student of the colour chosen which has been removed (LIFO policy)
     * @throws NoSuchStudentException Exception thrown if there are no students of the chosen colour
     */
    Student removeStudent(Colour colour) throws NoSuchStudentException
    {
        if(colour==null) throw new NullPointerException();

        for(List<Student> l:classrooms)
            if(!l.isEmpty())
                if (l.get(0).getColour() == colour)
                    return l.remove(0);
        throw new NoSuchStudentException();
    }

    /**
     * Method to add a teacher in the classroom of the corresponding color
     * @param teacher the teacher to add
     * @throws TooManyTeachersException Exception thrown if all the teachers are already in a player's dashboard
     * @throws TeacherAlreadyInException Exception thrown if the selected teacher is already in his classroom
     */
    void addTeacher(Teacher teacher) throws TooManyTeachersException, TeacherAlreadyInException
    {
        if(teacher==null) throw new NullPointerException();

        if(teachers.size()>=5)
            throw new TooManyTeachersException();
        if(teachers.contains(teacher))
            throw new TeacherAlreadyInException();
        teachers.add(teacher);
    }

    /**
     * Method to remove a selected teacher from a classroom
     * @param colour the colour of the teacher that you need to remove
     * @return the teacher of the requested colour
     * @throws NoSuchTeacherException Exception thrown if there isn't a teacher of the requested colour
     */
    Teacher removeTeacher(Colour colour) throws NoSuchTeacherException
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

    /**
     * Method used to acknowledge how many students there are in a classroom of a selected color
     * @param colour the colour of the classroom requested
     * @return the number of students inside the classroom
     */
    int getStudentNum(Colour colour)
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

    /**
     * Method to acknowledge if the teacher of the selected colour is inside a player's classroom
     * @param colour the colour of the teacher
     * @return true if the teacher is present or false if it isn't
     */
    boolean checkTeacherPresence(Colour colour)
    {
        if(colour==null) throw new NullPointerException();

        for(Teacher t:teachers)
        {
            if(t.getColour()==colour)
                return true;
        }
        return false;
    }

    /**
     * Method to print the infos about the classrooms
     */
    public void classroomPrinter()
    {
        final String sBlue = "\u001B[34m";
        final String sRed = "\u001B[31m";
        final String sGreen = "\u001B[32m";
        final String sYellow = "\u001B[33m";
        final String sPink = "\u001B[35m";
        final String space = " ";

        final String cRESET = "\u001B[0m";

        Map<Colour, String> coloursTMap=new HashMap<>();

        for(Colour c: Colour.values())
        {
            if(this.checkTeacherPresence(c))
            {
                coloursTMap.put(c, "T");
            }
            else
            {
                coloursTMap.put(c, "-");
            }
        }

        System.out.println(sBlue + this.getStudentNum(Colour.blue)
                + space + sRed + this.getStudentNum(Colour.red)
                + space + sGreen + this.getStudentNum(Colour.green)
                + space + sYellow + this.getStudentNum(Colour.yellow)
                + space + sPink + this.getStudentNum(Colour.pink) + cRESET +
                " students. \nYour teachers are "
                + sBlue + coloursTMap.get(Colour.blue)
                + space + sRed + coloursTMap.get(Colour.red)
                + space + sGreen + coloursTMap.get(Colour.green)
                + space + sYellow + coloursTMap.get(Colour.yellow)
                + space + sPink + coloursTMap.get(Colour.pink) + cRESET);

    }
}
