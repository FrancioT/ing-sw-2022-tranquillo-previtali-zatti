package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Dashboard.Dashboard;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.ModelAndDecorators.Model;

import java.io.Serializable;
import java.util.List;

public class Player implements Serializable
{
    private final String uID;
    private String nickName;
    private final Dashboard playerDashboard;
    private final Deck playerDeck;
    private int coins;
    transient private final Model model;
    static final long serialVersionUID= 80200L;

    public Player(String uID, Towers towers, Model model)
    {
        this.uID=uID;
        this.playerDashboard=new Dashboard(towers);
        this.playerDeck=new Deck();
        this.coins=1;
        this.model=model;
    }

    public void setNickName(String nickName) { this.nickName=nickName; }
    public void entranceFiller(List<Student> students) throws FullEntranceException
    {
        playerDashboard.entranceFiller(students);
    }
    public Student entranceEmptier(Colour colour) throws EmptyException, NoSuchStudentException
    {
        return playerDashboard.entranceEmptier(colour);
    }
    public List<Colour> getStudents()
    {
        return playerDashboard.getStudents();
    }
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
    public void addTeacher(Teacher teacher) throws TooManyTeachersException, TeacherAlreadyInException
    {
        playerDashboard.addTeacher(teacher);
    }
    public Teacher removeTeacher(Colour colour) throws NoSuchTeacherException
    {
        return playerDashboard.removeTeacher(colour);
    }
    public int getStudentNum(Colour colour)
    {
        return playerDashboard.getStudentNum(colour);
    }
    public boolean checkTeacherPresence(Colour colour)
    {
        return playerDashboard.checkTeacherPresence(colour);
    }
    public StandardCard cardDiscarder(int pos) { return playerDeck.cardDiscarder(pos); }

    public String getuID(){return uID;}
    public Towers getTowers()
    {
        return playerDashboard.getDTowers();
    }
    public int getLastCardMNValue() throws EmptyException { return playerDeck.getLastCardMNValue(); }
    public void pay(int cost) throws CardPaymentException
    {
        if(cost>coins) throw new CardPaymentException();
        coins -= cost;
    }
    public void studentsSwap(Colour entranceStudentColour, Colour classroomStudentColour)
                                            throws NoSuchStudentException, EmptyException,
                                                   FullEntranceException, FullClassException
    {
        playerDashboard.studentsSwap(entranceStudentColour, classroomStudentColour);
    }

    public int getCoins(){return coins;}

    public Student removeStudentClassroom(Colour colour) throws NoSuchStudentException {
        return playerDashboard.removeStudentClassroom(colour);
    }
    public List<StandardCard> getHandCards() { return playerDeck.getHandCards(); }

    public void playerPrinter()
    {
        final String coinsYellow = "\u001B[33m";
        final String space = " ";

        final String cRESET = "\u001B[0m";

        System.out.println("You are " + this.uID + " and in game you are called " + this.nickName + ", now you have " + coinsYellow + this.coins + cRESET + "\n");

        System.out.println("On your entrance you have ");

        this.playerDashboard.dashboardPrinter();

    }
}
