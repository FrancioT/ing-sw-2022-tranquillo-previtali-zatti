package it.polimi.ingsw;

public class CharacterCard {
    protected final int price;
    protected int overPrice;
    protected final int cardID;

    public int getPrice() {
        return price+overPrice;
    }

}
