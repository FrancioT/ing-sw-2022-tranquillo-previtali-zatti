package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;

abstract public class CharacterCard {
    protected final int price;
    protected int overPrice;
    protected final int cardID;

    public CharacterCard(int cardID,int price)
    {
        this.cardID=cardID;
        this.price=price;
        this.overPrice=0;
    }
    public int getPrice() {
        return price+overPrice;
    }
    public int getCardID() { return cardID; }
    abstract public void handle(String uID, DataBuffer userData, Controller controller) throws Exception;
}
