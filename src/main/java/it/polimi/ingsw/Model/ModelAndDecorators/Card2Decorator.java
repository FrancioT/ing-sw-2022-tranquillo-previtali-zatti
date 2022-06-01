package it.polimi.ingsw.Model.ModelAndDecorators;

import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.NoSuchPlayerException;
import it.polimi.ingsw.Model.Exceptions.NoSuchTeacherException;
import it.polimi.ingsw.Model.Exceptions.TeacherAlreadyInException;
import it.polimi.ingsw.Model.Exceptions.TooManyTeachersException;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Teacher;

public class Card2Decorator extends Model
{
    /**
     * Constructor for decorator
     * @param model the model on which is based the decorator
     */
    public Card2Decorator(Model model) { super(model); }

    /**
     * This method checks the position of all the teachers. If there is an opponent who has a teacher and has the
     * same number of students as the player who has activated the card, this method assigns the property of the
     * teacher to this last player.
     * @param uID the uid of the player who has activated the card
     * @throws NoSuchPlayerException Exception thrown if there isn't any player with that uid
     * @throws NoSuchTeacherException Exception thrown if the method tried to remove a teacher that is not available
     * @throws TooManyTeachersException Exception thrown if the method tried to add a teacher to a player who already
     * has all the teachers
     * @throws TeacherAlreadyInException Exception thrown if a player already owns the teacher the method tried to add
     */
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

    /**
     * This method checks if the teacher of the specified colour must be moved to the passed player also if there
     * is a draw in the number of students
     * @throws TooManyTeachersException thrown if the specified player already has all the teachers and the method
     * tries to add another one
     * @throws TeacherAlreadyInException thrown if the method tries to move the teacher to the specified player, but
     * he already has that teacher
     * @throws NoSuchTeacherException thrown if there is no teacher for the specified colour
     */
    @Override
    protected synchronized void teacherDominance(Player player, Colour colour) throws TooManyTeachersException,
                                                                                TeacherAlreadyInException,
                                                                                NoSuchTeacherException
    {
        if(colour==null || player==null) throw new NullPointerException();

        Player player2;
        int num1, num2;
        Teacher teacher = null;

        if (!player.checkTeacherPresence(colour) && player.getStudentNum(colour)>0){
            for(Teacher t : teachersList)
                if(t.getColour() == colour)
                    teacher = t;
            if(teacher==null)
                throw new NoSuchTeacherException();
            player2 = teacher.getCurrentPos();
            if(player2==null) {
                player.addTeacher(teacher);
                teacher.setNewPos(player);
            }
            else {
                num1 = player.getStudentNum(colour);
                num2 = player2.getStudentNum(colour);
                if (num1 >= num2) {
                    player.addTeacher(player2.removeTeacher(colour));
                    teacher.setNewPos(player);
                }
            }
        }
    }
}
