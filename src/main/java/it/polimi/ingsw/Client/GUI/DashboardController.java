package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.ClientsHandler.Messages.ChooseCard;
import it.polimi.ingsw.ClientsHandler.Messages.Message;
import it.polimi.ingsw.ClientsHandler.Messages.ModelMessage;
import it.polimi.ingsw.ClientsHandler.Messages.StudentToDashboard;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Exceptions.EmptyException;
import it.polimi.ingsw.Model.Player;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashboardController extends Showable
{
    @FXML
    Button placeOnDashboard;
    @FXML
    Text nickPlayer,coins;
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
    @FXML
    ImageView edgeC1,edgeC2,edgeC3,edgeC4,edgeC5,edgeC6,edgeC7,edgeC8,edgeC9,edgeC10;
    @FXML
    ImageView usedC1,usedC2,usedC3,usedC4,usedC5,usedC6,usedC7,usedC8,usedC9,usedC10;
    @FXML
    ImageView lumS1,lumS2,lumS3,lumS4,lumS5,lumS6,lumS7,lumS8,lumS9;

    Image redS = new Image("resizedRedStudent.png");
    Image pinkS = new Image("resizedPinkStudent.png");
    Image greenS = new Image("resizedGreenStudent.png");
    Image yellowS = new Image("resizedYellowStudent.png");
    Image blueS = new Image("resizedBlueStudent.png");
    private final HashMap<Colour, ImageView> teacherDashboard = new HashMap<>();
    private final HashMap<Colour, HashMap<Integer, ImageView>> studentsDashboard = new HashMap<>();
    private final HashMap<Integer, ImageView> entranceStudents = new HashMap<>();
    private final HashMap<Integer, ImageView> pinkStudents = new HashMap<>();
    private final HashMap<Integer, ImageView> greenStudents = new HashMap<>();
    private final HashMap<Integer, ImageView> redStudents = new HashMap<>();
    private final HashMap<Integer, ImageView> yellowStudents = new HashMap<>();
    private final HashMap<Integer, ImageView> blueStudents = new HashMap<>();
    private final HashMap<Integer, ImageView> cardsMap = new HashMap<>();
    private final HashMap<Integer, ImageView> usedCards = new HashMap<>();
    private final HashMap<Integer, ImageView> edgeCards = new HashMap<>();
    private final HashMap<Integer, ImageView> lumStud = new HashMap<>();
    private final HashMap<Colour, Image> colourToImage = new HashMap<>();
    private ModelMessage game;
    private ReceiverGui receiver;
    private String nickname;
    private Stage window;
    private static ImageView selectedStudent;
    private static Colour selectStudentColour;
    private final List<Integer> cardsIndexes= new ArrayList<>();

    /**
     * This method associates each parameter of the fxml file in
     * a data structure of the DashboardController and set everything
     * to the default values furthermore it creates the window for the
     * Dashboard graphics
     */
    public void initialize(Parent scene, String nickname)
    {
        colourToImage.put(Colour.red, redS);
        colourToImage.put(Colour.pink, pinkS);
        colourToImage.put(Colour.blue, blueS);
        colourToImage.put(Colour.green, greenS);
        colourToImage.put(Colour.yellow, yellowS);

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

        for(int i=0; i<10; i++)
            cardsIndexes.add(i);

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

        for(int i=1; i<=10; i++)
            (cardsMap.get(i)).setVisible(true);

        disC.setVisible(false);

        usedCards.put(1, usedC1);
        usedCards.put(2, usedC2);
        usedCards.put(3, usedC3);
        usedCards.put(4, usedC4);
        usedCards.put(5, usedC5);
        usedCards.put(6, usedC6);
        usedCards.put(7, usedC7);
        usedCards.put(8, usedC8);
        usedCards.put(9, usedC9);
        usedCards.put(10, usedC10);

        edgeCards.put(1, edgeC1);
        edgeCards.put(2, edgeC2);
        edgeCards.put(3, edgeC3);
        edgeCards.put(4, edgeC4);
        edgeCards.put(5, edgeC5);
        edgeCards.put(6, edgeC6);
        edgeCards.put(7, edgeC7);
        edgeCards.put(8, edgeC8);
        edgeCards.put(9, edgeC9);
        edgeCards.put(10, edgeC10);

        for(int i=1; i<=10; i++)
        {
            (usedCards.get(i)).setVisible(false);
            (edgeCards.get(i)).setVisible(false);
        }

        lumStud.put(1, lumS1);
        lumStud.put(2, lumS2);
        lumStud.put(3, lumS3);
        lumStud.put(4, lumS4);
        lumStud.put(5, lumS5);
        lumStud.put(6, lumS6);
        lumStud.put(7, lumS7);
        lumStud.put(8, lumS8);
        lumStud.put(9, lumS9);

        for(int i=1; i<=9; i++)
        {
            (lumStud.get(i)).setVisible(false);
        }

        disableTeachers();
        disableStudentsOnDashboard();
        nickPlayer.setMouseTransparent(true);
        nickPlayer.setFocusTraversable(false);
        coins.setMouseTransparent(true);
        coins.setFocusTraversable(false);
        coins.setVisible(false);

        window= new Stage();
        window.setTitle("Player's Dashboard");
        window.setScene(new Scene(scene));
        window.getIcons().add(new Image("icon.png"));
        window.setResizable(false);
        window.show();
        window.setOnCloseRequest(event -> GUI.getInstance().removeShowableStage(this));
        this.nickname= nickname;
        nickPlayer.setText(nickname);
        GUI.getInstance().addShowableStage(this);
        receiver=GUI.getInstance().getReceiver();
        show();
    }

    /**
     * This method controls in which turn and phase of the game the players are and
     * decides which graphics elements enable
     */
    private void setActionOnPhaseDashboard()
    {
        if(GUI.getInstance().getModel().getCurrPlayerNickname().equals(GUI.getInstance().getNickName())
           && GUI.getInstance().getNickName().equals(nickname))
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
                    enablePlaceOnDash();
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

    /**
     * Method used to set the visibility and the number of coins owned by this player
     */
    public void setCoins()
    {
        if(game.isExpertMode())
        {
            for(int i=1; i<=game.getPlayerList().size(); i++)
            {
                if(game.getPlayerList().get(i-1).getuID().equals(nickname))
                {
                    coins.setText("Coins: " + game.getPlayerList().get(i - 1).getCoins());
                    coins.setVisible(true);
                }
            }
        }
    }

    /**
     * Method used to get the colour of the student selected by the user with the mouse click, before returning
     * the colour the student opacity is returned to normal
     * @return the colour of the student selected in your entrance
     */
    public synchronized static Colour getSelectStudentColour()
    {
        if(selectStudentColour==null)
            return null;
        Colour returnValue= selectStudentColour;
        selectedStudent.setOpacity(1);
        selectStudentColour=null;
        return returnValue;
    }

    /**
     * Method used to set the opacity of the student selected in your entrance
     * @param image the ImageView selected
     * @param colour the colour associated to the ImageView selected
     */
    public synchronized static void setSelectStudentColour(ImageView image, Colour colour)
    {
        if(selectStudentColour!=null)
            selectedStudent.setOpacity(1);
        selectStudentColour=colour;
        selectedStudent=image;
        selectedStudent.setOpacity(0.5);
    }

    /**
     * This method enable the possibility to interact with the placeOnDashboard button
     */
    private void enablePlaceOnDash()
    {
        placeOnDashboard.setDisable(false);
    }

    /**
     * This method disable the possibility to interact with the placeOnDashboard button
     */
    private void disablePlaceOnDash()
    {
        placeOnDashboard.setDisable(true);
    }

    /**
     * This method disable the possibility to interact with teachers ImageViews
     */
    private void disableTeachers()
    {
        for(Colour c: Colour.values())
        {
            (teacherDashboard.get(c)).setMouseTransparent(true);
            (teacherDashboard.get(c)).setFocusTraversable(false);
        }
    }

    /**
     * This method disable the possibility to interact with Cards ImageViews
     */
    private void disableCards()
    {
        for(int i=1; i<=10; i++)
        {
            (cardsMap.get(i)).setMouseTransparent(true);
            (cardsMap.get(i)).setFocusTraversable(false);

            (usedCards.get(i)).setMouseTransparent(true);
            (usedCards.get(i)).setFocusTraversable(false);
        }
    }

    /**
     * This method enable the possibility to interact with Cards ImageViews
     */
    private void enableCards()
    {
        for(int i=1; i<=10; i++)
        {
            (cardsMap.get(i)).setMouseTransparent(false);
            (cardsMap.get(i)).setFocusTraversable(true);

            (usedCards.get(i)).setMouseTransparent(false);
            (usedCards.get(i)).setFocusTraversable(true);
        }
    }

    /**
     * This method disable the possibility to interact with Entrance Students
     */
    private void disableEntranceStudents()
    {
        for(int i=1; i<=9; i++)
        {
            (entranceStudents.get(i)).setMouseTransparent(true);
            (entranceStudents.get(i)).setFocusTraversable(false);
        }
    }

    /**
     * This method enable the possibility to interact with Entrance Students
     */
    private void enableEntranceStudents()
    {
        int entranceSize= (1-game.getPlayerList().size()%2)*7+(game.getPlayerList().size()%2)*9;
        for(int i=1; i<=entranceSize; i++)
        {
            if(i>entranceStudentsNum())
            {
                (entranceStudents.get(i)).setMouseTransparent(true);
                (entranceStudents.get(i)).setFocusTraversable(false);
            }
            else
            {
                (entranceStudents.get(i)).setMouseTransparent(false);
                (entranceStudents.get(i)).setFocusTraversable(true);
            }
        }
    }

    /**
     * This method disable the possibility to interact with Dashboard Students
     */
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

    /**
     * @return the number of students in the entrance
     */
    private int entranceStudentsNum()
    {
        for(Player player: game.getPlayerList())
            if(player.getuID().equals(nickname))
                return player.getStudents().size();
        throw new IllegalArgumentException("Nickname doesn't correspond");
    }

    /**
     * This method display the correct amount of students on your entrance
     */
    private void setEntranceStudents()
    {
        for(int i=1; i<=game.getPlayerList().size(); i++)
        {
            if(game.getPlayerList().get(i-1).getuID().equals(nickname))
            {
                for(int j=1; j<=9; j++)
                {
                    if(j>entranceStudentsNum())
                        entranceStudents.get(j).setVisible(false);
                    else
                    {
                        (entranceStudents.get(j)).setImage(colourToImage.get(game.getPlayerList().get(i-1)
                                .getStudents().get(j-1)));
                        (entranceStudents.get(j)).setVisible(true);
                    }
                }
            }
        }

    }

    /**
     * This method display the correct amount of students and teachers on your dashboard
     */
    private void setStudentsAndTeachers()
    {
        for(int i=1; i<=game.getPlayerList().size(); i++)
        {
            if(game.getPlayerList().get(i-1).getuID().equals(nickname))
            {
                for(Colour cS: Colour.values())
                {
                    teacherDashboard.get(cS).setVisible(game.getPlayerList().get(i-1).checkTeacherPresence(cS));

                    for(int j=1; j<=10; j++)
                    {
                        if(j<=game.getPlayerList().get(i-1).getStudentNum(cS))
                            studentsDashboard.get(cS).get(j).setVisible(true);
                        else
                            studentsDashboard.get(cS).get(j).setVisible(false);
                    }
                }
            }
        }
    }

    /**
     * This method display the cards that you have not played yet
     */
    private void setCards()
    {
        Player yourself= null;
        for(Player player: game.getPlayerList())
            if(player.getuID().equals(nickname))
            {
                yourself=player;
                break;
            }
        if(yourself==null)
            throw new IllegalArgumentException("Nickname doesn't correspond");

        // set the hand's cards real index
        for(int i=0; i<10; i++)
            cardsIndexes.set(i, -1);
        
        int realIndex=0;
        for(int i=0; i<yourself.getHandCards().size(); i++)
        {
            cardsIndexes.set(yourself.getHandCards().get(i).getRoundValue()-1, realIndex);
            realIndex++;
        }
        
        // after setting the indexes we show the available cards
        for(int i=1; i<=10; i++)
        {
            if(cardsIndexes.get(i-1)!=-1)
            {
                cardsMap.get(i).setVisible(true);
                usedCards.get(i).setVisible(false);
            }
            else
            {
                cardsMap.get(i).setVisible(true);
                usedCards.get(i).setVisible(true);
            }
        }



        try {
            disC.setImage(cardsMap.get(yourself.getDiscardedCard().getRoundValue()).getImage());
            disC.setVisible(true);
        }catch(EmptyException ignored) {
            disC.setVisible(false);
        }
    }

    /**
     * Method used to send a message to the server after the activation of a character card
     * @param index position int the ModelMessage of the character card selected
     */
    private void useCard(int index)
    {
        // if the card wasn't already played
        if(cardsIndexes.get(index)!=-1)
            sendMessage(new ChooseCard(GUI.getInstance().getNickName(), cardsIndexes.get(index)));
    }
    /**
     * This method send the message to the receiver for the card 1
     */
    @FXML
    private void chooseCardMN1()
    {
        useCard(0);
    }

    /**
     * This method send the message to the receiver for the card 2
     */
    @FXML
    private void chooseCardMN2()
    {
        useCard(1);
    }

    /**
     * This method send the message to the receiver for the card 3
     */
    @FXML
    private void chooseCardMN3()
    {
        useCard(2);
    }

    /**
     * This method send the message to the receiver for the card 4
     */
    @FXML
    private void chooseCardMN4()
    {
        useCard(3);
    }

    /**
     * This method send the message to the receiver for the card 5
     */
    @FXML
    private void chooseCardMN5()
    {
        useCard(4);
    }

    /**
     * This method send the message to the receiver for the card 6
     */
    @FXML
    private void chooseCardMN6()
    {
        useCard(5);
    }

    /**
     * This method send the message to the receiver for the card 7
     */
    @FXML
    private void chooseCardMN7()
    {
        useCard(6);
    }

    /**
     * This method send the message to the receiver for the card 8
     */
    @FXML
    private void chooseCardMN8()
    {
        useCard(7);
    }

    /**
     * This method send the message to the receiver for the card 9
     */
    @FXML
    private void chooseCardMN9()
    {
        useCard(8);
    }

    /**
     * This method send the message to the receiver for the card 10
     */
    @FXML
    private void chooseCardMN10()
    {
        useCard(9);
    }

    /**
     * This method really send the message to the receiver for each card
     * @param message the message given by the caller function
     */
    private void sendMessage(Message message)
    {
        try{
            receiver.send(message);
        }catch (IOException e)
        {
            AlertBox.display("Fatal error", "Unable to communicate with the server");
            GUI.getInstance().closeAllWindows();
        }

    }

    /**
     * Method used by all the ImageView in the entrance after a mouse click
     * @param image the ImageView selected
     * @param index the position of the student in the entrance
     */
    private void selectedEntranceStudent(ImageView image, int index)
    {
        for(int i=0; i<game.getPlayerList().size(); i++)
        {
            if(game.getPlayerList().get(i).getuID().equals(nickname))
            {
                setSelectStudentColour(image, game.getPlayerList().get(i).getStudents().get(index));
            }
        }
    }

    /**
     * This method select the student 1 on the entrance and prepare it to be moved on dashboard or
     * on the first island which is clicked
     */
    @FXML
    private void select1()
    {
        selectedEntranceStudent(eSt1, 0);
    }

    /**
     * This method select the student 2 on the entrance and prepare it to be moved on dashboard or
     * on the first island which is clicked
     */
    @FXML
    private void select2()
    {
        selectedEntranceStudent(eSt2, 1);
    }

    /**
     * This method select the student 3 on the entrance and prepare it to be moved on dashboard or
     * on the first island which is clicked
     */
    @FXML
    private void select3()
    {
        selectedEntranceStudent(eSt3, 2);
    }

    /**
     * This method select the student 4 on the entrance and prepare it to be moved on dashboard or
     * on the first island which is clicked
     */
    @FXML
    private void select4()
    {
        selectedEntranceStudent(eSt4, 3);
    }

    /**
     * This method select the student 5 on the entrance and prepare it to be moved on dashboard or
     * on the first island which is clicked
     */
    @FXML
    private void select5()
    {
        selectedEntranceStudent(eSt5, 4);
    }

    /**
     * This method select the student 6 on the entrance and prepare it to be moved on dashboard or
     * on the first island which is clicked
     */
    @FXML
    private void select6()
    {
        selectedEntranceStudent(eSt6, 5);
    }

    /**
     * This method select the student 7 on the entrance and prepare it to be moved on dashboard or
     * on the first island which is clicked
     */
    @FXML
    private void select7()
    {
        selectedEntranceStudent(eSt7, 6);
    }

    /**
     * This method select the student 8 on the entrance and prepare it to be moved on dashboard or
     * on the first island which is clicked
     */
    @FXML
    private void select8()
    {
        selectedEntranceStudent(eSt8, 7);
    }

    /**
     * This method select the student 9 on the entrance and prepare it to be moved on dashboard or
     * on the first island which is clicked
     */
    @FXML
    private void select9()
    {
        selectedEntranceStudent(eSt9, 8);
    }

    /**
     * Method used to highlight the player's card 1 when passing on it with the mouse
     */
    @FXML
    private void mouseOnC1(){ edgeCards.get(1).setVisible(true);}

    /**
     * Method used to highlight the player's card 2 when passing on it with the mouse
     */
    @FXML
    private void mouseOnC2(){ edgeCards.get(2).setVisible(true);}

    /**
     * Method used to highlight the player's card 3 when passing on it with the mouse
     */
    @FXML
    private void mouseOnC3(){ edgeCards.get(3).setVisible(true);}

    /**
     * Method used to highlight the player's card 4 when passing on it with the mouse
     */
    @FXML
    private void mouseOnC4(){ edgeCards.get(4).setVisible(true);}

    /**
     * Method used to highlight the player's card 5 when passing on it with the mouse
     */
    @FXML
    private void mouseOnC5(){ edgeCards.get(5).setVisible(true);}

    /**
     * Method used to highlight the player's card 6 when passing on it with the mouse
     */
    @FXML
    private void mouseOnC6(){ edgeCards.get(6).setVisible(true);}

    /**
     * Method used to highlight the player's card 7 when passing on it with the mouse
     */
    @FXML
    private void mouseOnC7(){ edgeCards.get(7).setVisible(true);}

    /**
     * Method used to highlight the player's card 8 when passing on it with the mouse
     */
    @FXML
    private void mouseOnC8(){ edgeCards.get(8).setVisible(true);}

    /**
     * Method used to highlight the player's card 9 when passing on it with the mouse
     */
    @FXML
    private void mouseOnC9(){ edgeCards.get(9).setVisible(true);}

    /**
     * Method used to highlight the player's card 10 when passing on it with the mouse
     */
    @FXML
    private void mouseOnC10(){ edgeCards.get(10).setVisible(true);}

    /**
     * Method used to remove the possibility to highlight the player's card 1 when passing on it with
     * the mouse in case it's not the turn to choose a card
     */
    @FXML
    private void mouseOffC1(){ edgeCards.get(1).setVisible(false);}

    /**
     * Method used to remove the possibility to highlight the player's card 2 when passing on it with
     * the mouse in case it's not the turn to choose a card
     */
    @FXML
    private void mouseOffC2(){ edgeCards.get(2).setVisible(false);}

    /**
     * Method used to remove the possibility to highlight the player's card 3 when passing on it with
     * the mouse in case it's not the turn to choose a card
     */
    @FXML
    private void mouseOffC3(){ edgeCards.get(3).setVisible(false);}

    /**
     * Method used to remove the possibility to highlight the player's card 4 when passing on it with
     * the mouse in case it's not the turn to choose a card
     */
    @FXML
    private void mouseOffC4(){ edgeCards.get(4).setVisible(false);}

    /**
     * Method used to remove the possibility to highlight the player's card 5 when passing on it with
     * the mouse in case it's not the turn to choose a card
     */
    @FXML
    private void mouseOffC5(){ edgeCards.get(5).setVisible(false);}

    /**
     * Method used to remove the possibility to highlight the player's card 6 when passing on it with
     * the mouse in case it's not the turn to choose a card
     */
    @FXML
    private void mouseOffC6(){ edgeCards.get(6).setVisible(false);}

    /**
     * Method used to remove the possibility to highlight the player's card 7 when passing on it with
     * the mouse in case it's not the turn to choose a card
     */
    @FXML
    private void mouseOffC7(){ edgeCards.get(7).setVisible(false);}

    /**
     * Method used to remove the possibility to highlight the player's card 8 when passing on it with
     * the mouse in case it's not the turn to choose a card
     */
    @FXML
    private void mouseOffC8(){ edgeCards.get(8).setVisible(false);}

    /**
     * Method used to remove the possibility to highlight the player's card 9 when passing on it with
     * the mouse in case it's not the turn to choose a card
     */
    @FXML
    private void mouseOffC9(){ edgeCards.get(9).setVisible(false);}

    /**
     * Method used to remove the possibility to highlight the player's card 10 when passing on it with
     * the mouse in case it's not the turn to choose a card
     */
    @FXML
    private void mouseOffC10(){ edgeCards.get(10).setVisible(false);}

    /**
     * Method used to highlight the player's student 1 in the entrance when passing on it with the mouse
     */
    @FXML
    private void mouseOnS1(){ lumStud.get(1).setVisible(true);}

    /**
     * Method used to highlight the player's student 2 in the entrance when passing on it with the mouse
     */
    @FXML
    private void mouseOnS2(){ lumStud.get(2).setVisible(true);}

    /**
     * Method used to highlight the player's student 3 in the entrance when passing on it with the mouse
     */
    @FXML
    private void mouseOnS3(){ lumStud.get(3).setVisible(true);}

    /**
     * Method used to highlight the player's student 4 in the entrance when passing on it with the mouse
     */
    @FXML
    private void mouseOnS4(){ lumStud.get(4).setVisible(true);}

    /**
     * Method used to highlight the player's student 5 in the entrance when passing on it with the mouse
     */
    @FXML
    private void mouseOnS5(){ lumStud.get(5).setVisible(true);}

    /**
     * Method used to highlight the player's student 6 in the entrance when passing on it with the mouse
     */
    @FXML
    private void mouseOnS6(){ lumStud.get(6).setVisible(true);}

    /**
     * Method used to highlight the player's student 7 in the entrance when passing on it with the mouse
     */
    @FXML
    private void mouseOnS7(){ lumStud.get(7).setVisible(true);}

    /**
     * Method used to highlight the player's student 8 in the entrance when passing on it with the mouse
     */
    @FXML
    private void mouseOnS8(){ lumStud.get(8).setVisible(true);}

    /**
     * Method used to highlight the player's student 9 in the entrance when passing on it with the mouse
     */
    @FXML
    private void mouseOnS9(){ lumStud.get(9).setVisible(true);}

    /**
     * Method used to remove the possibility to highlight the player's student 1 on the entrance when passing
     * on it with the mouse in case it's not the turn to choose a card
     */
    @FXML
    private void mouseOffS1(){ lumStud.get(1).setVisible(false);}

    /**
     * Method used to remove the possibility to highlight the player's student 2 on the entrance when passing
     * on it with the mouse in case it's not the turn to choose a card
     */
    @FXML
    private void mouseOffS2(){ lumStud.get(2).setVisible(false);}

    /**
     * Method used to remove the possibility to highlight the player's student 3 on the entrance when passing
     * on it with the mouse in case it's not the turn to choose a card
     */
    @FXML
    private void mouseOffS3(){ lumStud.get(3).setVisible(false);}

    /**
     * Method used to remove the possibility to highlight the player's student 4 on the entrance when passing
     * on it with the mouse in case it's not the turn to choose a card
     */
    @FXML
    private void mouseOffS4(){ lumStud.get(4).setVisible(false);}

    /**
     * Method used to remove the possibility to highlight the player's student 5 on the entrance when passing
     * on it with the mouse in case it's not the turn to choose a card
     */
    @FXML
    private void mouseOffS5(){ lumStud.get(5).setVisible(false);}

    /**
     * Method used to remove the possibility to highlight the player's student 6 on the entrance when passing
     * on it with the mouse in case it's not the turn to choose a card
     */
    @FXML
    private void mouseOffS6(){ lumStud.get(6).setVisible(false);}

    /**
     * Method used to remove the possibility to highlight the player's student 7 on the entrance when passing
     * on it with the mouse in case it's not the turn to choose a card
     */
    @FXML
    private void mouseOffS7(){ lumStud.get(7).setVisible(false);}

    /**
     * Method used to remove the possibility to highlight the player's student 8 on the entrance when passing
     * on it with the mouse in case it's not the turn to choose a card
     */
    @FXML
    private void mouseOffS8(){ lumStud.get(8).setVisible(false);}

    /**
     * Method used to remove the possibility to highlight the player's student 9 on the entrance when passing
     * on it with the mouse in case it's not the turn to choose a card
     */
    @FXML
    private void mouseOffS9(){ lumStud.get(9).setVisible(false);}



    /**
     * This method complete the movement of the selected student on dashboard
     */
    public void placeOnDashboard()
    {
        Colour selected;
        selected= getSelectStudentColour();
        if(selected!=null)
            sendMessage(new StudentToDashboard(GUI.getInstance().getNickName(), selected));
    }

    /**
     * Method used to close this dashboard window when the GUI main class is closing
     */
    @Override
    public void close() { window.close(); }

    /**
     * Method used to stop all possible interactions with this dashboard window
     */
    @Override
    public void pause()
    {
        disableEntranceStudents();
        disablePlaceOnDash();
        disableCards();
    }

    /**
     * Method used to enable again the interaction with this dashboard window after the pause method was called
     */
    @Override
    public void resume()
    {
        setActionOnPhaseDashboard();
    }

    /**
     * This method shows and updates the GUI of the Dashboards
     */
    @Override
    public void show()
    {
        game=GUI.getInstance().getModel();
        setActionOnPhaseDashboard();
        setEntranceStudents();
        setStudentsAndTeachers();
        setCards();
        setCoins();
    }
}
