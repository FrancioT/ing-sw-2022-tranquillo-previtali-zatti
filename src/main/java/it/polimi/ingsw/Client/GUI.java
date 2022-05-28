package it.polimi.ingsw.Client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GUI extends Application {

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent firstMenu = FXMLLoader.load(getClass().getResource("firstMenu.fxml"));
        primaryStage.setTitle("Eriantys");
        primaryStage.setScene(new Scene(firstMenu, 1280, 720));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
