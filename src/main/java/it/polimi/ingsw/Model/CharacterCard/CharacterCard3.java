package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Model.Exceptions.*;

public class CharacterCard3 extends CharacterCard {

    public CharacterCard3(int cardID, int price) {
        super(3, 3);
    }

    @Override
    public void handle(String uID, Object choice, Controller controller) throws NoSuchPlayerException, NoSuchCardException, cardPaymentException, EmptyException, FullTowersException, RunOutOfTowersException, LinkFailedException {
        int currentIslandIndex, nextIslandIndex, delta;

        controller.getModel().payCard(uID, cardID);
        overPrice++;
        currentIslandIndex=controller.getModel().getCurrPosMN();
        nextIslandIndex=(int) choice;

        if(nextIslandIndex>currentIslandIndex)
        {
            delta=nextIslandIndex-currentIslandIndex;
        }
        else
        {
            delta=(12-currentIslandIndex)+nextIslandIndex;
        }

        controller.getModel().moveMN(delta);

        if(nextIslandIndex>currentIslandIndex)
        {
            delta=(12-nextIslandIndex)+currentIslandIndex;
        }
        else
        {
            delta=currentIslandIndex-nextIslandIndex;
        }

        controller.getModel().moveMN(delta);
        //devo ricordarmi di risolvere il problema
        // correlato all'InhibitionFlag
        // con il ricalcolo della dominanza dopo che MN
        // è tornata sulla vecchia isola
    }

}
