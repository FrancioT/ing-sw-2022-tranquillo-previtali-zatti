package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Exceptions.FullEntranceException;
import it.polimi.ingsw.Model.Exceptions.NoSuchStudentException;

import java.util.ArrayList;
import java.util.List;

public class Cloud extends Tile{
    transient final private Bag bag;
    static final long serialVersionUID= 80001L;

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

    public void cloudPrinter() throws NoSuchStudentException
    {
        final String sBlue = "\u001B[34m";
        final String sRed = "\u001B[31m";
        final String sGreen = "\u001B[32m";
        final String sYellow = "\u001B[33m";
        final String sPink = "\u001B[35m";
        final String space = " ";

        int bSt=0,rSt=0,gSt=0,ySt=0,pSt=0;

        for(Colour c: this.getStudentsColours())
        {
            switch (c) {
                case blue:
                    bSt++;
                    break;
                case red:
                    rSt++;
                    break;
                case green:
                    gSt++;
                    break;
                case yellow:
                    ySt++;
                    break;
                case pink:
                    pSt++;
                    break;
                default:
                    throw new NoSuchStudentException();
            }
        }

        System.out.println("This tile is a Cloud with these students:" + sBlue + bSt + space + sRed + rSt + space + sGreen + gSt + space
                                                                       + sYellow + ySt + space + sPink + pSt);
    }
}
