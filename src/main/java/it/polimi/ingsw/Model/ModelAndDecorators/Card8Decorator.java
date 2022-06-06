package it.polimi.ingsw.Model.ModelAndDecorators;

import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Teacher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Card8Decorator extends Model
{
    private final Player player;
    /**
     * Constructor of the model decorator
     * @param model the model on which the decorator is based
     * @param uID the player who has activated the card
     */
    public Card8Decorator(Model model, String uID) throws NoSuchPlayerException
    {
        super(model);
        if(uID==null) throw new NullPointerException();

        Player tmp = null;
        for (Player p : playersList)
            if (p.getuID().equals(uID))
                tmp = p;
        if(tmp==null)
            throw new NoSuchPlayerException();
        player=tmp;
    }

    /**
     * This method calculate the dominance of an island normally, then it adds 2 extra dominance points to the player
     * who has activated the card
     * @throws FullTowersException thrown by the method towersSwitcher of island
     * @throws RunOutOfTowersException thrown by the method towersSwitcher of island
     * @throws EmptyException thrown if the island was modified during the execution of this method and was
     * emptied from every tower
     * @throws LinkFailedException thrown by the method towersSwitcher of island
     */
    @Override
    protected synchronized void islandDominance(Island island) throws FullTowersException,
                                                                      RunOutOfTowersException,
                                                                      EmptyException, LinkFailedException
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

                if(p==this.player)
                    pPoints+=2;

                for(Teacher t : teachersList)
                    if(p.checkTeacherPresence(t.getColour()) || teamMate.checkTeacherPresence(t.getColour()))
                        pPoints += coloursMap.get(t.getColour());

                if (island.getNumTowers() != 0)
                    if(island.getTowersColour()==p.getTowers().getColour())
                        pPoints += island.getNumTowers();

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
                island.towersSwitcher((dominantPlayer.getTowers()));
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
