package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Exceptions.FullEntranceException;
import it.polimi.ingsw.Model.Exceptions.RunOutOfStudentsException;

import java.util.ArrayList;
import java.util.List;

public class Cloud extends Tile{
    transient final private Bag bag;
    static final long serialVersionUID= 80001L;

    /**
     * Constructor of the clouds
     * @param bag the bag from where the clouds will get the students to fill themselves
     */
    public Cloud(Bag bag)
    {
        super();
        this.bag=bag;
    }

    /**
     * Method used to fill the clouds with random students
     * @param n this parameter indicated how many students the cloud will have to add on itself (n = 3 for 2/4
     *          players; n = 4 for 3 players)
     * @throws IndexOutOfBoundsException Exception thrown if the parameters given is unacceptable
     * @throws RunOutOfStudentsException Exception thrown if there are no more students in the bag
     */
    public void cloudFiller(int n) throws IndexOutOfBoundsException, RunOutOfStudentsException
    {
        if(n<0) throw new IndexOutOfBoundsException();

        for (int i = 0; i < n; i++)
        {
            addStudent(bag.randomExtraction());
        }
    }

    /**
     * Method used to empty the clouds and to fill, with the students removed, the entrance of the players
     * @param player the player's entrance where to add the students
     * @throws FullEntranceException Exception thrown when the entrance is full
     */
    public void cloudEmptier(Player player) throws FullEntranceException
    {
        if(player==null) throw new NullPointerException();

        List<Student> studentsTmp= new ArrayList<>(studentsList);
        studentsList.clear();
        player.entranceFiller(studentsTmp);
    }

    /**
     * Method to print infos about the clouds
     */
    public void cloudPrinter()
    {
        System.out.println("This tile is a Cloud with these students: ");
        this.tilePrinter();
    }
}
