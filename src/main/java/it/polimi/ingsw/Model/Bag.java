package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Exceptions.RunOutOfStudentsException;

import java.util.ArrayList;
import java.util.List;

public class Bag {
    private static final int n_StudentPC=26;  //number of students per colour
    private List<Student> studentsList;

    /**
     * First constructor of the bag where we only put 2 students per color
     */
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

    /**
     * Filler of the bag used after the initialization to fill the bag with all the remaining students
     * @param bag it is the bag created at the start of the game
     */
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

    /**
     * Method used to randomly extract a student from the bag
     * @return a random student
     * @throws RunOutOfStudentsException Exception thrown when there are no more students in the bag
     */
    public synchronized Student randomExtraction() throws RunOutOfStudentsException
    {
        if(studentsList.size()<=0)
            throw new RunOutOfStudentsException();
        int index = (int) (Math.random()*studentsList.size());
        return studentsList.remove(index);
    }

    /**
     * Method used to add back a student in the bag
     * @param student the student to re-insert in the bag
     */
    public synchronized void addStudentBag(Student student){
        studentsList.add(student);
    }
}
