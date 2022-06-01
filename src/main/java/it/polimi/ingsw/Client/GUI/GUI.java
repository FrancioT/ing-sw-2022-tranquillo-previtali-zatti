package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.CLI.Receiver;
import it.polimi.ingsw.ClientsHandler.Messages.ModelMessage;
import it.polimi.ingsw.Model.CharacterCard.CharacterCard;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Model.Player;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.image.Image;
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
    private final List<Stage> allStages;
    private ModelMessage game;
    private Receiver receiver;
    private Stage window;


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

    public void addStage(Stage newStage)
    {
        allStages.add(newStage);
    }

    public Stage getWindow() { return window; }

    public ModelMessage getModel() { return game; }

    @Override
    public void propertyChange(PropertyChangeEvent event)
    {
        /*String eventName=event.getPropertyName();
        if("ModelModifications".equals(eventName))
        {
            ModelMessage message = (ModelMessage) event.getNewValue();
            if(message.errorStatus())
            {
                if(message.getErrorMessage().isFatal())
                {
                    if(!gameEnded)
                    {
                        System.out.println(message.getErrorMessage().getMessage());
                        errorFlag= true;
                    }
                    this.interrupt();
                }
                else
                {
                    synchronized(gameLock)
                    {
                        if(game.orElse(null).getCurrPlayerNickname().equals(nickName))
                            System.out.println(message.getErrorMessage().getMessage());
                    }
                }
                return;
            }
            synchronized(gameLock)
            {
                if (game.isPresent()) {
                    List<Island> islandList = game.orElse(null).getIslandList();
                    List<Cloud> cloudList = game.orElse(null).getCloudList();
                    List<Player> playerList = game.orElse(null).getPlayerList();
                    List<CharacterCard> characterCardList = game.orElse(null).getCharacterCardList();
                    if (message.getIslandList().size() != 0)
                        islandList = message.getIslandList();
                    if (message.getCloudList().size() != 0)
                        cloudList = message.getCloudList();
                    if (message.getPlayerList().size() != 0) {
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
                    game = Optional.of(new ModelMessage(message.isExpertMode(), islandList, cloudList, playerList,
                            characterCardList, message.getCurrPlayerNickname(),
                            message.getUnusedCoins(), message.hasGameEnded(),
                            message.getPhase()));
                    gameEnded=message.hasGameEnded();
                } else {
                    game = Optional.of(new ModelMessage(message.isExpertMode(), message.getIslandList(),
                            message.getCloudList(), message.getPlayerList(), message.getCharacterCardList(),
                            message.getCurrPlayerNickname(), message.getUnusedCoins(), message.hasGameEnded(),
                            message.getPhase()));
                }
                printGame();
                gameLock.notifyAll();
            }
        }*/
    }
}
