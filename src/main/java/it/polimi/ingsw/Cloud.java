package it.polimi.ingsw;
import it.polimi.ingsw.Dashboard.*;
import it.polimi.ingsw.Exceptions.FullEntranceException;

public class Cloud extends Tile{
    private Bag bag;

    public void cloudFiller(int n) {
        for (int i = 0; i < n; i++) {
            addStudent(bag.randomExtraction());
        }
    }

    @Override
    public void removeStudents() {
        for (int i = 0; i < this.getStudents().size(); i++) {
            this.getStudents().remove(0);
        }
    }

    public void cloudEmptier(Dashboard d) throws FullEntranceException
    {
        d.entranceFiller(this.getStudents());
        removeStudents();
    }
}
