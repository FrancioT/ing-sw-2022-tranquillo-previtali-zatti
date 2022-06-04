package it.polimi.ingsw.Client.GUI;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class FirstMenuController
{
    @FXML
    private Button startButton;
    @FXML
    private TextField nickname;
    @FXML
    private RadioButton twoPlayers, threePlayers, fourPlayers;
    @FXML
    private RadioButton expertON, expertOFF;
    @FXML
    private Button startTheGame;
    @FXML
    private TextField serverIP;

    private ToggleGroup numPlayers;
    private ToggleGroup mode;
    private static Socket serverConnection;
    private static String yourNickname;
    private static final int serverPort=55790;
    private static final String firstPlayerMessage="Choose game mode";

    /**
     * This method verify that nickname and serverIP are correctly initialized and try the connection, if
     * it's the first client to connect to the server open the gameCreationScreen and if it's not open the
     * loadingScreen.
     */
    public void connection()
    { inputChecker(); }

    private void inputChecker()
    {
        String nick= nickname.getText();
        if((nick.trim().length()==0))
        {
            AlertBox.display("Error", "Unacceptable nickname");
            return;
        }
        yourNickname= nick;
        String[] ip= serverIP.getText().split(".");
        for(String part: ip)
            if(Integer.parseInt(part)>255)
            {
                AlertBox.display("Error", "Unacceptable server ip");
                return;
            }
        try {
            firstConnection();
        }catch (IOException e) {
            AlertBox.display("Error", "Unable to connect to the server");
            try{ serverConnection.close(); }catch (IOException ignored){}
            Parent firstMenu;
            try{
                firstMenu= FXMLLoader.load(getClass().getClassLoader().getResource("firstMenu.fxml"));
            }catch (IOException e1){ throw new RuntimeException(); }
            GUI.getInstance().getWindow().setScene(new Scene(firstMenu));
        }
    }

    private void firstConnection() throws IOException
    {
        serverConnection = new Socket(serverIP.getText(), serverPort);
        GUI.getInstance().getWindow().setOnCloseRequest(event -> closeWindow(event));
        BufferedReader in = new BufferedReader(new InputStreamReader(serverConnection.getInputStream()));
        boolean firstPlayer= in.readLine().equals(firstPlayerMessage);
        if(firstPlayer)
        {
            FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("gameCreatorScreen.fxml"));
            Parent choosingGame = loader.load();
            ((FirstMenuController)loader.getController()).chooseMode();
            GUI.getInstance().getWindow().setScene(new Scene(choosingGame));
        }
        else
        {
            String newNick = sendInfo(yourNickname, new String());
            if (!newNick.equals(yourNickname))
            {
                yourNickname = newNick;
                AlertBox.display("NickName already used", "Your nickname was changed in " + newNick);
            }
            GUI.getInstance().setNickName(newNick);
            Parent choosingGame = FXMLLoader.load(getClass().getClassLoader().getResource("loadingScreen.fxml"));
            GUI.getInstance().getWindow().setScene(new Scene(choosingGame));
            startReceiver();
        }
    }

    private void chooseMode()
    {
        numPlayers= new ToggleGroup();
        twoPlayers.setToggleGroup(numPlayers);
        threePlayers.setToggleGroup(numPlayers);
        fourPlayers.setToggleGroup(numPlayers);
        mode= new ToggleGroup();
        expertON.setToggleGroup(mode);
        expertOFF.setToggleGroup(mode);
        startTheGame.setOnAction(event -> sendMode());
    }

    private void sendMode()
    {
        if(numPlayers.getSelectedToggle()==null || mode.getSelectedToggle()==null)
            return;
        String modeMessage;
        Toggle selected= numPlayers.getSelectedToggle();
        if(selected==twoPlayers)
            modeMessage="2";
        else if(selected==threePlayers)
            modeMessage="3";
        else
            modeMessage="4";

        selected= mode.getSelectedToggle();
        if(selected==expertON)
            modeMessage= modeMessage.concat("1");
        else
            modeMessage= modeMessage.concat("2");

        try {
            String newNick = sendInfo(yourNickname, modeMessage);
            if (!newNick.equals(yourNickname))
            {
                yourNickname = newNick;
                AlertBox.display("NickName already used", "Your nickname was changed in " + newNick);
            }
            Parent waitingPlayers = FXMLLoader.load(getClass().getClassLoader().getResource("loadingScreen.fxml"));
            GUI.getInstance().getWindow().setScene(new Scene(waitingPlayers));
            startReceiver();
        }catch(IOException e){
            // closing the main window and the connection
            try{ serverConnection.close(); }catch(IOException ignored){}
            AlertBox.display("Error", "Connection error with the server");
            GUI.getInstance().getWindow().setOnCloseRequest(closeEvent -> {});
            GUI.getInstance().getWindow().fireEvent(new WindowEvent(GUI.getInstance().getWindow(),
                                                                    WindowEvent.WINDOW_CLOSE_REQUEST));
        }
    }

    private String sendInfo(String nickname, String mode) throws IOException
    {
        PrintWriter out = new PrintWriter(serverConnection.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(serverConnection.getInputStream()));
        out.println(nickname);
        if(!mode.isEmpty())
            out.println(mode);
        String newNickName= in.readLine();
        return newNickName;
    }

    private void startReceiver()
    {
        Task<Boolean> receiverTask= new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception
            {
                // if there was a failure during the creation of the receiver
                if(!GUI.getInstance().setReceiver(serverConnection))
                    return false;
                //now that the connection is established change the on closing window method
                GUI.getInstance().setClosingWindow();
                GUI.getInstance().getReceiver().run();
                return true;
            }
        };
        receiverTask.setOnSucceeded(event -> {
            if(!receiverTask.getValue()) {
                // closing the main window
                AlertBox.display("Error", "Failed to create a stable connection with the server");
                GUI.getInstance().getWindow().setOnCloseRequest(closeEvent -> {});
                GUI.getInstance().getWindow().fireEvent(new WindowEvent(GUI.getInstance().getWindow(),
                                                                        WindowEvent.WINDOW_CLOSE_REQUEST));
            }
        });
        Thread receiverThread= new Thread(receiverTask);
        receiverThread.setDaemon(true);
        receiverThread.start();
    }

    private void closeWindow(WindowEvent event)
    {
        event.consume();  // consume the main closing window event
        Stage popUp= new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setTitle("Closing window");
        popUp.setMinWidth(350);
        popUp.setMinHeight(300);

        Label text= new Label("Do you want to close the game?");
        Button yesButton= new Button("Yes");
        yesButton.setOnAction(ev -> {
            popUp.close();
            try{ serverConnection.close(); }catch(IOException ignored){}
            GUI.getInstance().getWindow().close();
        });
        Button noButton= new Button("No");
        noButton.setOnAction(ev -> popUp.close());
        VBox layout= new VBox(20);
        HBox buttons= new HBox(20);
        buttons.getChildren().addAll(yesButton, noButton);
        buttons.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(text, buttons);
        layout.setAlignment(Pos.CENTER);

        Scene scene= new Scene(layout);
        popUp.setScene(scene);
        popUp.showAndWait();
    }
}
