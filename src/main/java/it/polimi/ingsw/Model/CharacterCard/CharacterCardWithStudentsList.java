package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.NoSuchStudentException;
import it.polimi.ingsw.Model.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CharacterCardWithStudentsList extends CharacterCard{
    protected final List<Student> studentsList;
    static final long serialVersionUID= 80310L;

    /**
     * Constructor for the character cards that have students on them
     * @param cardID ID of the card
     * @param price price of the card
     */
    CharacterCardWithStudentsList(int cardID, int price)
    {
        super(cardID, price);
        studentsList=new ArrayList<>();
    }

    /**
     * Method which checks if there is a student of the color requested on a character card and removes it if found.
     * The student found is also returned.
     * @param colour colour of a student we want to remove
     * @return a student of the colour requested if found
     * @throws NoSuchStudentException exception thrown if there isn't any student of the color requested
     */
    Student removeStudent(Colour colour) throws NoSuchStudentException {
        Student student = null;
        for (Student s : studentsList) {
            if (s.getColour() == colour) {
                student = s;
                break;
            }
        }
        if (student != null) {
            studentsList.remove(student);
            return student;
        } else
            throw new NoSuchStudentException();
    }

    /**
     * Method which returns the colours of the students available on the card
     */
    public List<Colour> getColoursOnCard(){
        List<Colour> colours = new ArrayList<>();
        for(Student s: studentsList)
            colours.add(s.getColour());
        return colours;
    }

    /**
     * Method to print the information of the card
     */
    @Override
    public void ccPrinter()
    {
        super.ccPrinter();
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

        for(Colour c1: this.getColoursOnCard())
        {
            coloursMap.replace(c1, coloursMap.get(c1)+1);
        }

        System.out.println("This card has also these students on her: " + sBlue + coloursMap.get(Colour.blue)
                + space + sRed + coloursMap.get(Colour.red)
                + space + sGreen + coloursMap.get(Colour.green)
                + space + sYellow + coloursMap.get(Colour.yellow)
                + space + sPink + coloursMap.get(Colour.pink) + cRESET + "\n");
    }
}
