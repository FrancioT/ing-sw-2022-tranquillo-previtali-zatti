package it.polimi.ingsw.Dashboard;
import it.polimi.ingsw.*;

import java.util.List;

public class Dashboard
{
    private Entrance entrance;
    private Classrooms classrooms;
    private Towers towers;

    Dashboard(Classrooms classrooms, Entrance entrance, Towers towers)
    {
        classrooms=classrooms;
        entrance=entrance;
        towers=towers;
    }

    public void entranceFiller(List<Student> students) throws FullEntranceException
    {
        entrance.entranceFiller(students);
    }
    public Student entranceEmptier(Colour c) throws EmptyException, UnexistingException
    {
        return entrance.entranceEmptier(c);
    }
    public List<Colour> getStudents()
    {
        return entrance.getStudents();
    }
}
