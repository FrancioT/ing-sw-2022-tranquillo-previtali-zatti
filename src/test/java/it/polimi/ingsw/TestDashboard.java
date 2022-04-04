package it.polimi.ingsw;

import it.polimi.ingsw.Dashboard.Dashboard;
import it.polimi.ingsw.Exceptions.*;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

class TestDashboard extends TestCase
{
    @Test
    public void testClassrooms() throws TooManyTeachersException, TeacherAlreadyInException, NoSuchTeacherException
    {
        Teacher red_t=new Teacher(Colour.red);
        Teacher blue_t=new Teacher(Colour.blue);
        Teacher pink_t=new Teacher(Colour.pink);
        Teacher yellow_t=new Teacher(Colour.yellow);
        Teacher green_t=new Teacher(Colour.green);
        Dashboard d=new Dashboard(new Towers(ColourT.grey));

        d.addStudent(new Student(Colour.blue));
        assertEquals(d.getStudentNum(Colour.blue), 1);
        d.addStudent(new Student(Colour.blue));
        d.addStudent(new Student(Colour.blue));
        assertEquals(d.getStudentNum(Colour.blue), 3);
        assertEquals(d.getStudentNum(Colour.yellow), 0);
        assertFalse(d.checkTeacherPresence(Colour.red));
        d.addTeacher(red_t);
        try {
            d.addTeacher(red_t);
            assertTrue(false);
        } catch (TeacherAlreadyInException e){}
        assertTrue(d.checkTeacherPresence(Colour.red));
        d.addTeacher(blue_t);
        d.addTeacher(green_t);
        d.addTeacher(yellow_t);
        d.addTeacher(pink_t);
        try {
            d.addTeacher(new Teacher(Colour.red));
            assertTrue(false);
        } catch (TooManyTeachersException e){}
        try {
            d.addTeacher(null);
            assertTrue(false);
        } catch (NullPointerException e){}
        try {
            d.addStudent(null);
            assertTrue(false);
        } catch (NullPointerException e){}
        d.removeTeacher(Colour.red);
        try {
            d.removeTeacher(Colour.red);
            assertTrue(false);
        } catch (NoSuchTeacherException e){}
        d.removeTeacher(Colour.blue);
        d.removeTeacher(Colour.green);
        d.removeTeacher(Colour.yellow);
        d.removeTeacher(Colour.pink);
        try {
            d.removeTeacher(Colour.blue);
            assertTrue(false);
        } catch (NoSuchTeacherException e){}
        try {
            d.removeTeacher(null);
            assertTrue(false);
        } catch (NullPointerException e){}
        try {
            d.getStudentNum(null);
            assertTrue(false);
        } catch (NullPointerException e){}
    }

    @Test
    void testEntrance() throws EmptyException, NoSuchStudentException, FullEntranceException
    {
        Dashboard d=new Dashboard(new Towers(ColourT.grey));
        List<Student> students=new ArrayList<>();
        students.add(new Student(Colour.blue));
        students.add(new Student(Colour.blue));
        students.add(new Student(Colour.blue));
        students.add(new Student(Colour.red));
        students.add(new Student(Colour.red));
        students.add(new Student(Colour.green));
        students.add(new Student(Colour.pink));
        students.add(new Student(Colour.pink));
        students.add(new Student(Colour.pink));

        assertEquals(d.getDTowers().getColour(), ColourT.grey);
        d.entranceFiller(students);
        try
        {
            List<Student> tmp=new ArrayList<>();
            tmp.add(new Student(Colour.red));
            d.entranceFiller(tmp);
            assertTrue(false);
        } catch (FullEntranceException e){}
        try
        {
            List<Student> tmp=null;
            d.entranceFiller(tmp);
            assertTrue(false);
        } catch (NullPointerException e){}
        try
        {
            List<Student> tmp=new ArrayList<>();
            tmp.add(null);
            d.entranceFiller(tmp);
            assertTrue(false);
        } catch (NullPointerException e){}
        int blue_num=0;
        int red_num=0;
        int green_num=0;
        int pink_num=0;
        int yellow_num=0;
        for(Colour c: d.getStudents())
        {
            if(c==Colour.blue) blue_num++;
            if(c==Colour.red) red_num++;
            if(c==Colour.green) green_num++;
            if(c==Colour.pink) pink_num++;
            if(c==Colour.yellow) yellow_num++;
        }
        assertEquals(blue_num, 3);
        assertEquals(red_num, 2);
        assertEquals(green_num, 1);
        assertEquals(pink_num, 3);
        assertEquals(yellow_num, 0);

        d.entranceEmptier(Colour.pink);
        d.entranceEmptier(Colour.red);
        d.entranceEmptier(Colour.blue);
        blue_num=0;
        red_num=0;
        green_num=0;
        pink_num=0;
        yellow_num=0;
        for(Colour c: d.getStudents())
        {
            if(c==Colour.blue) blue_num++;
            if(c==Colour.red) red_num++;
            if(c==Colour.green) green_num++;
            if(c==Colour.pink) pink_num++;
            if(c==Colour.yellow) yellow_num++;
        }
        assertEquals(blue_num, 2);
        assertEquals(red_num, 1);
        assertEquals(green_num, 1);
        assertEquals(pink_num, 2);
        assertEquals(yellow_num, 0);

        d.entranceEmptier(Colour.pink);
        d.entranceEmptier(Colour.pink);
        try
        {
            d.entranceEmptier(Colour.pink);
            assertTrue(false);
        } catch (NoSuchStudentException e){}
        try
        {
            d.entranceEmptier(Colour.yellow);
            assertTrue(false);
        } catch (NoSuchStudentException e){}
        d.entranceEmptier(Colour.blue);
        d.entranceEmptier(Colour.blue);
        d.entranceEmptier(Colour.red);
        d.entranceEmptier(Colour.green);
        try
        {
            d.entranceEmptier(Colour.red);
            assertTrue(false);
        } catch (EmptyException e){}
    }
}
