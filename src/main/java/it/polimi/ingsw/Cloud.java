package it.polimi.ingsw;
import it.polimi.ingsw.Dashboard.*;
import it.polimi.ingsw.Exceptions.FullEntranceException;

import java.util.ArrayList;
import java.util.List;

public class Cloud extends Tile{
    private Bag bag;

    public void cloudFiller(int n) {
        for (int i = 0; i < n; i++) {
            addStudent(bag.randomExtraction());
        }
    }

    public void cloudEmptier(Player p) throws FullEntranceException {
        List<Student> studentsTmp= new ArrayList<>(studentsList);
        studentsList.clear();
        p.entranceFiller(studentsTmp);
    }
}
