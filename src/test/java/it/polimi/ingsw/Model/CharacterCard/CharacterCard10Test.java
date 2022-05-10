package it.polimi.ingsw.Model.CharacterCard;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.NoSuchPlayerException;
import it.polimi.ingsw.Model.Exceptions.NoSuchStudentException;
import it.polimi.ingsw.Model.Exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.Model.ModelAndDecorators.ModelTest;
import it.polimi.ingsw.Model.Student;
import it.polimi.ingsw.RemoteView.RemoteView;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCard10Test
{
    @Test
    public void CharacterCard10Handle() throws Exception
    {
        String player1="Aldo";
        String player2="Giovanni";
        String player3="Giacomo";
        CharacterCard10 card = new CharacterCard10();
        List<CharacterCard> cards = new ArrayList<>();
        cards.add(card);
        Map<String, DataBuffer> uIDs = new HashMap<>();
        uIDs.put(player1, new DataBuffer(player1));
        uIDs.put(player2, new DataBuffer(player2));
        uIDs.put(player3, new DataBuffer(player3));
        Controller controller = new Controller(uIDs, true, new ArrayList<RemoteView>());
        ModelTest.changeCard(controller.getModel(), cards);

        try{
            card.handle(null, null, null);
            fail();
        }catch (NullPointerException n){};

        card.overPrice = true;

        try {
            card.handle("Aldo", new DataBuffer(player1), controller);
            fail();
        }catch (NotEnoughMoneyException n){};

        card.overPrice = false;

        try {
            card.handle("Ajeje", new DataBuffer(player1), controller);
            fail();
        }catch (NoSuchPlayerException n){};

        for(Colour c : controller.getModel().getStudents("Giacomo"))
            controller.getModel().entranceEmptier("Giacomo", c);

        controller.getModel().addStudentDashboard("Giacomo", new Student(Colour.red));
        controller.getModel().addStudentDashboard("Giacomo", new Student(Colour.blue));

        List<Student> students = new ArrayList<>();
        students.add(new Student(Colour.pink));
        students.add(new Student(Colour.green));
        controller.getModel().entranceFiller("Giacomo", students);

        assertTrue(ModelTest.getPlayers(controller.getModel()).get(0).checkTeacherPresence(Colour.red));
        assertTrue(ModelTest.getPlayers(controller.getModel()).get(0).checkTeacherPresence(Colour.blue));

        controller.getModel().addStudentDashboard("Aldo", new Student(Colour.blue));

        assertTrue(ModelTest.getPlayers(controller.getModel()).get(0).checkTeacherPresence(Colour.blue));

        DataBuffer data = new DataBuffer("Giacomo");

        List<Colour> colourserror = new ArrayList<>();
        colourserror.add(Colour.pink);
        colourserror.add(Colour.red);
        colourserror.add(Colour.pink);
        colourserror.add(Colour.blue);

        data.setStudentsColours(colourserror);

        try {
            card.handle("Giacomo", data, controller);
            fail();
        }catch (NoSuchStudentException n){};

        List<Colour> colourserror1 = new ArrayList<>();
        colourserror1.add(Colour.pink);
        colourserror1.add(Colour.red);
        colourserror1.add(Colour.green);
        colourserror1.add(Colour.yellow);

        data.setStudentsColours(colourserror1);

        try {
            card.handle("Giacomo", data, controller);
            fail();
        }catch (NoSuchStudentException n){};

        List<Colour> colourserror2 = new ArrayList<>();
        colourserror2.add(Colour.pink);
        colourserror2.add(Colour.red);
        colourserror2.add(Colour.green);

        data.setStudentsColours(colourserror2);

        try {
            card.handle("Giacomo", data, controller);
            fail();
        }catch (IllegalArgumentException i){};

        List<Colour> colours = new ArrayList<>();
        colours.add(Colour.pink);
        colours.add(Colour.red);
        colours.add(Colour.green);
        colours.add(Colour.blue);

        data.setStudentsColours(colours);
        card.handle("Giacomo", data, controller);

        assertTrue(ModelTest.getPlayers(controller.getModel()).get(0).checkTeacherPresence(Colour.red));
        assertTrue(ModelTest.getPlayers(controller.getModel()).get(1).checkTeacherPresence(Colour.blue));
        assertTrue(ModelTest.getPlayers(controller.getModel()).get(0).checkTeacherPresence(Colour.pink));
        assertTrue(ModelTest.getPlayers(controller.getModel()).get(0).checkTeacherPresence(Colour.green));
    }
}