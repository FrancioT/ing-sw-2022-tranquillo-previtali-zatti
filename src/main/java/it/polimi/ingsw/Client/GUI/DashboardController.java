package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.ClientsHandler.Messages.ModelMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class DashboardController
{
    @FXML
    Button placeOnDashboard,quit;
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

    private static GUI dashboardControllerGUI;
    public static void dashboardGUISetter(GUI gui)
    {
        dashboardControllerGUI=gui;
    }

    public void quit()
    {
        //close this window
    }

    public void placeOnDashboard()
    {
        //after the selection of a student you can place it on dashboard
    }

    public void showDashboard()
    {
        //aggiorna lo stage con cose nuove ogni volta in base al model e aggiorna i bottoni con set actionOnPhase
        //ogni controller deve avere il suo show e un suo setAction (prima di caricare e showare la nuova scena aggiorna i parametiri
        // con setAction)
    }

    private void setActionOnPhaseDashboard()
    {
        ModelMessage game = dashboardControllerGUI.getModel();

        switch(game.getPhase())
        {
            case choose_card:
            {
                disableTeachers();
                disableStudentsOnDashboard();
                disableEntranceStudents();
                placeOnDashboard.setDisable(true);
                nickPlayer.setMouseTransparent(true);
                nickPlayer.setFocusTraversable(false);
            }break;
            case move_students:
            {
                disableTeachers();
                disableStudentsOnDashboard();
                enableEntranceStudents();
                placeOnDashboard.setDisable(false);
                nickPlayer.setMouseTransparent(true);
                nickPlayer.setFocusTraversable(false);
            }break;
            case move_mother_nature:
            {
                disableStudentsOnDashboard();
                disableTeachers();
                disableEntranceStudents();
                placeOnDashboard.setDisable(true);
                nickPlayer.setMouseTransparent(true);
                nickPlayer.setFocusTraversable(false);
            }break;
            case choose_cloud:
            {
                disableStudentsOnDashboard();
                disableEntranceStudents();
                disableTeachers();
                placeOnDashboard.setDisable(true);
                nickPlayer.setMouseTransparent(true);
                nickPlayer.setFocusTraversable(false);
            }break;
        }
    }

    private void disableTeachers()
    {
        pinkT.setMouseTransparent(true);
        pinkT.setFocusTraversable(false);
        greenT.setMouseTransparent(true);
        greenT.setFocusTraversable(false);
        redT.setMouseTransparent(true);
        redT.setFocusTraversable(false);
        yellowT.setMouseTransparent(true);
        yellowT.setFocusTraversable(false);
        blueT.setMouseTransparent(true);
        blueT.setFocusTraversable(false);
    }

    private void disableEntranceStudents()
    {
        eSt1.setMouseTransparent(true);
        eSt1.setFocusTraversable(false);
        eSt2.setMouseTransparent(true);
        eSt2.setFocusTraversable(false);
        eSt3.setMouseTransparent(true);
        eSt3.setFocusTraversable(false);
        eSt4.setMouseTransparent(true);
        eSt4.setFocusTraversable(false);
        eSt5.setMouseTransparent(true);
        eSt5.setFocusTraversable(false);
        eSt6.setMouseTransparent(true);
        eSt6.setFocusTraversable(false);
        eSt7.setMouseTransparent(true);
        eSt7.setFocusTraversable(false);
        eSt8.setMouseTransparent(true);
        eSt8.setFocusTraversable(false);
        eSt9.setMouseTransparent(true);
        eSt9.setFocusTraversable(false);
    }

    private void enableEntranceStudents()
    {
        eSt1.setMouseTransparent(false);
        eSt1.setFocusTraversable(true);
        eSt2.setMouseTransparent(false);
        eSt2.setFocusTraversable(true);
        eSt3.setMouseTransparent(false);
        eSt3.setFocusTraversable(true);
        eSt4.setMouseTransparent(false);
        eSt4.setFocusTraversable(true);
        eSt5.setMouseTransparent(false);
        eSt5.setFocusTraversable(true);
        eSt6.setMouseTransparent(false);
        eSt6.setFocusTraversable(true);
        eSt7.setMouseTransparent(false);
        eSt7.setFocusTraversable(true);
        eSt8.setMouseTransparent(false);
        eSt8.setFocusTraversable(true);
        eSt9.setMouseTransparent(false);
        eSt9.setFocusTraversable(true);
    }

    private void disableStudentsOnDashboard()
    {
        ps1.setMouseTransparent(true);
        ps1.setFocusTraversable(false);
        ps2.setMouseTransparent(true);
        ps2.setFocusTraversable(false);
        ps3.setMouseTransparent(true);
        ps3.setFocusTraversable(false);
        ps4.setMouseTransparent(true);
        ps4.setFocusTraversable(false);
        ps5.setMouseTransparent(true);
        ps5.setFocusTraversable(false);
        ps6.setMouseTransparent(true);
        ps6.setFocusTraversable(false);
        ps7.setMouseTransparent(true);
        ps7.setFocusTraversable(false);
        ps8.setMouseTransparent(true);
        ps8.setFocusTraversable(false);
        ps9.setMouseTransparent(true);
        ps9.setFocusTraversable(false);
        ps10.setMouseTransparent(true);
        ps10.setFocusTraversable(false);

        gs1.setMouseTransparent(true);
        gs1.setFocusTraversable(false);
        gs2.setMouseTransparent(true);
        gs2.setFocusTraversable(false);
        gs3.setMouseTransparent(true);
        gs3.setFocusTraversable(false);
        gs4.setMouseTransparent(true);
        gs4.setFocusTraversable(false);
        gs5.setMouseTransparent(true);
        gs5.setFocusTraversable(false);
        gs6.setMouseTransparent(true);
        gs6.setFocusTraversable(false);
        gs7.setMouseTransparent(true);
        gs7.setFocusTraversable(false);
        gs8.setMouseTransparent(true);
        gs8.setFocusTraversable(false);
        gs9.setMouseTransparent(true);
        gs9.setFocusTraversable(false);
        gs10.setMouseTransparent(true);
        gs10.setFocusTraversable(false);

        rs1.setMouseTransparent(true);
        rs1.setFocusTraversable(false);
        rs2.setMouseTransparent(true);
        rs2.setFocusTraversable(false);
        rs3.setMouseTransparent(true);
        rs3.setFocusTraversable(false);
        rs4.setMouseTransparent(true);
        rs4.setFocusTraversable(false);
        rs5.setMouseTransparent(true);
        rs5.setFocusTraversable(false);
        rs6.setMouseTransparent(true);
        rs6.setFocusTraversable(false);
        rs7.setMouseTransparent(true);
        rs7.setFocusTraversable(false);
        rs8.setMouseTransparent(true);
        rs8.setFocusTraversable(false);
        rs9.setMouseTransparent(true);
        rs9.setFocusTraversable(false);
        rs10.setMouseTransparent(true);
        rs10.setFocusTraversable(false);

        ys1.setMouseTransparent(true);
        ys1.setFocusTraversable(false);
        ys2.setMouseTransparent(true);
        ys2.setFocusTraversable(false);
        ys3.setMouseTransparent(true);
        ys3.setFocusTraversable(false);
        ys4.setMouseTransparent(true);
        ys4.setFocusTraversable(false);
        ys5.setMouseTransparent(true);
        ys5.setFocusTraversable(false);
        ys6.setMouseTransparent(true);
        ys6.setFocusTraversable(false);
        ys7.setMouseTransparent(true);
        ys7.setFocusTraversable(false);
        ys8.setMouseTransparent(true);
        ys8.setFocusTraversable(false);
        ys9.setMouseTransparent(true);
        ys9.setFocusTraversable(false);
        ys10.setMouseTransparent(true);
        ys10.setFocusTraversable(false);

        bs1.setMouseTransparent(true);
        bs1.setFocusTraversable(false);
        bs2.setMouseTransparent(true);
        bs2.setFocusTraversable(false);
        bs3.setMouseTransparent(true);
        bs3.setFocusTraversable(false);
        bs4.setMouseTransparent(true);
        bs4.setFocusTraversable(false);
        bs5.setMouseTransparent(true);
        bs5.setFocusTraversable(false);
        bs6.setMouseTransparent(true);
        bs6.setFocusTraversable(false);
        bs7.setMouseTransparent(true);
        bs7.setFocusTraversable(false);
        bs8.setMouseTransparent(true);
        bs8.setFocusTraversable(false);
        bs9.setMouseTransparent(true);
        bs9.setFocusTraversable(false);
        bs10.setMouseTransparent(true);
        bs10.setFocusTraversable(false);
    }

    public void setNickPlayer(String newNick)
    {
        nickPlayer.setText(newNick);
    }
}
