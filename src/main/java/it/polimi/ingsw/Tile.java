package it.polimi.ingsw;

import java.util.List;

public class Tile {
    protected List<Student> studentsList;

    public void addStudent(Student s){
        studentsList.add(s);
    }

    public List<Student> getStudents() {
        return studentsList;
    }
}
