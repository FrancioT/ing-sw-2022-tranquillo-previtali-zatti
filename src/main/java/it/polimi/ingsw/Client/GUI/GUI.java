package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.ClientsHandler.Messages.ModelMessage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GUI extends Application {

    private final List<Stage> allStages;
    private ModelMessage game;
    public void execute(String[] args)
    {
        launch(args);
    }

    public GUI()
    {
        allStages = new ArrayList<>();
        FirstMenuController.firstGUIsetter(this);
        DashboardController.dashboardGUISetter(this);
        IslandController.islandGUISetter(this);
    }

    @Override
    public void start(Stage primaryStage)
    {
        Image icon = new Image("icon.png");

        try
        {
            Parent firstMenu = FXMLLoader.load(getClass().getClassLoader().getResource("firstMenu.fxml"));
            primaryStage.setTitle("Eriantys");
            primaryStage.setScene(new Scene(firstMenu));
            primaryStage.getIcons().add(icon);
            primaryStage.setResizable(false);
            primaryStage.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void waiting4Players()
    {

    }

    public void addStage(Stage newStage)
    {
        allStages.add(newStage);
    }

    public ModelMessage getModel()
    {
        return game;
    }

}
