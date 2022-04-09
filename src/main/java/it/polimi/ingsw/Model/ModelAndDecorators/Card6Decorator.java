package it.polimi.ingsw.Model.ModelAndDecorators;

import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.EmptyException;
import it.polimi.ingsw.Model.Exceptions.FullTowersException;
import it.polimi.ingsw.Model.Exceptions.RunOutOfTowersException;
import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Teacher;

import java.util.HashMap;
import java.util.List;

public class Card6Decorator extends Model
{
    public Card6Decorator(Model model) { super(model); }

    @Override
    protected synchronized void islandDominance(Island island) throws FullTowersException,
                                                                    RunOutOfTowersException
    {
        List<Colour> islandColoursList=island.getStudentsColours();
        HashMap<Colour, Integer> coloursMap=new HashMap<>();
        Player dominantPlayer=null;
        boolean drawFlag=false;
        int pPoints=0, maxPPoints=0;

        if(island.equals(motherNature.getCurrentPos()) && !island.getInhibitionFlag())
        {
            for(Colour c: Colour.values())
            {
                coloursMap.put(c, Integer.valueOf(0));
            }

            for(Colour c: islandColoursList)
            {
                //here "coloursMap.get(c)" is equivalent to
                //"Integer.valueOf(coloursMap.get(c).intValue())"
                coloursMap.replace(c, coloursMap.get(c)+1);
            }

            for(Player p : playersList)
            {
                for(Teacher t : teachersList)
                {
                    if(p.checkTeacherPresence(t.getColour()))
                    {
                        pPoints += coloursMap.get(t.getColour());
                    }
                }

                if(pPoints==maxPPoints)
                    drawFlag=true;

                if(pPoints>maxPPoints)
                {
                    maxPPoints=pPoints;
                    dominantPlayer=p;
                    drawFlag=false;
                }

                pPoints=0;
            }

            if(drawFlag==false)
                island.towersSwitcher((dominantPlayer.getTowers()));
        }
        else
        {
            if(island.getInhibitionFlag())
            {
                island.setInhibitionFlag(false);
                card5.giveBackInhibitionFlag();
            }
            else
                throw new IllegalArgumentException();  // the passed Island doesn't have the
            // mother nature on it
        }
    }
}
