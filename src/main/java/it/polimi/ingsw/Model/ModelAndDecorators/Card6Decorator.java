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

public class Card6Decorator extends Model
{
    /**
     * Constructor of the model decorator
     * @param model the model on which the decorator is based
     */
    public Card6Decorator(Model model) { super(model); }

    /**
     * This method calculates the dominance over an island, but without considering the towers on it
     * @throws FullTowersException thrown by the method towersSwitcher of island
     * @throws RunOutOfTowersException thrown by the method towersSwitcher of island
     * @throws LinkFailedException thrown by the method towersSwitcher of island
     */
    @Override
    protected synchronized void islandDominance(Island island) throws FullTowersException,
                                                                    RunOutOfTowersException,
                                                                    LinkFailedException
    {
        List<Colour> islandColoursList=island.getStudentsColours();
        Map<Colour, Integer> coloursMap=new HashMap<>();
        Player dominantPlayer=playersList.get(0);
        boolean drawFlag=true;
        int pPoints=0, maxPPoints=0;

        if(island==motherNature.getCurrentPos() && !island.getInhibition())
        {
            for(Colour c: Colour.values())
                coloursMap.put(c, Integer.valueOf(0));

            for(Colour c: islandColoursList)
                coloursMap.replace(c, coloursMap.get(c)+1);

            for(Player p : playersList)
            {
                Player teamMate= p;
                // if it's the 4 players mode, look for the team-mate, else the team-mate is the player itself
                if(playersList.size()==4)
                    for(Player mate: playersList)
                        if(mate!=p && mate.getTowers().getColour()==p.getTowers().getColour())
                        {
                            teamMate=mate;
                            break;
                        }

                for(Teacher t : teachersList)
                    if(p.checkTeacherPresence(t.getColour()) || teamMate.checkTeacherPresence(t.getColour()))
                        pPoints += coloursMap.get(t.getColour());

                if(pPoints==maxPPoints && p.getTowers().getColour()!=dominantPlayer.getTowers().getColour())
                    drawFlag=true;

                if(pPoints>maxPPoints)
                {
                    maxPPoints=pPoints;
                    dominantPlayer=p;
                    drawFlag=false;
                }

                pPoints=0;
            }

            if(!drawFlag)
            {
                try{
                    island.towersSwitcher((dominantPlayer.getTowers()));
                    checkIslandLinking();
                }catch(RunOutOfTowersException e){
                    checkIslandLinking();
                    throw e;
                }
            }
        }
        else
        {
            if(island.getInhibition())
            {
                island.subInhibition();
                card5.giveBackInhibitionFlag();
            }
            else
                throw new IllegalArgumentException();  // the passed Island doesn't have the
            // mother nature on it
        }
    }
}
