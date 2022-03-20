package it.polimi.ingsw;

public class CharacterCard {
    protected int price;
    protected int overPrice;
    protected int cardID;

    public int getPrice() {
        return price+overPrice;
    }

}
