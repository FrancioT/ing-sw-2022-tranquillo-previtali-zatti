package it.polimi.ingsw.Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GUI extends Application {

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        Image icona = new Image("icon.png");

        try
        {
            Parent firstMenu = FXMLLoader.load(getClass().getClassLoader().getResource("firstMenu.fxml"));
            primaryStage.setTitle("Eriantys");
            primaryStage.setScene(new Scene(firstMenu));
            primaryStage.getIcons().add(icona);
            primaryStage.setResizable(false);
            primaryStage.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
