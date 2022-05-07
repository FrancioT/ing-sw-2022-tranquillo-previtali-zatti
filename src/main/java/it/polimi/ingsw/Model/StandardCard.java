package it.polimi.ingsw.Model;

import java.io.Serializable;

public class StandardCard implements Serializable
{
    private final int roundValue;
    private final int mnValue;
    static final long serialVersionUID= 80221L;

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
