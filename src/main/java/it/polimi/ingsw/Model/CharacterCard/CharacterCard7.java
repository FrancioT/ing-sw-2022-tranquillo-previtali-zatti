package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.NoSuchStudentException;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.Exceptions.RunOutOfStudentsException;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;
import it.polimi.ingsw.Model.Student;

import java.util.ArrayList;
import java.util.List;

public class CharacterCard7 extends CharacterCardWithStudentsList {
    static final long serialVersionUID = 80312L;

    /**
     * Constructor of the card
     * @param bag the bag from which the card will take students to refill itself
     * @throws RunOutOfStudentsException Exception thrown if the students in the bag are finished
     */
    public CharacterCard7(Bag bag) throws RunOutOfStudentsException
    {
        super(7, 1);
        for (int i = 0; i < 6; i++) {
            studentsList.add(bag.randomExtraction());
        }
    }

    /**
     * @param userData   the databuffer with the colours of the students that the player wants to swap.
     *                   The order of the students must be: the first up to 3 colours are the ones that must
     *                   be moved from the entrance to the card, the last up to 3 colours are moved to the
     *                   entrance from the card
     */
    @Override
    public void handle(String uID, DataBuffer userData, Controller controller) throws Exception {
        if (uID == null || userData == null || controller == null)
            throw new NullPointerException();
        Model model = controller.getModel();
        if (!model.checkEnoughMoney(uID, cardID))
            throw new NotEnoughMoneyException();

        boolean ok = false;
        // The order of studentToSwap must be: the first up to 3 colours are the ones that must be moved from
        // the entrance to the card, the last up to 3 colours are moved to the entrance from the card
        List<Colour> studentsToSwap = userData.getStudentsColours();
        if (studentsToSwap.size() % 2 != 0 || studentsToSwap.size() > 6)
            throw new IllegalArgumentException();

        int studentsToMove = studentsToSwap.size() / 2;
        List<Colour> studentsToCard = new ArrayList<>();
        List<Colour> studentsToEntrance = new ArrayList<>();

        for (int i = 0; i < studentsToMove; i++) {
            studentsToCard.add(studentsToSwap.remove(0));
        }

        for (int i = 0; i < studentsToMove; i++) {
            studentsToEntrance.add(studentsToSwap.remove(0));
        }

        // in this first part we check if the requested students are available

        for (Colour c : studentsToCard) {
            int check = 0;
            for(Colour colour1: studentsToCard)
                if(c == colour1)
                    check++;
            for(Colour colour : model.getStudents(uID))
                if(c == colour)
                    check--;
            if(check > 0)
                throw new NoSuchStudentException();
        }

        for(Colour c : studentsToEntrance){
            int check = 0;
            for(Colour colour1: studentsToCard)
                if(c == colour1)
                    check++;
            for(Colour colour : getColoursOnCard())
                if(c == colour)
                    check--;
            if(check > 0)
                throw new NoSuchStudentException();
        }

        //in this second part we make the effective swap

        for(int i = 0; i < studentsToMove; i++){
            studentsList.add(model.entranceEmptier(uID, studentsToCard.remove(0)));
        }

        for(int i = 0; i < studentsToMove; i++){
            List<Student> studentToAdd = new ArrayList<>();
            studentToAdd.add(removeStudent(studentsToEntrance.remove(0)));
            model.entranceFiller(uID, studentToAdd);
        }

        model.payCard(uID, cardID);
        overPrice=true;
    }

    @Override
    public void ccPrinter()
    {
        super.ccPrinter();
        System.out.println("Effect: You may take up to 3 Students from this card " +
                "and replace them with the same number of Students " +
                "from your Entrance.");
        System.out.println("Parameters: list of colours (order must be: the first up to 3 colours are the ones \n" +
                "that must be moved from the entrance to the card, \n" +
                "the last up to 3 colours are moved to the entrance from the card)");
    }
}
