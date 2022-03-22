package it.polimi.ingsw;

import java.util.List;

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

    public void cloudEmptier(it.polimi.ingsw.Dashboard.Dashboard d){
        d.entranceFiller(this.getStudents());
        removeStudents();
    }
}
