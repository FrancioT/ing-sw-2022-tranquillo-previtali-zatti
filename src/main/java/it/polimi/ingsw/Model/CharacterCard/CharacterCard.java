package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;

import java.io.Serializable;

abstract public class CharacterCard implements Serializable
{
    protected final int price;
    protected boolean overPrice;
    protected final int cardID;
    static final long serialVersionUID= 80300L;

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

    public void ccPrinter()
    {
        System.out.println("This is the " + this.cardID + "°" + "character card and its effect costs: ");

        if (overPrice)
        {
            System.out.println((this.price + 1) + "\n");
        }
        else
        {
            System.out.println(this.price + "\n");
        }
    }
}
