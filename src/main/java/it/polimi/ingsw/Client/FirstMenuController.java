package it.polimi.ingsw.Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FirstMenuController
{
    //Non so ancora se la devo mettere qui o nella GUI (credo GUI)
    CLI currentCLI = new CLI();

    @FXML
    private TextField nickname;
    @FXML
    private TextField serverIP;
    @FXML
    private TextField serverPort;
    @FXML
    private Button startButton;
    @FXML
    private AnchorPane errorAnhorPane;

    @FXML


    public void firstButton(ActionEvent event)
    {
        //Questa funzione prende l'ip la porta e il nickname e verifica
        //l'esistenza di una partita su quel server se no da la possibilità
        //a quel client di creare una partita.
        checkLogin();
    }

    private void checkLogin()
    {
        String port = serverPort.getText();

        if(!(nickname.getText().isEmpty() && serverIP.getText().isEmpty() && serverPort.getText().isEmpty()) && checkPort(port))
        {
            currentCLI.setNickName(nickname.getText());
            currentCLI.setServerIP(serverIP.getText());
            currentCLI.setServerPort(Integer.valueOf(port));
        }
        else
        {
            //metti a display un messaggio di errore che spiega
            //come inserire i parametri

            Stage errorStage = (Stage) errorAnhorPane.getScene().getWindow();
            Alert.AlertType type = Alert.AlertType.ERROR;
            Alert alert = new Alert(type,"");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(errorStage);
            alert.getDialogPane().setContentText("Please enter correct parameters");
            alert.getDialogPane().setHeaderText("Parameters error");
            alert.showAndWait();
        }
    }

    private boolean checkPort(String port)
    {
        return (Integer.valueOf(port)<=65535 && Integer.valueOf(port)>=1000);
    }
}
