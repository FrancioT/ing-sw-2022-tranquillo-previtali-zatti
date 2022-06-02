package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.CLI.Receiver;
import it.polimi.ingsw.ClientsHandler.Messages.ModelMessage;
import it.polimi.ingsw.Model.CharacterCard.CharacterCard;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Model.Player;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GUI extends Application implements PropertyChangeListener
{
    private static final Boolean instanceLock= false;
    private static GUI instance= null;
    private final List<Showable> allStages;
    private ModelMessage game;
    private Receiver receiver;
    private Stage window;
    private String nickName;


    public static void execute(String[] args)
    {
        launch(args);
    }

    public static GUI getInstance()
    {
        if(instance==null)
        {
            synchronized(instanceLock) {
                while (instance==null)
                    try{ instanceLock.wait(); }catch(InterruptedException ignored){}
            }
            instance= new GUI();
        }
        return instance;
    }

    public GUI()
    {
        synchronized(instanceLock)
        {
            if(instance!=null)
                throw new RuntimeException("Illegal access, another instance was already created");
        }
        allStages = new ArrayList<>();
        game=null;
        receiver=null;
        window=null;
    }

    @Override
    public void start(Stage primaryStage)
    {
        synchronized(instanceLock) {
            instance= this;
            instanceLock.notifyAll();
        }
        Image icon = new Image("icon.png");
        window= primaryStage;
        try
        {
            Parent firstMenu = FXMLLoader.load(getClass().getClassLoader().getResource("firstMenu.fxml"));
            window.setTitle("Eriantys");
            window.setScene(new Scene(firstMenu));
            window.getIcons().add(icon);
            window.setResizable(false);
            window.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setReceiver(Socket serverConnection)
    {
        try {
            receiver= new Receiver(serverConnection);
            receiver.addPropertyChangeListener(this);
            receiver.start();
        }catch(IOException e) {
            // closing the main window
            AlertBox.display("Error", "Failed to create a stable connection with the server");
            window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
    }

    public Receiver getReceiver()
    {
        return  receiver;
    }

    public void setNickName(String nickName)
    {
        this.nickName= nickName;
    }

    public synchronized void addShowableStage(Showable showable)
    {
        allStages.add(showable);
    }

    public synchronized void removeShowableStage(Showable showable)
    {
        allStages.remove(showable);
    }

    public synchronized Stage getWindow() { return window; }

    public synchronized ModelMessage getModel() { return game; }

    public void setClosingWindow()
    {
        window.setOnCloseRequest(event -> {
            Stage window= new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Closing window");
            window.setMinWidth(350);
            window.setMinHeight(300);

            Label text= new Label("Do you want to close the game?");
            Button yesButton= new Button("Yes");
            yesButton.setOnAction(ev -> {
                window.close();
                try{ receiver.close(); }catch(IOException ignored){}
            });
            Button noButton= new Button("No");
            yesButton.setOnAction(ev -> {
                window.close();
                event.consume();  // consume the main closing window event
            });
            VBox layout= new VBox(20);
            HBox buttons= new HBox(20);
            buttons.getChildren().addAll(yesButton, noButton);
            layout.getChildren().addAll(text, buttons);
            layout.setAlignment(Pos.CENTER);

            Scene scene= new Scene(layout);
            window.setScene(scene);
            window.showAndWait();
        });
    }

    // synchronized because we change all the stages to follow the new model, but it can't be updated
    // again during this action
    @Override
    public synchronized void propertyChange(PropertyChangeEvent event)
    {
        String eventName=event.getPropertyName();
        if("ModelModifications".equals(eventName))
        {
            ModelMessage message = (ModelMessage) event.getNewValue();
            // check if the message was an error message and if it's fatal
            if(message.errorStatus())
            {
                if(message.getErrorMessage().isFatal())
                {
                    if(game==null || !game.hasGameEnded())
                    {
                        AlertBox.display("Fatal error", message.getErrorMessage().getMessage());
                        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
                    }
                }
                else
                {
                    if(game.getCurrPlayerNickname().equals(nickName))
                        AlertBox.display("Error", message.getErrorMessage().getMessage());
                }
                return;
            }
            // if it's the first modelMessage put it in the game variable as it is, but
            // if there was already a game that must be updated, merge the differences
            if(game!=null)
            {
                List<Island> islandList = game.getIslandList();
                List<Cloud> cloudList = game.getCloudList();
                List<Player> playerList = game.getPlayerList();
                List<CharacterCard> characterCardList = game.getCharacterCardList();
                if(message.getIslandList().size() != 0)
                    islandList = message.getIslandList();
                if(message.getCloudList().size() != 0)
                    cloudList = message.getCloudList();
                if(message.getPlayerList().size() != 0) {
                    for (Player newPlayer : message.getPlayerList())
                        for (Player oldPlayer : playerList)
                            if (oldPlayer.getuID().equals(newPlayer.getuID()))
                                playerList.set(playerList.indexOf(oldPlayer), newPlayer);
                }
                if (message.getCharacterCardList().size() != 0) {
                    for (CharacterCard newCard : message.getCharacterCardList())
                        for (CharacterCard oldCard : characterCardList)
                            if (oldCard.getCardID() == newCard.getCardID())
                                characterCardList.set(characterCardList.indexOf(oldCard), newCard);
                }
                game = new ModelMessage(message.isExpertMode(), islandList, cloudList, playerList,
                        characterCardList, message.getCurrPlayerNickname(),
                        message.getUnusedCoins(), message.hasGameEnded(),
                        message.getPhase());
                if(game.hasGameEnded())
                    showWinner();
            }
            else
            {
                game = new ModelMessage(message.isExpertMode(), message.getIslandList(),
                        message.getCloudList(), message.getPlayerList(), message.getCharacterCardList(),
                        message.getCurrPlayerNickname(), message.getUnusedCoins(), message.hasGameEnded(),
                        message.getPhase());
            }
            updateStages();
        }
    }

    private synchronized void updateStages()
    {
        for(Showable stage: allStages)
            stage.show();
    }

    private synchronized void showWinner()
    {
        int maxScore=0;
        List<String> winners= new ArrayList<>();
        if(game==null)
            return;

        // calculating the number of towers with which a player start the game
        int mode= game.getPlayerList().size()%2;  // mode==0 => 8 towers
        // mode==1 => 6 towers
        final int maxTowers= (mode)*6 + (1-mode)*8;
        for(Player player: game.getPlayerList())
        {
            int score= 0;
            for(Colour c: Colour.values())
                if(player.checkTeacherPresence(c))
                    score++;
            score+= (maxTowers - player.getTowers().availabilityChecker())*10;
            if(score==maxScore)
                winners.add(player.getuID());
            else if(score>maxScore)
            {
                maxScore=score;
                winners.clear();
                winners.add(player.getuID());
            }
        }
        if(winners.size()>1)
        {
            String message= "The winners are: ";
            for(String winner: winners)
                message= message.concat(winner+"   ");
            AlertBox.display("Game ended", message);
        }
        else
            AlertBox.display("Game ended", "The winner is "+winners.get(0));
    }
}
