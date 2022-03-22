package it.polimi.ingsw;

import java.util.List;

abstract public class Tile {
    private List<Student> studentsList;

    public void addStudent(Student s){
        studentsList.add(s);
    }

    abstract public void removeStudents();

    public List<Student> getStudents() {
        return studentsList;
    }
}
