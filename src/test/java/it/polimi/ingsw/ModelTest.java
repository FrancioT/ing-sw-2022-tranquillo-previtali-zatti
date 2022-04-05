package it.polimi.ingsw;

import it.polimi.ingsw.Exceptions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest
{
    Model model;
    final private static String P1="Francio";
    final private static String P2="Tarallo";

    @BeforeEach
    void initialization()
    {
        List<String> uIDs=new ArrayList<>();
        uIDs.add(P1);
        uIDs.add(P2);
        model= new Model(uIDs);
    }

    //@Test
    //void teacherDominance()
    //{
    //}

    @Test
    void addStudentDashboard() throws NoSuchPlayerException, FullClassException
    {
        model.addStudentDashboard(P1, new Student(Colour.green));
        try {
            model.addStudentDashboard(null, null);
            assertTrue(false);
        } catch (NullPointerException e){}
        try {
            model.addStudentDashboard("aaa", new Student(Colour.yellow));
            assertTrue(false);
        } catch (NoSuchPlayerException e){}
    }

    @Test
    void addStudentIsland()
    {
        model.addStudentIsland(0, new Student(Colour.pink));
        model.addStudentIsland(11, new Student(Colour.blue));
        try {
            model.addStudentIsland(-1, new Student(Colour.red));
            assertTrue(false);
        } catch (IndexOutOfBoundsException e){}
        try {
            model.addStudentIsland(12, new Student(Colour.yellow));
            assertTrue(false);
        } catch (IndexOutOfBoundsException e){}
    }

    @Test
    void cloudsFill_empty() throws FullEntranceException,
                                   NoSuchPlayerException
    {
        model.cloudsFiller(3);
        try {
            model.cloudEmptier(P1,-1);
            assertTrue(false);
        } catch (IndexOutOfBoundsException e){}
        try {
            model.cloudEmptier(P1,4);
            assertTrue(false);
        } catch (IndexOutOfBoundsException e){}
        try {
            model.cloudEmptier(null,100);
            assertTrue(false);
        } catch (NullPointerException e){}
        try {
            model.cloudEmptier("aaa",-1);
            assertTrue(false);
        } catch (IndexOutOfBoundsException e){}

    }


    @Test
    void cardDiscarder() throws NoSuchPlayerException
    {
        model.cardDiscarder(P1, 3);
        try {
            model.cardDiscarder(null, 9);
            assertTrue(false);
        } catch (NullPointerException e){}
        try {
            model.cardDiscarder("abab", 8);
            assertTrue(false);
        } catch (NoSuchPlayerException e){}
    }

    @Test
    void entranceEmptier() throws EmptyException, FullEntranceException,
                                  NoSuchStudentException, NoSuchPlayerException
    {
        try {
            model.getStudents(null);
            assertTrue(false);
        } catch (NullPointerException e){}
        try {
            model.getStudents("sgds");
            assertTrue(false);
        } catch (NoSuchPlayerException e){}
        model.cloudsFiller(3);
        model.cloudEmptier(P2, 0);
        model.cloudEmptier(P2, 1);
        List<Colour> tmp= model.getStudents(P2);
        model.entranceEmptier(P2, tmp.get(0));
        tmp.remove(0);
        for(Colour c: model.getStudents(P2))
            tmp.remove(c);

        assertTrue(tmp.size()==0);

        try {
            model.entranceEmptier(null, null);
            assertTrue(false);
        } catch (NullPointerException e){}
        try {
            model.entranceEmptier("ekfir", null);
        } catch (NoSuchPlayerException e){}
    }

    @Test
    void getCurrPosMN() throws Exception
    {
        int pos= model.getCurrPosMN();
        assertTrue(pos==0);
        model.moveMN(4);
        pos= model.getCurrPosMN();
        assertTrue(pos==4);
        model.moveMN(10);
        pos= model.getCurrPosMN();
        assertTrue(pos==2);
    }

    /*
    @Test
    void moveMN() {
    }*/

    @Test
    void getLastCardValue() throws NoSuchPlayerException, EmptyException
    {
        try {
            model.getLastCardValue(null);
            assertTrue(false);
        } catch (NullPointerException e){}
        try {
            model.getLastCardValue("rowuq");
            assertTrue(false);
        } catch (NoSuchPlayerException e){}
        try {
            model.getLastCardValue(P2);
            assertTrue(false);
        } catch (EmptyException e){}
        model.cardDiscarder(P1, 3);
        assertTrue(model.getLastCardValue(P1)==2);
    }
}