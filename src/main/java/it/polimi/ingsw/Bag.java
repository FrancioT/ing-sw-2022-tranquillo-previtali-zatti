package it.polimi.ingsw;

import java.util.List;

public class Bag {
    private static final int n_StudentPC=26;  //number of students per colour
    private List<Student> studentsList;

    public Bag()
    {
        for(Colour c:Colour.values())
        {
            for(int i=0;i<n_StudentPC;i++)
            {
                studentsList.add(new Student(c));
            }
        }
    }
    public Student randomExtraction(){
        int index = (int) (Math.random()*studentsList.size());
        return studentsList.remove(index);
    }

}
