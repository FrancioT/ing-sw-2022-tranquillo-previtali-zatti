package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.Exceptions.RunOutOfStudentsException;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;

public class CharacterCard1 extends CharacterCardWithStudentsList{
    transient final private Bag bag;
    static final long serialVersionUID= 80311L;

    /**
     * Constructor of the card
     * @param bag the bag from which the card will take students to refill itself
     */
    public CharacterCard1(Bag bag)
    {
        super(1, 1);
        this.effect="Take 1 student from this card and place it on an island of your choice.";
        this.bag = bag;
    }

    /**
     * Extract 4 students from the bag and add them to the card
     */
    @Override
    public void initialize()
    {
        for (int i = 0; i < 4; i++){
            try{
                studentsList.add(bag.randomExtraction());
            }
            catch(RunOutOfStudentsException e){
                throw new RuntimeException("There were not enough students in the bag for the creation of the game");
            }
        }
    }


    /**
     * @param userData   the databuffer containing the colour of the students that the player wants to move
     */
    @Override
    public void handle(String uID, DataBuffer userData, Controller controller) throws Exception {
        if(userData==null || uID==null || controller==null)
            throw new NullPointerException();
        Model model = controller.getModel();
        if(!model.checkEnoughMoney(uID, cardID))
            throw new NotEnoughMoneyException();

        int index= userData.getIslandPos();
        Colour colour= userData.getStudentColour();
        model.addStudentIsland(index, removeStudent(colour));

        model.payCard(uID, cardID);
        overPrice=true;
        studentsList.add(bag.randomExtraction());
    }

    @Override
    public void ccPrinter()
    {
        super.ccPrinter();
        System.out.println("Effect: "+effect);
        System.out.println("Parameters: Student's colour, Island's position");
    }
}
