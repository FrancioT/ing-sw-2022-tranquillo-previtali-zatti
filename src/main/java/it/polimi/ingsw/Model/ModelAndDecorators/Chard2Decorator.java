package it.polimi.ingsw.Model.ModelAndDecorators;

import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.NoSuchPlayerException;
import it.polimi.ingsw.Model.Exceptions.NoSuchTeacherException;
import it.polimi.ingsw.Model.Exceptions.TeacherAlreadyInException;
import it.polimi.ingsw.Model.Exceptions.TooManyTeachersException;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Teacher;

public class Chard2Decorator extends Model
{
    public Chard2Decorator(Model model) { super(model); }

    public synchronized void moveTeachers(String uID) throws NoSuchPlayerException, NoSuchTeacherException,
                                                TooManyTeachersException, TeacherAlreadyInException
    {
        if(uID==null) throw new NullPointerException();

        Player player1 = null;
        for(Player p : playersList)
            if(p.getuID().equals(uID))
                player1 = p;
        if(player1==null)
            throw new NoSuchPlayerException();

        int numStud=0;
        Player player2;
        for(Teacher teacher: teachersList)
        {
            player2=teacher.getCurrentPos();
            if(player2!=null && player2!=player1) {
                numStud = player2.getStudentNum(teacher.getColour());
                if (numStud == player1.getStudentNum(teacher.getColour())) {
                    player1.addTeacher(player2.removeTeacher(teacher.getColour()));
                    teacher.setNewPos(player1);
                }
            }
        }
    }
    @Override
    public synchronized void teacherDominance(String uID, Colour colour) throws TooManyTeachersException,
                                                                   TeacherAlreadyInException,
                                                                   NoSuchTeacherException,
                                                                   NoSuchPlayerException
    {
        if(colour==null || uID==null) throw new NullPointerException();

        Player player1 = null;
        Player player2;
        int num1, num2;
        Teacher teacher = null;

        for(Player p : playersList)
            if(p.getuID().equals(uID))
                player1 = p;
        if(player1==null)
            throw new NoSuchPlayerException();

        if (!player1.checkTeacherPresence(colour)){
            for(Teacher t : teachersList)
                if(t.getColour() == colour)
                    teacher = t;
            player2 = teacher.getCurrentPos();
            if(player2==null) {
                player1.addTeacher(teacher);
                teacher.setNewPos(player1);
            }
            else {
                num1 = player1.getStudentNum(colour);
                num2 = player2.getStudentNum(colour);
                if (num1 >= num2) {
                    player1.addTeacher(player2.removeTeacher(colour));
                    teacher.setNewPos(player1);
                }
            }
        }
    }
}
