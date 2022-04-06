package it.polimi.ingsw.Model;

public class StandardCard {
    private final int roundValue;
    private final int mnValue;

    public StandardCard(int rv, int mnv)
    {
        roundValue=rv;
        mnValue=mnv;
    }

    public int getRoundValue(){
        return roundValue;
    }

    public int getMnValue(){
        return mnValue;
    }
}
