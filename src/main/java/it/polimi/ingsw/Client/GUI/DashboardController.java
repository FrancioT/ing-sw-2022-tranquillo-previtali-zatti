package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.CLI.Receiver;
import it.polimi.ingsw.ClientsHandler.Messages.ChooseCard;
import it.polimi.ingsw.ClientsHandler.Messages.Message;
import it.polimi.ingsw.ClientsHandler.Messages.ModelMessage;
import it.polimi.ingsw.ClientsHandler.Messages.StudentToDashboard;
import it.polimi.ingsw.Model.Colour;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.HashMap;

/**
 *
 */
public class DashboardController extends Showable
{
    @FXML
    Button placeOnDashboard;
    @FXML
    Text nickPlayer;
    @FXML
    ImageView pinkT,greenT,redT,yellowT,blueT;
    @FXML
    ImageView ps1,ps2,ps3,ps4,ps5,ps6,ps7,ps8,ps9,ps10;
    @FXML
    ImageView gs1,gs2,gs3,gs4,gs5,gs6,gs7,gs8,gs9,gs10;
    @FXML
    ImageView rs1,rs2,rs3,rs4,rs5,rs6,rs7,rs8,rs9,rs10;
    @FXML
    ImageView ys1,ys2,ys3,ys4,ys5,ys6,ys7,ys8,ys9,ys10;
    @FXML
    ImageView bs1,bs2,bs3,bs4,bs5,bs6,bs7,bs8,bs9,bs10;
    @FXML
    ImageView eSt1,eSt2,eSt3,eSt4,eSt5,eSt6,eSt7,eSt8,eSt9;
    @FXML
    ImageView cc1,cc2,cc3,cc4,cc5,cc6,cc7,cc8,cc9,cc10,disC;

    Image redS = new Image("red_student.png)");
    Image pinkS = new Image("pink_student.png)");
    Image greenS = new Image("green_student.png)");
    Image yellowS = new Image("yellow_student.png)");
    Image blueS = new Image("blue_student.png)");
    private HashMap<Colour, ImageView> teacherDashboard = new HashMap<>();
    private HashMap<Colour, HashMap<Integer, ImageView>> studentsDashboard = new HashMap<>();
    private HashMap<Integer, ImageView> entranceStudents = new HashMap<>();
    private HashMap<Integer, ImageView> pinkStudents = new HashMap<>();
    private HashMap<Integer, ImageView> greenStudents = new HashMap<>();
    private HashMap<Integer, ImageView> redStudents = new HashMap<>();
    private HashMap<Integer, ImageView> yellowStudents = new HashMap<>();
    private HashMap<Integer, ImageView> blueStudents = new HashMap<>();
    private HashMap<Integer, ImageView> cardsMap = new HashMap<>();
    private ModelMessage game;
    private Receiver receiver;
    private String nickname;
    private Stage window;
    public static ImageView selectedStudent;
    public static Colour selectStudentColour;

    /**
     * This method associates each parameter of the fxml file in
     * a data structure of the DashboardController and set everything
     * to the default values
     */
    public void initialize(Parent scene, String nickname)
    {
        teacherDashboard.put(Colour.pink, pinkT);
        teacherDashboard.put(Colour.green, greenT);
        teacherDashboard.put(Colour.red, redT);
        teacherDashboard.put(Colour.yellow, yellowT);
        teacherDashboard.put(Colour.blue, blueT);

        for(Colour c: Colour.values())
        {
            (teacherDashboard.get(c)).setVisible(false);
        }

        studentsDashboard.put(Colour.pink, pinkStudents);
        studentsDashboard.put(Colour.green, greenStudents);
        studentsDashboard.put(Colour.red, redStudents);
        studentsDashboard.put(Colour.yellow, yellowStudents);
        studentsDashboard.put(Colour.blue, blueStudents);

        entranceStudents.put(1, eSt1);
        entranceStudents.put(2, eSt2);
        entranceStudents.put(3, eSt3);
        entranceStudents.put(4, eSt4);
        entranceStudents.put(5, eSt5);
        entranceStudents.put(6, eSt6);
        entranceStudents.put(7, eSt7);
        entranceStudents.put(8, eSt8);
        entranceStudents.put(9, eSt9);

        pinkStudents.put(1, ps1);
        pinkStudents.put(2, ps2);
        pinkStudents.put(3, ps3);
        pinkStudents.put(4, ps4);
        pinkStudents.put(5, ps5);
        pinkStudents.put(6, ps6);
        pinkStudents.put(7, ps7);
        pinkStudents.put(8, ps8);
        pinkStudents.put(9, ps9);
        pinkStudents.put(10, ps10);

        greenStudents.put(1, gs1);
        greenStudents.put(2, gs2);
        greenStudents.put(3, gs3);
        greenStudents.put(4, gs4);
        greenStudents.put(5, gs5);
        greenStudents.put(6, gs6);
        greenStudents.put(7, gs7);
        greenStudents.put(8, gs8);
        greenStudents.put(9, gs9);
        greenStudents.put(10, gs10);

        redStudents.put(1, rs1);
        redStudents.put(2, rs2);
        redStudents.put(3, rs3);
        redStudents.put(4, rs4);
        redStudents.put(5, rs5);
        redStudents.put(6, rs6);
        redStudents.put(7, rs7);
        redStudents.put(8, rs8);
        redStudents.put(9, rs9);
        redStudents.put(10, rs10);

        yellowStudents.put(1, ys1);
        yellowStudents.put(2, ys2);
        yellowStudents.put(3, ys3);
        yellowStudents.put(4, ys4);
        yellowStudents.put(5, ys5);
        yellowStudents.put(6, ys6);
        yellowStudents.put(7, ys7);
        yellowStudents.put(8, ys8);
        yellowStudents.put(9, ys9);
        yellowStudents.put(10, ys10);

        blueStudents.put(1, bs1);
        blueStudents.put(2, bs2);
        blueStudents.put(3, bs3);
        blueStudents.put(4, bs4);
        blueStudents.put(5, bs5);
        blueStudents.put(6, bs6);
        blueStudents.put(7, bs7);
        blueStudents.put(8, bs8);
        blueStudents.put(9, bs9);
        blueStudents.put(10, bs10);

        for(Colour c: Colour.values())
        {
            for(int i=1; i<=10; i++)
            {
                (studentsDashboard.get(c).get(i)).setVisible(false);
            }
        }

        cardsMap.put(1, cc1);
        cardsMap.put(2, cc2);
        cardsMap.put(3, cc3);
        cardsMap.put(4, cc4);
        cardsMap.put(5, cc5);
        cardsMap.put(6, cc6);
        cardsMap.put(7, cc7);
        cardsMap.put(8, cc8);
        cardsMap.put(9, cc9);
        cardsMap.put(10, cc10);
        cardsMap.put(11, disC);

        for(int i=1; i<=11; i++)
        {
            (cardsMap.get(i)).setVisible(true);
            if(i==11)
            {
                (cardsMap.get(i)).setVisible(false);
            }
        }

        disableTeachers();
        disableStudentsOnDashboard();
        nickPlayer.setMouseTransparent(true);
        nickPlayer.setFocusTraversable(false);

        window= new Stage();
        window.setScene(new Scene(scene));
        window.show();
        window.setOnCloseRequest(event -> GUI.getInstance().removeShowableStage(this));
        this.nickname= nickname;
        GUI.getInstance().addShowableStage(this);
        show();
    }

    private void setActionOnPhaseDashboard()
    {
        if(GUI.getInstance().getModel().getCurrPlayerNickname()== nickname)
        {
            switch(game.getPhase())
            {
                case choose_card:
                {
                    disableEntranceStudents();
                    disablePlaceOnDash();
                    enableCards();
                }break;
                case move_students:
                {
                    enableEntranceStudents();
                    disablePlaceOnDash();
                    disableCards();
                }break;
                case move_mother_nature: case choose_cloud:
                {
                    disableEntranceStudents();
                    disablePlaceOnDash();
                    disableCards();
                }break;
            }
        }
        else
        {
            disableEntranceStudents();
            disablePlaceOnDash();
            disableCards();
        }
    }

    private void enablePlaceOnDash()
    {
        placeOnDashboard.setDisable(false);
    }

    private void disablePlaceOnDash()
    {
        placeOnDashboard.setDisable(true);
    }

    private void disableTeachers()
    {
        for(Colour c: Colour.values())
        {
            (teacherDashboard.get(c)).setMouseTransparent(true);
            (teacherDashboard.get(c)).setFocusTraversable(false);
        }
    }

    private void disableCards()
    {
        for(int i=1; i<=11; i++)
        {
            (cardsMap.get(i)).setMouseTransparent(true);
            (cardsMap.get(i)).setFocusTraversable(false);
        }
    }

    private void enableCards()
    {
        for(int i=1; i<=11; i++)
        {
            (cardsMap.get(i)).setMouseTransparent(false);
            (cardsMap.get(i)).setFocusTraversable(true);
        }
    }

    private void disableEntranceStudents()
    {
        for(int i=1; i<=9; i++)
        {
            (entranceStudents.get(i)).setMouseTransparent(true);
            (entranceStudents.get(i)).setFocusTraversable(false);
        }
    }

    private void enableEntranceStudents()
    {
        for(int i=1; i<=9; i++)
        {
            (entranceStudents.get(i)).setMouseTransparent(false);
            (entranceStudents.get(i)).setFocusTraversable(true);
        }
    }

    private void disableStudentsOnDashboard()
    {
        for(Colour c: Colour.values())
        {
            for(int i=1; i<=10; i++)
            {
                (studentsDashboard.get(c).get(i)).setMouseTransparent(true);
                (studentsDashboard.get(c).get(i)).setFocusTraversable(false);
            }
        }
    }

    private int playersNum()
    {
        int n = (GUI.getInstance().getModel().getPlayerList()).size();
        return n;
    }

    private int currentMod()
    {
        if(playersNum()==2 || playersNum()==4)
        {
            return 7;
        }
        else
        {
            return 9;
        }
    }

    private void setEntranceStudents()
    {
        for(int i=1; i<=playersNum(); i++)
        {
            if(game.getPlayerList().get(i-1).getuID()== nickname)
            {
                for(int j=1; j<=currentMod(); j++)
                {
                    switch(game.getPlayerList().get(i-1).getStudents().get(j-1))
                    {
                        //Vanno aggiunti i corretti dimenisonamenti delle immagini perchè
                        //la posizione degli studenti nell'immagine è diversa
                        case pink:
                            (entranceStudents.get(j)).setImage(pinkS);
                            break;
                        case red:
                            (entranceStudents.get(j)).setImage(redS);
                            break;
                        case blue:
                            (entranceStudents.get(j)).setImage(blueS);
                            break;
                        case green:
                            (entranceStudents.get(j)).setImage(greenS);
                            break;
                        case yellow:
                            (entranceStudents.get(j)).setImage(yellowS);
                            break;
                    }
                    (entranceStudents.get(j)).setVisible(true);
                }
            }
        }

    }

    private void setStudentsAndTeachers()
    {
        for(int i=1; i<=playersNum(); i++)
        {
            if(game.getPlayerList().get(i-1).getuID()=="Nick del bottone, chiedere a francesco")
            {
                for(Colour cS: Colour.values())
                {
                    teacherDashboard.get(cS).setVisible(game.getPlayerList().get(i-1).checkTeacherPresence(cS));

                    for(int j=1; j<=game.getPlayerList().get(i-1).getStudentNum(cS); j++)
                    {
                        (studentsDashboard.get(cS).get(j)).setVisible(true);
                    }
                }
            }
        }
    }

    private void setCards()
    {
        for(int i=1; i<=playersNum(); i++)
        {
            if(game.getPlayerList().get(i-1).getuID()== nickname)
            {
                for(int j=1; j<=10; j++)
                {
                    if(game.getPlayerList().get(i-1).getHandCards().get(j-1).getRoundValue()==j)
                    {
                        cardsMap.get(j).setVisible(true);
                    }
                    else
                    {
                        cardsMap.get(j).setVisible(false);
                    }
                }
            }
        }
    }

    @FXML
    private void chooseCardMN1()
    {
        sendMessage(new ChooseCard(nickname, 1));
        disableCards();
    }

    @FXML
    private void chooseCardMN2()
    {
        sendMessage(new ChooseCard(nickname, 2));
        disableCards();
    }

    @FXML
    private void chooseCardMN3()
    {
        sendMessage(new ChooseCard(nickname, 3));
        disableCards();
    }

    @FXML
    private void chooseCardMN4()
    {
        sendMessage(new ChooseCard(nickname, 4));
        disableCards();
    }

    @FXML
    private void chooseCardMN5()
    {
        sendMessage(new ChooseCard(nickname, 5));
        disableCards();
    }

    @FXML
    private void chooseCardMN6()
    {
        sendMessage(new ChooseCard(nickname, 6));
        disableCards();
    }

    @FXML
    private void chooseCardMN7()
    {
        sendMessage(new ChooseCard(nickname, 7));
        disableCards();
    }

    @FXML
    private void chooseCardMN8()
    {
        sendMessage(new ChooseCard(nickname, 8));
        disableCards();
    }

    @FXML
    private void chooseCardMN9()
    {
        sendMessage(new ChooseCard(nickname, 9));
        disableCards();
    }

    @FXML
    private void chooseCardMN10()
    {
        sendMessage(new ChooseCard(nickname, 10));
        disableCards();
    }
    private void sendMessage(Message message)
    {
        try{
            receiver.send(message);
        }catch (IOException e)
        {
            AlertBox.display("Fatal error", "Unable to communicate with the server");
            GUI.getInstance().getWindow().fireEvent(new WindowEvent(GUI.getInstance().getWindow(),
                    WindowEvent.WINDOW_CLOSE_REQUEST));
        }

    }

    @FXML
    private void select1()
    {
        disableEntranceStudents();
        enablePlaceOnDash();
        selectedStudent=eSt1;
        for(int i=0; i<playersNum(); i++)
        {
            if(game.getPlayerList().get(i).getuID()== nickname)
            {
                selectStudentColour=game.getPlayerList().get(i).getStudents().get(0);
            }
        }

    }

    @FXML
    private void select2()
    {
        disableEntranceStudents();
        enablePlaceOnDash();
        selectedStudent=eSt2;
        for(int i=0; i<playersNum(); i++)
        {
            if(game.getPlayerList().get(i).getuID()== nickname)
            {
                selectStudentColour=game.getPlayerList().get(i).getStudents().get(1);
            }
        }
    }

    @FXML
    private void select3()
    {
        disableEntranceStudents();
        enablePlaceOnDash();
        selectedStudent=eSt3;
        for(int i=0; i<playersNum(); i++)
        {
            if(game.getPlayerList().get(i).getuID()== nickname)
            {
                selectStudentColour=game.getPlayerList().get(i).getStudents().get(2);
            }
        }
    }

    @FXML
    private void select4()
    {
        disableEntranceStudents();
        enablePlaceOnDash();
        selectedStudent=eSt4;
        for(int i=0; i<playersNum(); i++)
        {
            if(game.getPlayerList().get(i).getuID()== nickname)
            {
                selectStudentColour=game.getPlayerList().get(i).getStudents().get(3);
            }
        }
    }

    @FXML
    private void select5()
    {
        disableEntranceStudents();
        enablePlaceOnDash();
        selectedStudent=eSt5;
        for(int i=0; i<playersNum(); i++)
        {
            if(game.getPlayerList().get(i).getuID()==nickname)
            {
                selectStudentColour=game.getPlayerList().get(i).getStudents().get(4);
            }
        }
    }

    @FXML
    private void select6()
    {
        disableEntranceStudents();
        enablePlaceOnDash();
        selectedStudent=eSt6;
        for(int i=0; i<playersNum(); i++)
        {
            if(game.getPlayerList().get(i).getuID()== nickname)
            {
                selectStudentColour=game.getPlayerList().get(i).getStudents().get(5);
            }
        }
    }

    @FXML
    private void select7()
    {
        disableEntranceStudents();
        enablePlaceOnDash();
        selectedStudent=eSt7;
        for(int i=0; i<playersNum(); i++)
        {
            if(game.getPlayerList().get(i).getuID()== nickname)
            {
                selectStudentColour=game.getPlayerList().get(i).getStudents().get(6);
            }
        }
    }

    @FXML
    private void select8()
    {
        disableEntranceStudents();
        enablePlaceOnDash();
        selectedStudent=eSt8;
        for(int i=0; i<playersNum(); i++)
        {
            if(game.getPlayerList().get(i).getuID()== nickname)
            {
                selectStudentColour=game.getPlayerList().get(i).getStudents().get(7);
            }
        }
    }

    @FXML
    private void select9()
    {
        disableEntranceStudents();
        enablePlaceOnDash();
        selectedStudent=eSt9;
        for(int i=0; i<playersNum(); i++)
        {
            if(game.getPlayerList().get(i).getuID()== nickname)
            {
                selectStudentColour=game.getPlayerList().get(i).getStudents().get(8);
            }
        }
    }

    public void placeOnDashboard()
    {
        sendMessage(new StudentToDashboard(nickname, selectStudentColour));
        disablePlaceOnDash();
    }

    @Override
    public void show()
    {
        game=GUI.getInstance().getModel();
        receiver=GUI.getInstance().getReceiver();
        setActionOnPhaseDashboard();
        setEntranceStudents();
        setStudentsAndTeachers();
        setCards();
        nickPlayer.setText(nickname);
    }
}
