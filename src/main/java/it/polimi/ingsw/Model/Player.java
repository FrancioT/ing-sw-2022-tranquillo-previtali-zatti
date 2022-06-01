package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Dashboard.Dashboard;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;

import java.io.Serializable;
import java.util.List;

public class Player implements Serializable
{
    private final String uID;
    //private String nickName;
    private final Dashboard playerDashboard;
    private final Deck playerDeck;
    private int coins;
    transient private final Model model;
    static final long serialVersionUID= 80200L;

    /**
     * Constructor of the players
     * @param uID user id of the player, which is the nickname they choose at the start
     * @param towers the towers associated tp the player
     * @param model the model on which they are playing
     */
    public Player(String uID, Towers towers, Model model)
    {
        this.uID=uID;
        this.playerDashboard=new Dashboard(towers);
        this.playerDeck=new Deck();
        this.coins=1;
        this.model=model;
    }

    /**
     * Method used to fill a player's entrance with a list of students
     * @param students the list of students to add
     * @throws FullEntranceException Exception thrown if the entrance is full
     */
    //public void setNickName(String nickName) { this.nickName=nickName; }
    public void entranceFiller(List<Student> students) throws FullEntranceException
    {
        playerDashboard.entranceFiller(students);
    }

    /**
     * Method to remove from a player's entrance a student of the color selected
     * @param colour color of the student we have to remove from the entrance
     * @return student removed from entrance
     * @throws EmptyException Exception thrown if the entrance is empty
     * @throws NoSuchStudentException Exception thrown if there are no students in the entrance of the requested color
     */
    public Student entranceEmptier(Colour colour) throws EmptyException, NoSuchStudentException
    {
        return playerDashboard.entranceEmptier(colour);
    }

    /**
     * @return list of students' color that are currently in the entrance
     */
    public List<Colour> getStudents()
    {
        return playerDashboard.getStudents();
    }

    /**
     * Method to add a student to a classroom
     * @param student the student to add
     * @throws FullClassException Exception thrown if the classroom is full
     */
    public void addStudent(Student student) throws FullClassException
    {
        playerDashboard.addStudent(student);
        if(getStudentNum(student.getColour())%3==0)
        {
            try {
                model.getCoin();
                coins++;
            } catch (NoMoreCoinsException ignored) {}
        }
    }

    /**
     * Method to add a teacher to a player's classroom
     * @param teacher the teacher to add
     * @throws TooManyTeachersException Exception thrown if all the teachers are already in the player's dashboard
     * @throws TeacherAlreadyInException Exception thrown if the requested teacher is already in the player's classroom
     */
    public void addTeacher(Teacher teacher) throws TooManyTeachersException, TeacherAlreadyInException
    {
        playerDashboard.addTeacher(teacher);
    }

    /**
     * Method to remove a teacher of a selected color from a player's classroom
     * @param colour the colour of the teacher we have to remove
     * @return the removed teacher
     * @throws NoSuchTeacherException Exception thrown if the requested teacher is not in the player's classroom
     */
    public Teacher removeTeacher(Colour colour) throws NoSuchTeacherException
    {
        return playerDashboard.removeTeacher(colour);
    }

    /**
     * @param colour the color of the students whose number we want to know
     * @return how many students there are in the player's classroom
     */
    public int getStudentNum(Colour colour)
    {
        return playerDashboard.getStudentNum(colour);
    }

    /**
     * @param colour the color of the teacher whose presence we want to know
     * @return true if the teacher is in the player's classroom, false if not
     */
    public boolean checkTeacherPresence(Colour colour)
    {
        return playerDashboard.checkTeacherPresence(colour);
    }

    /**
     * @param pos position of the card that the player wants to discard
     * @return the standard card discarded
     */
    public StandardCard cardDiscarder(int pos) { return playerDeck.cardDiscarder(pos); }

    /**
     * @return the player's uID
     */
    public String getuID(){ return uID; }

    /**
     * @return the player's towers
     */
    public Towers getTowers()
    {
        return playerDashboard.getDTowers();
    }

    /**
     * @return the last card discarded MN value
     * @throws EmptyException Exception thrown if there isn't any discarded card
     */
    public int getLastCardMNValue() throws EmptyException { return playerDeck.getLastCardMNValue(); }

    /**
     * Method used to pay a card; it decreases the cost of the card to the player's coins
     * @param cost how many coins the card's activation cost
     * @throws CardPaymentException Exception thrown if the player doesn't have enough coins to activate the card
     */
    public void pay(int cost) throws CardPaymentException
    {
        if(cost>coins) throw new CardPaymentException();
        coins -= cost;
    }

    /**
     * Method to swap a pair of students between the entrance and the classroom of a player
     * @param entranceStudentColour student to remove from entrance
     * @param classroomStudentColour student to remove from classroom
     * @throws NoSuchStudentException Exception thrown if one of the requested students is not available
     * @throws EmptyException Exception thrown if the entrance is empty
     * @throws FullEntranceException Exception thrown if the entrance is full
     * @throws FullClassException Exception thrown if the classroom is full
     */
    public void studentsSwap(Colour entranceStudentColour, Colour classroomStudentColour)
                                            throws NoSuchStudentException, EmptyException,
                                                   FullEntranceException, FullClassException
    {
        playerDashboard.studentsSwap(entranceStudentColour, classroomStudentColour);
    }

    /**
     * @return the number of coins that the player has
     */
    public int getCoins(){return coins;}

    /**
     * Method to remove a student from a player's classroom
     * @param colour the colour of the student that has to be removed
     * @return the removed student
     * @throws NoSuchStudentException Exception thrown if there are no students of the requested color in the classroom
     */
    public Student removeStudentClassroom(Colour colour) throws NoSuchStudentException {
        return playerDashboard.removeStudentClassroom(colour);
    }

    /**
     * @return the list of the cards in a player's hand
     */
    public List<StandardCard> getHandCards() { return playerDeck.getHandCards(); }

    /**
     * Method to print player's infos
     * @param expertModeFlag true if expert mode, false if standard mode
     */
    public void playerPrinter(boolean expertModeFlag){

        final String coinsYellow = "\u001B[33m";
        final String space = " ";

        final String cRESET = "\u001B[0m";

        System.out.print(this.uID);

        if(expertModeFlag)
            System.out.print(" now you have " + coinsYellow + this.coins + " coins" + cRESET);

        System.out.println();

        System.out.println("On your entrance you have ");

        this.playerDashboard.dashboardPrinter();

        this.playerDeck.deckPrinter();

    }
}
