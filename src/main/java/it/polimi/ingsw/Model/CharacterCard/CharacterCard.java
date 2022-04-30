package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;

abstract public class CharacterCard {
    protected final int price;
    protected boolean overPrice;
    protected final int cardID;

    public CharacterCard(int cardID,int price)
    {
        this.cardID=cardID;
        this.price=price;
        this.overPrice=false;
    }
    public int getPrice() {
        return price+(overPrice?1:0);
    }
    public int getCardID() { return cardID; }
    public boolean getOverprice() { return overPrice; }
    abstract public void handle(String uID, DataBuffer userData, Controller controller) throws Exception;
}
