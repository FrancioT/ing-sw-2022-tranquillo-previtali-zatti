package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.List;

public class Bag {
    private static final int n_StudentPC=26;  //number of students per colour
    private List<Student> studentsList;

    public Bag()
    {
        studentsList=new ArrayList<>();
        for(Colour c:Colour.values())
        {
            for(int i=0;i<2;i++)
            {
                studentsList.add(new Student(c));
            }
        }
    }
    public Bag(Bag bag)
    {
        studentsList=new ArrayList<>();
        for(Colour c:Colour.values())
        {
            for(int i=0;i<n_StudentPC-2;i++)
            {
                studentsList.add(new Student(c));
            }
        }
        this.studentsList.addAll(bag.studentsList);
    }
    public synchronized Student randomExtraction(){
        int index = (int) (Math.random()*studentsList.size());
        return studentsList.remove(index);
    }

    public synchronized void addStudentBag(Student student){
        studentsList.add(student);
    }
}
