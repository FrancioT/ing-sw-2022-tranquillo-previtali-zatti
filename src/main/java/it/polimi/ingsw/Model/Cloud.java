package it.polimi.ingsw.Model;
import it.polimi.ingsw.Model.Exceptions.FullEntranceException;

import java.util.ArrayList;
import java.util.List;

public class Cloud extends Tile{
    final private Bag bag;

    public Cloud(Bag bag)
    {
        super();
        this.bag=bag;
    }

    public void cloudFiller(int n) throws IndexOutOfBoundsException
    {
        if(n<0) throw new IndexOutOfBoundsException();

        for (int i = 0; i < n; i++)
        {
            addStudent(bag.randomExtraction());
        }
    }

    public void cloudEmptier(Player player) throws FullEntranceException
    {
        if(player==null) throw new NullPointerException();

        List<Student> studentsTmp= new ArrayList<>(studentsList);
        studentsList.clear();
        player.entranceFiller(studentsTmp);
    }
}
