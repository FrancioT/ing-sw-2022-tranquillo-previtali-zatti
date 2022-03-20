package it.polimi.ingsw;

import com.sun.tools.javac.util.List;

public class Bag {
    
    private List<Student> studentsList;

    public Student randomExtraction(){
        int index = (int) (Math.random()*studentsList.size());
        return studentsList.remove(index);
    }

}
