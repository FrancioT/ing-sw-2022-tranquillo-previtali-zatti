package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.Exceptions.RunOutOfStudentsException;


public class CharacterCard11 extends CharacterCardWithStudentsList
{
    transient final private Bag bag;
    static final long serialVersionUID= 80313L;

    /**
     * Constructor of the card
     * @param bag the bag from which the card will take students to refill itself
     * @throws RunOutOfStudentsException Exception thrown if the students in the bag are finished
     */
    public CharacterCard11(Bag bag) throws RunOutOfStudentsException
    {
        super(11, 2);
        this.bag = bag;
        this.effect="Take 1 Student from this card and place it in your classroom. \n" +
                    "Then, draw a new Student from the Bag and place it on this card.";
        for (int i = 0; i < 4; i++)
            studentsList.add(bag.randomExtraction());
    }

    /**
     * @param userData   the databuffer with the colour of the student that the player wants to move in his classroom
     */
    @Override
    public void handle(String uID, DataBuffer userData, Controller controller) throws Exception{
        if(userData==null || uID==null || controller==null)
            throw new NullPointerException();
        if(!controller.getModel().checkEnoughMoney(uID, cardID))
            throw new NotEnoughMoneyException();

        Colour colour= userData.getStudentColour();
        controller.getModel().addStudentDashboard(uID, removeStudent(colour));

        controller.getModel().payCard(uID, cardID);
        overPrice=true;
        studentsList.add(bag.randomExtraction());
    }

    @Override
    public void ccPrinter()
    {
        super.ccPrinter();
        System.out.println("Effect: "+effect);
        System.out.println("Parameters: Student's colour");
    }
}
