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
    private final String currPlayerNickname;
    private final int unusedCoins;
    private final boolean gameEndedFlag;
    private final boolean expertModeFlag;
    static final long serialVersionUID= 90000L;

    public ModelMessage(boolean expertModeFlag, List<Island> islandList, List<Cloud> cloudList, List<Player> playerList,
                        List<CharacterCard> characterCardList, String currPlayerNickname, int unusedCoins,
                        boolean gameEndedFlag)
    {
        this.expertModeFlag=expertModeFlag;
        if(islandList==null)
            islandList= new ArrayList<>();
        this.islandList= islandList;
        if(cloudList==null)
            cloudList= new ArrayList<>();
        this.cloudList= cloudList;
        if(playerList==null)
            playerList= new ArrayList<>();
        this.playerList= playerList;
        if(characterCardList==null)
            characterCardList= new ArrayList<>();
        this.currPlayerNickname=currPlayerNickname;
        this.characterCardList= characterCardList;
        this.unusedCoins= unusedCoins;
        this.gameEndedFlag= gameEndedFlag;
    }
    public List<Island> getIslandList() { return new ArrayList<>(islandList); }
    public List<Cloud> getCloudList() { return new ArrayList<>(cloudList); }
    public List<Player> getPlayerList() { return new ArrayList<>(playerList); }
    public List<CharacterCard> getCharacterCardList() { return new ArrayList<>(characterCardList); }
    public String getCurrPlayerNickname() { return currPlayerNickname; }
    public int getUnusedCoins() { return unusedCoins; }
    public boolean isExpertMode() { return expertModeFlag; }
    public boolean hasGameEnded() { return gameEndedFlag; }
}
