package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.ClientsHandler.Messages.ModelMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class IslandController
{
    @FXML
    ImageView is1,is2,is3,is4,is5,is6,is7,is8,is9,is10,is11,is12;
    @FXML
    ImageView mnIs1,mnIs2,mnIs3,mnIs4,mnIs5,mnIs6,mnIs7,mnIs8,mnIs9,mnIs10,mnIs11,mnIs12;
    @FXML
    ImageView towerIs1,towerIs2,towerIs3,towerIs4,towerIs5,towerIs6;
    @FXML
    ImageView towerIs7,towerIs8,towerIs9,towerIs10,towerIs11,towerIs12;
    @FXML
    Text bluIs1,greenIs1,pinkIs1,redIs1,yellowIs1;
    @FXML
    Text bluIs2,greenIs2,pinkIs2,redIs2,yellowIs2;
    @FXML
    Text bluIs3,greenIs3,pinkIs3,redIs3,yellowIs3;
    @FXML
    Text bluIs4,greenIs4,pinkIs4,redIs4,yellowIs4;
    @FXML
    Text bluIs5,greenIs5,pinkIs5,redIs5,yellowIs5;
    @FXML
    Text bluIs6,greenIs6,pinkIs6,redIs6,yellowIs6;
    @FXML
    Text bluIs7,greenIs7,pinkIs7,redIs7,yellowIs7;
    @FXML
    Text bluIs8,greenIs8,pinkIs8,redIs8,yellowIs8;
    @FXML
    Text bluIs9,greenIs9,pinkIs9,redIs9,yellowIs9;
    @FXML
    Text bluIs10,greenIs10,pinkIs10,redIs10,yellowIs10;
    @FXML
    Text bluIs11,greenIs11,pinkIs11,redIs11,yellowIs11;
    @FXML
    Text bluIs12,greenIs12,pinkIs12,redIs12,yellowIs12;
    @FXML
    ImageView cloud1,cloud2,cloud3,cloud4;
    @FXML
    ImageView s1cloud1,s2cloud1,s3cloud1,s4cloud1;
    @FXML
    ImageView s1cloud2,s2cloud2,s3cloud2,s4cloud2;
    @FXML
    ImageView s1cloud3,s2cloud3,s3cloud3,s4cloud3;
    @FXML
    ImageView s1cloud4,s2cloud4,s3cloud4,s4cloud4;
    @FXML
    ImageView characterCard1,characterCard2,characterCard3;
    @FXML
    Text currentPlayer,towersP1,towersP2,towersP3,towersP4;
    @FXML
    Text coinsP1,coinsP2,coinsP3,coinsP4,unusedCoins;
    @FXML
    Button playerID1,playerID2,playerID3,playerID4;

    private static GUI islandControllerGUI;
    public static void islandGUISetter(GUI gui)
    {
        islandControllerGUI=gui;
    }

    private void setActionOnPhaseDashboard()
    {
        ModelMessage game = islandControllerGUI.getModel();

        switch(game.getPhase())
        {
            case choose_card:
            {

            }break;
            case move_students:
            {

            }break;
            case move_mother_nature:
            {

            }break;
            case choose_cloud:
            {

            }break;
        }
    }



}
