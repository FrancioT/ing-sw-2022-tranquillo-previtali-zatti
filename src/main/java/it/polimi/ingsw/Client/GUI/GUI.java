package it.polimi.ingsw.Client.GUI;

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
    private final Image icon;
    private static GUI instance= null;
    private final List<Showable> allStages;
    private ModelMessage game;
    private ReceiverGui receiver;
    private Stage window;
    private String nickName;
    private boolean gameClosed;


    public static void execute(String[] args)
    {
        launch(args);
    }

    /**
     * Method to have access to the instance of a GUI, if it is null it waits until stimulated and creates a new one;
     * if it is not null, it returns the existing one
     * @return a new Instance or an existing one
     */
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

    /**
     * Constructor of GUI, if another instance already exists it throws an error
     */
    public GUI()
    {
        icon= new Image("icon.png");
        gameClosed=false;
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

    /**
     * Method to start the GUI; the first page it will show is with the title, the request of name and Server IP
     * @param primaryStage the stage set with the info we need to show
     */
    @Override
    public void start(Stage primaryStage)
    {
        synchronized(instanceLock) {
            instance= this;
            instanceLock.notifyAll();
        }
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

    /**
     * Method to create the receiver for the GUI, which will receive and analyze the messages from the server
     * @param serverConnection the socket for the connection with the server
     * @return true if correctly created or false if an error occurs
     */
    public synchronized boolean setReceiver(Socket serverConnection)
    {
        try {
            receiver= new ReceiverGui(serverConnection);
            receiver.addPropertyChangeListener(this);
            return true;
        }catch(IOException e) {
            return false;
        }
    }

    public synchronized ReceiverGui getReceiver()
    {
        return  receiver;
    }

    public synchronized void setNickName(String nickName)
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

    public synchronized String getNickName()
    {
        return nickName;
    }

    /**
     * Method that is called when a player tries to close the game manually. It will display a pop-up which asks
     * whether he really wants to quit the game and shows him 2 buttons; one will close the game, the other one will
     * make the player return to the main dashboard
     */
    public synchronized void setClosingWindow()
    {
        window.setOnCloseRequest(event -> {
            //creates the pop-up which can end the game
            event.consume();  // consume the main closing window event
            Stage popUp= new Stage();
            popUp.initModality(Modality.APPLICATION_MODAL);
            popUp.setTitle("Closing window");
            popUp.setMinWidth(350);
            popUp.setMinHeight(300);

            //description of the pop-up
            Label text= new Label("Do you want to close the game?");

            //if "yes", close the receiver and the game
            Button yesButton= new Button("Yes");
            yesButton.setOnAction(ev -> {
                popUp.close();
                try{ receiver.close(); }catch(IOException ignored){}
                closeAllWindows();
                gameClosed=true;
            });
            //if "no", return to game
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
        });
    }

    /**
     * Close all the open stages and window
     */
    public void closeAllWindows()
    {
        for(Showable stage: allStages)
            stage.close();
        window.close();
    }

    // synchronized because we change all the stages to follow the new model, but it can't be updated
    // again during this action
    @Override
    public synchronized void propertyChange(PropertyChangeEvent event)
    {
        if(gameClosed)
            return;
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
                        closeAllWindows();
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
                Parent islandView;
                FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("islandsGameScreen.fxml"));
                try{ islandView= loader.load(); }
                catch (IOException e1){ throw new RuntimeException(e1.getMessage()); }
                ((IslandController)loader.getController()).initialize();
                window.close();
                window= new Stage();
                window.setTitle("Eriantys");
                window.setScene(new Scene(islandView));
                window.getIcons().add(icon);
                window.setResizable(false);
                setClosingWindow();
                window.show();
                addShowableStage(loader.getController());
            }
            updateStages();
        }
    }

    private synchronized void updateStages()
    {
        for(Showable stage: allStages)
            stage.show();
    }

    /**
     * Method to find and print the winner/s
     */
    private synchronized void showWinner()
    {
        int maxScore=0;
        List<String> winners= new ArrayList<>();
        if(game==null)
            return;

        // calculating the number of towers with which a player start the game
        int mode= game.getPlayerList().size()%2;    // mode==0 => 8 towers
                                                    // mode==1 => 6 towers
        final int maxTowers= (mode)*6 + (1-mode)*8;

        // for loop to calculate the "score" of every player, based on the number of towers that he still has in the
        // dashboard and on the teachers he owns
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
        //in case there is a draw
        if(winners.size()>1)
        {
            String message= "The winners are: ";
            for(String winner: winners)
                message= message.concat(winner+"   ");
            AlertBox.display("Game ended", message);
        }
        else
            AlertBox.display("Game ended", "The winner is "+winners.get(0));
        closeAllWindows();
    }
}
