package it.polimi.ingsw.Model;

import java.io.Serializable;

public class StandardCard implements Serializable
{
    private final int roundValue;
    private final int mnValue;
    static final long serialVersionUID= 80221L;

    /**
     * Constructor of standard card
     * @param rv the Round Value
     * @param mnv the MotherNature Value
     */
    public StandardCard(int rv, int mnv)
    {
        roundValue=rv;
        mnValue=mnv;
    }

    /**
     * @return the RoundValue of the card
     */
    public int getRoundValue(){
        return roundValue;
    }

    /**
     * @return the MotherNatureValue of the card
     */
    public int getMnValue(){
        return mnValue;
    }
}
