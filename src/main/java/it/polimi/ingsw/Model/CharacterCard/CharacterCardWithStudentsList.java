package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.NoSuchStudentException;
import it.polimi.ingsw.Model.Student;

import java.util.ArrayList;
import java.util.List;

abstract class CharacterCardWithStudentsList extends CharacterCard{
    List<Student> studentsList;

    CharacterCardWithStudentsList(int cardID, int price) {
        super(cardID, price);
    }

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

    public List<Colour> getColoursOnCard(){
        List<Colour> colours = new ArrayList<>();
        for(Student s: studentsList)
            colours.add(s.getColour());
        return colours;
    }
}
