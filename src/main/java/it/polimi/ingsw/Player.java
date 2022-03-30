package it.polimi.ingsw;

import it.polimi.ingsw.Dashboard.Dashboard;
import it.polimi.ingsw.Exceptions.*;
import java.util.List;

public class Player {
    private final String uID;
    private String nickName;
    private final Dashboard playerDashboard;
    private final Deck playerDeck;

    Player(String uID, Towers towers)
    {
        this.uID=uID;
        this.playerDashboard=new Dashboard(towers);
        this.playerDeck=new Deck();
    }
    public void setNickName(String nickName) { this.nickName=nickName; }

    public void entranceFiller(List<Student> students) throws FullEntranceException
    {
        playerDashboard.entranceFiller(students);
    }
    public Student entranceEmptier(Colour c) throws EmptyException, NoSuchStudentException
    {
        return playerDashboard.entranceEmptier(c);
    }
    public List<Colour> getStudents()
    {
        return playerDashboard.getStudents();
    }
    public void addStudent(Student student)
    {
        playerDashboard.addStudent(student);
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
}
