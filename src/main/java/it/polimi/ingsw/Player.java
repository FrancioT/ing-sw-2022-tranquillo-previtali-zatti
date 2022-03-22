package it.polimi.ingsw;

import it.polimi.ingsw.Dashboard.Dashboard;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String uID;
    private String nickName;
    private final Dashboard playerDashboard;
    private final Deck playerDeck;
    private static List<Player> players;
    private static boolean createdFlag=false;

    private Player(String uID, Dashboard dashboard, Deck deck)
    {
        this.uID=uID;
        this.playerDashboard=dashboard;
        this.playerDeck=deck;
    }

    // the model must pass the list of Towers in the same order of the uIDs, in there are 4 players
    // the list must be like [towers1, towers1, towers2, towers2] because the towers are shared
    public static List<Player> playersCreator(int playerNum, List<String> uIDs, List<Towers> towers)
    {
        if(createdFlag==false)
        {
            players=new ArrayList<>();
            for(int i=0;i<playerNum;i++)
            {
                players.add(new Player(uIDs.get(i), new Dashboard(towers.get(i)), new Deck()));
            }
            createdFlag=true;
        }
        return players;
    }
    public void setNickName(String nickName) { this.nickName=nickName; }
}
