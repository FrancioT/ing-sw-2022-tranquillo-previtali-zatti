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

    /**
     * Constructor of the card
     * @param cardID the ID of the card
     * @param price the price of the card
     */
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

    /**
     * The card's method which activates the effect of it
     * @param uID the player that wants to activate the card
     * @param userData the databuffer with the infos necessary for the activation of the card
     * @param controller the controller where the card will activate the effect
     * @throws Exception
     */
    abstract public void handle(String uID, DataBuffer userData, Controller controller) throws Exception;

    /**
     * the method to print the information of a card
     */
    public void ccPrinter()
    {
        System.out.println("Card ID: "+this.cardID+"     cost: "+price+"     overprice: "+(overPrice?1:0));
    }
}
