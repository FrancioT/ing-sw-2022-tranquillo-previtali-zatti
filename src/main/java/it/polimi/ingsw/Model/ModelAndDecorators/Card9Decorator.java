package it.polimi.ingsw.Model.ModelAndDecorators;

import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.EmptyException;
import it.polimi.ingsw.Model.Exceptions.FullTowersException;
import it.polimi.ingsw.Model.Exceptions.LinkFailedException;
import it.polimi.ingsw.Model.Exceptions.RunOutOfTowersException;
import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Teacher;

import java.util.HashMap;
import java.util.List;

public class Card9Decorator extends Model
{
    private final Colour nullified_colour;
    public Card9Decorator(Model model, Colour colour)
    {
        super(model);
        nullified_colour=colour;
    }
    @Override
    protected synchronized void islandDominance(Island island) throws FullTowersException,
                                                                      RunOutOfTowersException,
                                                                      EmptyException, LinkFailedException
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
                if(c!=nullified_colour)
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

                if (island.getNumTowers() != 0){
                    if(island.getTowersColour()==p.getTowers().getColour()) {
                        pPoints += island.getNumTowers();
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
