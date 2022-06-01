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
import java.util.Map;

public class Card3Actuator
{
    /**
     * This method calculate the dominance on the island requested by the player even if there isn't mother nature on it
     * @param index the index of the island where we have to calculate the dominance
     * @param model the model where we have to look for the island
     * @throws FullTowersException Exception thrown if the maximum number of towers is surpassed
     * @throws RunOutOfTowersException Exception thrown when a player finishes his available towers
     * @throws EmptyException Exception thrown if there are no towers on the island
     * @throws LinkFailedException Exception thrown if a linking has failed
     */
    static public void card3Effect(int index, Model model) throws FullTowersException,
                                                                  RunOutOfTowersException,
                                                                  EmptyException, LinkFailedException
    {
        if(model==null) throw new NullPointerException();
        if(index<0) throw new IndexOutOfBoundsException();

        Island island= model.islandsList.get(index);
        List<Colour> islandColoursList=island.getStudentsColours();
        Map<Colour, Integer> coloursMap=new HashMap<>();
        Player dominantPlayer=null;
        boolean drawFlag=false;
        int pPoints=0, maxPPoints=0;

        if(!island.getInhibition())
        {
            for(Colour c: Colour.values())
                coloursMap.put(c, Integer.valueOf(0));

            for(Colour c: islandColoursList)
            {
                //here "coloursMap.get(c)" is equivalent to
                //"Integer.valueOf(coloursMap.get(c).intValue())"
                coloursMap.replace(c, coloursMap.get(c)+1);
            }

            for(Player p : model.playersList)
            {
                for(Teacher t : model.teachersList)
                    if(p.checkTeacherPresence(t.getColour()))
                        pPoints += coloursMap.get(t.getColour());

                if (island.getNumTowers() != 0)
                    if(island.getTowersColour()==p.getTowers().getColour())
                        pPoints += island.getNumTowers();

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
            island.subInhibition();
            model.giveBackInhibitionFlag();
        }
    }
}
