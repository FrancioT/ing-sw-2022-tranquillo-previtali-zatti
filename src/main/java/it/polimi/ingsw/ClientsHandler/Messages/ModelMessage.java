package it.polimi.ingsw.ClientsHandler.Messages;

import it.polimi.ingsw.Model.CharacterCard.CharacterCard;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ModelMessage implements Serializable
{
    private final List<Island> islandList;
    private final List<Cloud> cloudList;
    private final List<Player> playerList;
    private final List<CharacterCard> characterCardList;
    private final int unusedCoins;
    static final long serialVersionUID= 90000L;

    public ModelMessage(List<Island> islandList, List<Cloud> cloudList, List<Player> playerList,
                        List<CharacterCard> characterCardList, int unusedCoins)
    {
        this.islandList= new ArrayList<>(islandList);
        this.cloudList= new ArrayList<>(cloudList);
        this.playerList= new ArrayList<>(playerList);
        this.characterCardList= new ArrayList<>(characterCardList);
        this.unusedCoins=unusedCoins;
    }
    public List<Island> getIslandList() { return new ArrayList<>(islandList); }
    public List<Cloud> getCloudList() { return new ArrayList<>(cloudList); }
    public List<Player> getPlayerList() { return new ArrayList<>(playerList); }
    public List<CharacterCard> getCharacterCardList() { return new ArrayList<>(characterCardList); }
    public int getUnusedCoins() { return unusedCoins; }
}
