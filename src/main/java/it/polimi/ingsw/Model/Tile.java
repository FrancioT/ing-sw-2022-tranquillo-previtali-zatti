package it.polimi.ingsw.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tile implements Serializable
{
    protected List<Student> studentsList;
    static final long serialVersionUID= 80000L;

    /**
     * Constructor of tile
     */
    public Tile() { studentsList=new ArrayList<>(); }

    /**
     * Method to add a student on a tile
     * @param student the student to add on the tile
     */
    public void addStudent(Student student)
    {
        if(student==null) throw new NullPointerException();

        studentsList.add(student);
    }

    /**
     * @return the list of students' colour on the tile
     */
    public List<Colour> getStudentsColours()
    {
        List<Colour> colours=new ArrayList<>();
        for(Student s: studentsList)
        {
            colours.add(s.getColour());
        }
        return colours;
    }

    /**
     * Method to print infos of a tile
     */
    public void tilePrinter()
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

        for(Colour c1: this.getStudentsColours())
        {
            coloursMap.replace(c1, coloursMap.get(c1)+1);
        }

        System.out.println(sBlue + coloursMap.get(Colour.blue)
                           + space + sRed + coloursMap.get(Colour.red)
                           + space + sGreen + coloursMap.get(Colour.green)
                           + space + sYellow + coloursMap.get(Colour.yellow)
                           + space + sPink + coloursMap.get(Colour.pink) + cRESET);
    }
}
