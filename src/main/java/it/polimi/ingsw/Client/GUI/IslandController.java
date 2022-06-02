package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.CLI.Receiver;
import it.polimi.ingsw.ClientsHandler.Messages.Message;
import it.polimi.ingsw.ClientsHandler.Messages.ModelMessage;
import it.polimi.ingsw.ClientsHandler.Messages.MoveMN;
import it.polimi.ingsw.ClientsHandler.Messages.StudentToIsland;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.ColourT;
import it.polimi.ingsw.Model.Exceptions.EmptyException;
import it.polimi.ingsw.Model.ModelAndDecorators.Phase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.HashMap;

public class IslandController extends Showable
{
    @FXML
    private ImageView is1,is2,is3,is4,is5,is6,is7,is8,is9,is10,is11,is12;
    @FXML
    private ImageView mnIs1,mnIs2,mnIs3,mnIs4,mnIs5,mnIs6,mnIs7,mnIs8,mnIs9,mnIs10,mnIs11,mnIs12;
    @FXML
    private ImageView towerIs1,towerIs2,towerIs3,towerIs4,towerIs5,towerIs6;
    @FXML
    private ImageView towerIs7,towerIs8,towerIs9,towerIs10,towerIs11,towerIs12;
    @FXML
    private Text bluIs1,greenIs1,pinkIs1,redIs1,yellowIs1;
    @FXML
    private Text bluIs2,greenIs2,pinkIs2,redIs2,yellowIs2;
    @FXML
    private Text bluIs3,greenIs3,pinkIs3,redIs3,yellowIs3;
    @FXML
    private Text bluIs4,greenIs4,pinkIs4,redIs4,yellowIs4;
    @FXML
    private Text bluIs5,greenIs5,pinkIs5,redIs5,yellowIs5;
    @FXML
    private Text bluIs6,greenIs6,pinkIs6,redIs6,yellowIs6;
    @FXML
    private Text bluIs7,greenIs7,pinkIs7,redIs7,yellowIs7;
    @FXML
    private Text bluIs8,greenIs8,pinkIs8,redIs8,yellowIs8;
    @FXML
    private Text bluIs9,greenIs9,pinkIs9,redIs9,yellowIs9;
    @FXML
    private Text bluIs10,greenIs10,pinkIs10,redIs10,yellowIs10;
    @FXML
    private Text bluIs11,greenIs11,pinkIs11,redIs11,yellowIs11;
    @FXML
    private Text bluIs12,greenIs12,pinkIs12,redIs12,yellowIs12;
    @FXML
    private ImageView cloud1,cloud2,cloud3,cloud4;
    @FXML
    private ImageView s1cloud1,s2cloud1,s3cloud1,s4cloud1;
    @FXML
    private ImageView s1cloud2,s2cloud2,s3cloud2,s4cloud2;
    @FXML
    private ImageView s1cloud3,s2cloud3,s3cloud3,s4cloud3;
    @FXML
    private ImageView s1cloud4,s2cloud4,s3cloud4,s4cloud4;
    @FXML
    private ImageView characterCard1,characterCard2,characterCard3;
    @FXML
    private Text currentPlayer,towersP1,towersP2,towersP3,towersP4;
    @FXML
    private Text coinsP1,coinsP2,coinsP3,coinsP4,unusedCoins;
    @FXML
    private Text numTIs1,numTIs2,numTIs3,numTIs4,numTIs5,numTIs6,numTIs7,numTIs8,numTIs9,numTIs10,numTIs11,numTIs12;
    @FXML
    private Button playerID1,playerID2,playerID3,playerID4;

    private HashMap<Integer, ImageView> islandsImages = new HashMap<>();
    private HashMap<Integer, HashMap<Colour, Text>> islandsStudents = new HashMap<>();
    private HashMap<Colour, Text> island1 = new HashMap<>();
    private HashMap<Colour, Text> island2 = new HashMap<>();
    private HashMap<Colour, Text> island3 = new HashMap<>();
    private HashMap<Colour, Text> island4 = new HashMap<>();
    private HashMap<Colour, Text> island5 = new HashMap<>();
    private HashMap<Colour, Text> island6 = new HashMap<>();
    private HashMap<Colour, Text> island7 = new HashMap<>();
    private HashMap<Colour, Text> island8 = new HashMap<>();
    private HashMap<Colour, Text> island9 = new HashMap<>();
    private HashMap<Colour, Text> island10 = new HashMap<>();
    private HashMap<Colour, Text> island11 = new HashMap<>();
    private HashMap<Colour, Text> island12 = new HashMap<>();
    private HashMap<Integer, ImageView> cloudsImages = new HashMap<>();
    private HashMap<Integer,HashMap<Integer, ImageView>> clouds = new HashMap<>();
    private HashMap<Integer, ImageView> cloud_1 = new HashMap<>();
    private HashMap<Integer, ImageView> cloud_2 = new HashMap<>();
    private HashMap<Integer, ImageView> cloud_3 = new HashMap<>();
    private HashMap<Integer, ImageView> cloud_4 = new HashMap<>();
    private HashMap<Integer, ImageView> towers = new HashMap<>();
    private HashMap<Integer, Text> towersNum = new HashMap<>();
    private HashMap<Integer, ImageView> motherNature = new HashMap<>();
    private HashMap<Integer, ImageView> characterCards = new HashMap<>();
    private HashMap<Integer, Button> playersButtons = new HashMap<>();
    private HashMap<Integer, Text> playersTowers = new HashMap<>();
    private HashMap<Integer, Text> playersCoins = new HashMap<>();
    private ModelMessage game;
    private Receiver receiver;
    private String nickName;
    private Image whiteT = new Image("wt.png)");
    private Image blackT = new Image("bt.png)");
    private Image grayT = new Image("gt.png)");
    private Image redS = new Image("red_student.png)");
    private Image pinkS = new Image("pink_student.png)");
    private Image greenS = new Image("green_student.png)");
    private Image yellowS = new Image("yellow_student.png)");
    private Image blueS = new Image("blue_student.png)");
    private Image card1 = new Image("CarteTOT_front.jpg");
    private Image card2 = new Image("CarteTOT_front2.jpg");
    private Image card3 = new Image("CarteTOT_front3.jpg");
    private Image card4 = new Image("CarteTOT_front4.jpg");
    private Image card5 = new Image("CarteTOT_front5.jpg");
    private Image card6 = new Image("CarteTOT_front6.jpg");
    private Image card7 = new Image("CarteTOT_front7.jpg");
    private Image card8 = new Image("CarteTOT_front8.jpg");
    private Image card9 = new Image("CarteTOT_front9.jpg");
    private Image card10 = new Image("CarteTOT_front10.jpg");
    private Image card11 = new Image("CarteTOT_front11.jpg");
    private Image card12 = new Image("CarteTOT_front12.jpg");

    /**
     * This method associates each parameter of the fxml file in
     * a data structure of the IslandController and set everything
     * to the default values furthermore it creates the window for the
     * Island graphics
     */
    public void initialize()
    {
        characterCards.put(1, characterCard1);
        characterCards.put(2, characterCard2);
        characterCards.put(3, characterCard3);

        islandsImages.put(1, is1);
        islandsImages.put(2, is2);
        islandsImages.put(3, is3);
        islandsImages.put(4, is4);
        islandsImages.put(5, is5);
        islandsImages.put(6, is6);
        islandsImages.put(7, is7);
        islandsImages.put(8, is8);
        islandsImages.put(9, is9);
        islandsImages.put(10, is10);
        islandsImages.put(11, is11);
        islandsImages.put(12, is12);

        island1.put(Colour.red, redIs1);
        island1.put(Colour.pink, pinkIs1);
        island1.put(Colour.green, greenIs1);
        island1.put(Colour.blue, bluIs1);
        island1.put(Colour.yellow, yellowIs1);

        island2.put(Colour.red, redIs2);
        island2.put(Colour.pink, pinkIs2);
        island2.put(Colour.green, greenIs2);
        island2.put(Colour.blue, bluIs2);
        island2.put(Colour.yellow, yellowIs2);

        island3.put(Colour.red, redIs3);
        island3.put(Colour.pink, pinkIs3);
        island3.put(Colour.green, greenIs3);
        island3.put(Colour.blue, bluIs3);
        island3.put(Colour.yellow, yellowIs3);

        island4.put(Colour.red, redIs4);
        island4.put(Colour.pink, pinkIs4);
        island4.put(Colour.green, greenIs4);
        island4.put(Colour.blue, bluIs4);
        island4.put(Colour.yellow, yellowIs4);

        island5.put(Colour.red, redIs5);
        island5.put(Colour.pink, pinkIs5);
        island5.put(Colour.green, greenIs5);
        island5.put(Colour.blue, bluIs5);
        island5.put(Colour.yellow, yellowIs5);

        island6.put(Colour.red, redIs6);
        island6.put(Colour.pink, pinkIs6);
        island6.put(Colour.green, greenIs6);
        island6.put(Colour.blue, bluIs6);
        island6.put(Colour.yellow, yellowIs6);

        island7.put(Colour.red, redIs7);
        island7.put(Colour.pink, pinkIs7);
        island7.put(Colour.green, greenIs7);
        island7.put(Colour.blue, bluIs7);
        island7.put(Colour.yellow, yellowIs7);

        island8.put(Colour.red, redIs8);
        island8.put(Colour.pink, pinkIs8);
        island8.put(Colour.green, greenIs8);
        island8.put(Colour.blue, bluIs8);
        island8.put(Colour.yellow, yellowIs8);

        island9.put(Colour.red, redIs9);
        island9.put(Colour.pink, pinkIs9);
        island9.put(Colour.green, greenIs9);
        island9.put(Colour.blue, bluIs9);
        island9.put(Colour.yellow, yellowIs9);

        island10.put(Colour.red, redIs10);
        island10.put(Colour.pink, pinkIs10);
        island10.put(Colour.green, greenIs10);
        island10.put(Colour.blue, bluIs10);
        island10.put(Colour.yellow, yellowIs10);

        island11.put(Colour.red, redIs11);
        island11.put(Colour.pink, pinkIs11);
        island11.put(Colour.green, greenIs11);
        island11.put(Colour.blue, bluIs11);
        island11.put(Colour.yellow, yellowIs11);

        island12.put(Colour.red, redIs12);
        island12.put(Colour.pink, pinkIs12);
        island12.put(Colour.green, greenIs12);
        island12.put(Colour.blue, bluIs12);
        island12.put(Colour.yellow, yellowIs12);

        islandsStudents.put(1, island1);
        islandsStudents.put(2, island2);
        islandsStudents.put(3, island3);
        islandsStudents.put(4, island4);
        islandsStudents.put(5, island5);
        islandsStudents.put(6, island6);
        islandsStudents.put(7, island7);
        islandsStudents.put(8, island8);
        islandsStudents.put(9, island9);
        islandsStudents.put(10, island10);
        islandsStudents.put(11, island11);
        islandsStudents.put(12, island12);

        cloud1.setVisible(false);
        cloud2.setVisible(false);
        cloud3.setVisible(false);
        cloud4.setVisible(false);

        cloudsImages.put(1, cloud1);
        cloudsImages.put(2, cloud2);
        cloudsImages.put(3, cloud3);
        cloudsImages.put(4, cloud4);

        cloud_1.put(1, s1cloud1);
        cloud_1.put(2, s2cloud1);
        cloud_1.put(3, s3cloud1);
        cloud_1.put(4, s4cloud1);

        cloud_2.put(1, s1cloud2);
        cloud_2.put(2, s2cloud2);
        cloud_2.put(3, s3cloud2);
        cloud_2.put(4, s4cloud2);

        cloud_3.put(1, s1cloud3);
        cloud_3.put(2, s2cloud3);
        cloud_3.put(3, s3cloud3);
        cloud_3.put(4, s4cloud3);

        cloud_4.put(1, s1cloud4);
        cloud_4.put(2, s2cloud4);
        cloud_4.put(3, s3cloud4);
        cloud_4.put(4, s4cloud4);

        clouds.put(1, cloud_1);
        clouds.put(2, cloud_2);
        clouds.put(3, cloud_3);
        clouds.put(4, cloud_4);

        towers.put(1, towerIs1);
        towers.put(2, towerIs2);
        towers.put(3, towerIs3);
        towers.put(4, towerIs4);
        towers.put(5, towerIs5);
        towers.put(6, towerIs6);
        towers.put(7, towerIs7);
        towers.put(8, towerIs8);
        towers.put(9, towerIs9);
        towers.put(10, towerIs10);
        towers.put(11, towerIs11);
        towers.put(12, towerIs12);

        towersNum.put(1, numTIs1);
        towersNum.put(2, numTIs2);
        towersNum.put(3, numTIs3);
        towersNum.put(4, numTIs4);
        towersNum.put(5, numTIs5);
        towersNum.put(6, numTIs6);
        towersNum.put(7, numTIs7);
        towersNum.put(8, numTIs8);
        towersNum.put(9, numTIs9);
        towersNum.put(10, numTIs10);
        towersNum.put(11, numTIs11);
        towersNum.put(12, numTIs12);

        motherNature.put(1, mnIs1);
        motherNature.put(2, mnIs2);
        motherNature.put(3, mnIs3);
        motherNature.put(4, mnIs4);
        motherNature.put(5, mnIs5);
        motherNature.put(6, mnIs6);
        motherNature.put(7, mnIs7);
        motherNature.put(8, mnIs8);
        motherNature.put(9, mnIs9);
        motherNature.put(10, mnIs10);
        motherNature.put(11, mnIs11);
        motherNature.put(12, mnIs12);

        playersButtons.put(1, playerID1);
        playersButtons.put(2, playerID2);
        playersButtons.put(3, playerID3);
        playersButtons.put(4, playerID4);

        for(Button button: playersButtons.values())
        {
            button.setVisible(false);
            button.setMouseTransparent(true);
            button.setFocusTraversable(false);
        }

        playersTowers.put(1, towersP1);
        playersTowers.put(2, towersP2);
        playersTowers.put(3, towersP3);
        playersTowers.put(4, towersP4);

        playersCoins.put(1, coinsP1);
        playersCoins.put(2, coinsP2);
        playersCoins.put(3, coinsP3);
        playersCoins.put(4, coinsP4);

        for(int i=1; i<=12; i++)
        {
            (islandsImages.get(i)).setVisible(true);
            (motherNature.get(i)).setVisible(false);
            (towers.get(i)).setVisible(false);
        }

        disableTowersAndMN();
        disableIslands();
        disableClouds();
        expertActivation(GUI.getInstance().getModel().isExpertMode());
    }

    /**
     * This method verify if the expert mode is active and set visible or invisible
     * the characters cards
     * @param mod
     */
    private void expertActivation(boolean mod)
    {
        if(mod)
            for(int i=1; i<=3; i++)
                characterCards.get(i).setVisible(true);
        else
            for(int i=1; i<=3; i++)
            {
                characterCards.get(i).setVisible(false);
                characterCards.get(i).setOnMouseClicked(event -> {});
            }
    }

    /**
     * @return the number of players in this game
     */
    private int playersNum()
    {
        int n = (GUI.getInstance().getModel().getPlayerList()).size();
        return n;
    }

    /**
     * This method disable the possibility to interact with mother nature and towers
     */
    private void disableTowersAndMN()
    {
        for(int i=1; i<=12; i++)
        {
            towers.get(i).setOnMouseClicked(event -> {});
            motherNature.get(i).setOnMouseClicked(event -> {});
        }
    }

    /**
     * This method enable the possibility to interact with islands on board
     */
    private void enableIslands()
    {
        for(int i=1; i<=12; i++)
        {
            (islandsImages.get(i)).setMouseTransparent(false);
            (islandsImages.get(i)).setFocusTraversable(true);
        }
    }

    /**
     * This method disable the possibility to interact with islands on board
     */
    private void disableIslands()
    {
        for(int i=1; i<=12; i++)
        {
            (islandsImages.get(i)).setMouseTransparent(true);
            (islandsImages.get(i)).setFocusTraversable(false);
        }
    }

    /**
     * This method enable the possibility to interact with clouds on board
     */
    private void enableClouds()
    {
        for(int i=1; i<=playersNum(); i++)
        {
            (cloudsImages.get(i)).setMouseTransparent(false);
            (cloudsImages.get(i)).setFocusTraversable(true);
        }
    }

    /**
     * This method disable the possibility to interact with clouds on board
     */
    private void disableClouds()
    {
        for(int i=1; i<=playersNum(); i++)
        {
            (cloudsImages.get(i)).setMouseTransparent(true);
            (cloudsImages.get(i)).setFocusTraversable(false);
        }
    }

    /**
     * This method take towerC and islandIndex in input and if there is not a tower's image displayed
     * on board set it of the corresponding colour towerC on the island islandIndex; if a tower's image
     * it's already there displayed and the corresponding colour is different the method changes the image
     * @param islandIndex
     * @param towerC
     */
    public void addAndSwapTower(Integer islandIndex, ColourT towerC)
    {
        switch (towerC)
        {
            case white:
            {
                (towers.get(islandIndex)).setImage(whiteT);
                (towers.get(islandIndex)).setVisible(true);
            }break;
            case black:
            {
                (towers.get(islandIndex)).setImage(blackT);
                (towers.get(islandIndex)).setVisible(true);
            }break;
            case grey:
            {
                (towers.get(islandIndex)).setImage(grayT);
                (towers.get(islandIndex)).setVisible(true);
            }break;
        }
    }

    /**
     * This method setup character cards
     */
    private void setCharacterCards()
    {
        if(!game.isExpertMode())
            return;
        for(int i=1; i<=3; i++)
        {
            switch (game.getCharacterCardList().get(i-1).getCardID())
            {
                case 1:
                {
                    characterCards.get(i).setImage(card1);
                    characterCards.get(i).setOnMouseClicked(event -> activateCard1());
                }break;
                case 2:
                {
                    characterCards.get(i).setImage(card2);
                    characterCards.get(i).setOnMouseClicked(event -> activateCard2());
                }break;
                case 3:
                {
                    characterCards.get(i).setImage(card3);
                    characterCards.get(i).setOnMouseClicked(event -> activateCard3());
                }break;
                case 4:
                {
                    characterCards.get(i).setImage(card4);
                    characterCards.get(i).setOnMouseClicked(event -> activateCard4());
                }break;
                case 5:
                {
                    characterCards.get(i).setImage(card5);
                    characterCards.get(i).setOnMouseClicked(event -> activateCard5());
                }break;
                case 6:
                {
                    characterCards.get(i).setImage(card6);
                    characterCards.get(i).setOnMouseClicked(event -> activateCard6());
                }break;
                case 7:
                {
                    characterCards.get(i).setImage(card7);
                    characterCards.get(i).setOnMouseClicked(event -> activateCard7());
                }break;
                case 8:
                {
                    characterCards.get(i).setImage(card8);
                    characterCards.get(i).setOnMouseClicked(event -> activateCard8());
                }break;
                case 9:
                {
                    characterCards.get(i).setImage(card9);
                    characterCards.get(i).setOnMouseClicked(event -> activateCard9());
                }break;
                case 10:
                {
                    characterCards.get(i).setImage(card10);
                    characterCards.get(i).setOnMouseClicked(event -> activateCard10());
                }break;
                case 11:
                {
                    characterCards.get(i).setImage(card11);
                    characterCards.get(i).setOnMouseClicked(event -> activateCard11());
                }break;
                case 12:
                {
                    characterCards.get(i).setImage(card12);
                    characterCards.get(i).setOnMouseClicked(event -> activateCard12());
                }break;
            }
        }
    }


    private void activateCard1()
    {

    }
    private void activateCard2()
    {

    }
    private void activateCard3()
    {

    }
    private void activateCard4()
    {

    }
    private void activateCard5()
    {

    }
    private void activateCard6()
    {

    }
    private void activateCard7()
    {

    }
    private void activateCard8()
    {

    }
    private void activateCard9()
    {

    }
    private void activateCard10()
    {

    }
    private void activateCard11()
    {

    }
    private void activateCard12()
    {

    }

    @FXML
    private void clickCloud1()
    {

    }
    @FXML
    private void clickCloud2()
    {

    }
    @FXML
    private void clickCloud3()
    {

    }
    @FXML
    private void clickCloud4()
    {

    }

    @FXML
    private void clickIs1()
    {

    }
    @FXML
    private void clickIs2()
    {

    }
    @FXML
    private void clickIs3()
    {

    }
    @FXML
    private void clickIs4()
    {

    }
    @FXML
    private void clickIs5()
    {

    }
    @FXML
    private void clickIs6()
    {

    }
    @FXML
    private void clickIs7()
    {

    }
    @FXML
    private void clickIs8()
    {

    }
    @FXML
    private void clickIs9()
    {

    }
    @FXML
    private void clickIs10()
    {

    }
    @FXML
    private void clickIs11()
    {

    }
    @FXML
    private void clickIs12()
    {

    }
    private void selectedIsland(int index)
    {
        if(game.getPhase()==Phase.move_mother_nature)
        {
            sendMessage(new MoveMN(nickName, index));
        }
        else if(game.getPhase()==Phase.move_students)
        {
            if(DashboardController.selectStudentColour!=null)
            {
                sendMessage(new StudentToIsland(nickName, DashboardController.selectStudentColour,
                        index));
                DashboardController.selectStudentColour=null;
            }
        }
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



    /**
     *
     */
    private void setCurrentPlayer()
    {
        String[] phaseTmp= game.getPhase().toString().split("_");
        String phase="";
        for(String s: phaseTmp)
            phase=phase.concat(" "+s);

        currentPlayer.setText("Current player: " + game.getCurrPlayerNickname()+"\n\nPhase: "+phase);
    }

    /**
     *
     */
    private void setUnusedCoins()
    {
        if(game.isExpertMode())
            unusedCoins.setText("Unused coins: " + game.getUnusedCoins());
        else
            unusedCoins.setVisible(false);
    }

    /**
     *
     */
    private void setPlayersCoinsAndTowers()
    {
        for(int i=1; i<=game.getPlayerList().size(); i++)
        {
            playersTowers.get(i).setText("Towers: " + game.getPlayerList().get(i-1).getTowers().availabilityChecker());
            if(game.isExpertMode())
                playersCoins.get(i).setText("Coins: " + game.getPlayerList().get(i-1).getCoins());
            else
                playersCoins.get(i).setVisible(false);
        }
    }

    /**
     *
     */
    private void setMotherNature()
    {
        for(int i=1; i<=game.getIslandList().size(); i++)
        {
            if(game.getIslandList().get(i-1).isMotherNatureFlag())
            {
                motherNature.get(i).setVisible(true);
            }
            else
            {
                motherNature.get(i).setVisible(false);
            }
        }
    }

    /**
     * @param cS
     * @param islandIndex
     * @return
     */
    private int studentsColourOnIsland(Colour cS, Integer islandIndex)
    {
        int num=0;
        for(int i=0; i<=game.getIslandList().get(islandIndex).getStudentsColours().size(); i++)
        {
            if(game.getIslandList().get(islandIndex).getStudentsColours().get(i)==cS)
            {
                num++;
            }
        }
        return num;
    }

    /**
     *
     */
    private void setIslandsStudents()
    {
        for(int i=1; i<=game.getIslandList().size(); i++)
        {
            for(Colour cS: Colour.values())
            {
                islandsStudents.get(i).get(cS).setText(Integer.toString(studentsColourOnIsland(cS, i-1)));
            }
        }
    }

    /**
     *
     */
    private void setClouds()
    {
        for(int i=1; i<=playersNum(); i++)
        {
            cloudsImages.get(i).setVisible(true);
            for(int j=1; j<=studentsOnCLoud(); i++)
            {
                switch(game.getCloudList().get(i-1).getStudentsColours().get(j-1))
                {
                    case blue:
                    {
                        clouds.get(i).get(j).setImage(blueS);
                    }break;
                    case red:
                    {
                        clouds.get(i).get(j).setImage(redS);
                    }break;
                    case green:
                    {
                        clouds.get(i).get(j).setImage(greenS);
                    }break;
                    case yellow:
                    {
                        clouds.get(i).get(j).setImage(yellowS);
                    }break;
                    case pink:
                    {
                        clouds.get(i).get(j).setImage(pinkS);
                    }break;
                }
                clouds.get(i).get(j).setVisible(true);
            }
        }
    }

    /**
     * @return
     */
    private int studentsOnCLoud()
    {
        return game.getCloudList().get(0).getStudentsColours().size();
    }

    /**
     *
     */
    private void setActionOnPhaseIsland()
    {
        if(!game.getCurrPlayerNickname().equals(nickName))
        {
            disableClouds();
            disableIslands();
            return;
        }
        switch(game.getPhase())
        {
            case choose_card:
            {
                disableClouds();
                disableIslands();
            }break;
            case move_students: case move_mother_nature:
            {
                disableClouds();
                enableIslands();
            }break;
            case choose_cloud:
            {
                disableIslands();
                enableClouds();
            }break;
        }
    }

    /**
     *
     */
    private void setNicknames()
    {
        for(int i=0; i<=(playersNum()-1); i++)
        {
            (playersButtons.get(i+1)).setText(game.getPlayerList().get(i).getuID());
            (playersButtons.get(i+1)).setVisible(true);
            (playersButtons.get(i+1)).setMouseTransparent(false);
            (playersButtons.get(i+1)).setFocusTraversable(true);
        }
    }

    /**
     *
     */
    private void setTowers()
    {
        for (int i=1; i<=game.getIslandList().size(); i++)
        {
            if(game.getIslandList().get(i-1).getNumTowers()>=1)
            {
                try{
                    addAndSwapTower(i, game.getIslandList().get(i-1).getTowersColour());
                }catch (EmptyException e){System.out.println("Error in getting towers");}
                towersNum.get(i).setText(Integer.toString(game.getIslandList().get(i-1).getNumTowers()));
            }
            else
                towers.get(i).setVisible(false);
        }
    }

    /**
     *
     */
    @FXML
    public void showDashboard1()
    {
        Parent scene;
        FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("dashboardsGameScreen.fxml"));
        try{ scene= loader.load(); }catch (IOException e){ throw new RuntimeException(); }
        ((DashboardController)loader.getController()).initialize(scene, game.getPlayerList().get(0).getuID());
    }

    /**
     *
     */
    @FXML
    public void showDashboard2()
    {
        Parent scene;
        FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("dashboardsGameScreen.fxml"));
        try{ scene= loader.load(); }catch (IOException e){ throw new RuntimeException(); }
        ((DashboardController)loader.getController()).initialize(scene, game.getPlayerList().get(1).getuID());
    }

    /**
     *
     */
    @FXML
    public void showDashboard3()
    {
        Parent scene;
        FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("dashboardsGameScreen.fxml"));
        try{ scene= loader.load(); }catch (IOException e1){ throw new RuntimeException(); }
        ((DashboardController)loader.getController()).initialize(scene, game.getPlayerList().get(2).getuID());
    }

    /**
     *
     */
    @FXML
    public void showDashboard4()
    {
        Parent scene;
        FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("dashboardsGameScreen.fxml"));
        try{ scene= loader.load(); }catch (IOException e1){ throw new RuntimeException(); }
        ((DashboardController)loader.getController()).initialize(scene, game.getPlayerList().get(3).getuID());
    }

    /**
     * This method shows and updates the GUI of the Island
     */
    @Override
    public void show()
    {
        game= GUI.getInstance().getModel();
        receiver= GUI.getInstance().getReceiver();
        nickName= GUI.getInstance().getNickName();
        setActionOnPhaseIsland();
        setCharacterCards();
        setCurrentPlayer();
        setUnusedCoins();
        setPlayersCoinsAndTowers();
        setMotherNature();
        setClouds();
        setIslandsStudents();
        setNicknames();
        setTowers();
    }
}
