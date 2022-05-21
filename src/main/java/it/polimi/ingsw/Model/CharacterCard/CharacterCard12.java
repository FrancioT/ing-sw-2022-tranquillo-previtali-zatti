package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.NoSuchStudentException;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Student;

import java.util.ArrayList;
import java.util.List;

public class CharacterCard12 extends CharacterCard
{
    transient final private Bag bag;
    transient final private List<Player> players;
    static final long serialVersionUID= 80309L;

    public CharacterCard12(Bag bag, List<Player> players){
        super(12 ,3);
        this.bag = bag;
        this.players=new ArrayList<>(players);
    }

    @Override
    public void handle(String uID, DataBuffer userData, Controller controller) throws Exception {
        if(userData==null || uID==null || controller==null)
            throw new NullPointerException();
        Model model = controller.getModel();
        if(!model.checkEnoughMoney(uID, cardID))
            throw new NotEnoughMoneyException();

        Colour colour=userData.getStudentColour();
        synchronized (model)
        {
            for (Player p : players)
            {
                for (int i = 0; i < 3; i++)
                {
                    try {
                        Student student = p.removeStudentClassroom(colour);
                        bag.addStudentBag(student);
                    } catch (NoSuchStudentException e) { break; }
                }
            }
        }

        model.payCard(uID, cardID);
        overPrice=true;
    }

    @Override
    public void ccPrinter()
    {
        super.ccPrinter();
        System.out.println("Effect: Choose a type of Student: every player (including yourself) must return 3 Students \n" +
                "of that type from their Dining Room to the bag. If any player has fewer than 3 Students of that type, \n" +
                "return as many Students as they have.");
        System.out.println("Parameters: Student's colour");
    }
}
