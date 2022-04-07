package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;

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
    abstract public void handle(String uID, Object choice, Controller controller);
}
