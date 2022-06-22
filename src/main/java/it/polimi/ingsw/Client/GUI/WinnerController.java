package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.ClientsHandler.Messages.ModelMessage;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Player;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class WinnerController extends Showable
{

    private ModelMessage game;
    private static Stage window;
    @FXML
    private Text winner1, winner2, winner3;
    private final List<Text> winnersText= new ArrayList<>();

    public static void setWindow(Stage winnerWindow)
    {
        window= winnerWindow;
    }

    /**
     * This method calculate the winner and display his name in the last game's screen
     */
    private synchronized void showWinner()
    {
        int maxScore=0;
        List<String> winners= new ArrayList<>();

        winnersText.add(winner1);
        winnersText.add(winner2);
        winnersText.add(winner3);

        for(Text tx: winnersText)
        {
            tx.setVisible(false);
        }

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

        if(winners.size()==4)
        {
            winnersText.get(0).setText("Both teams have won");
            winnersText.get(0).setVisible(true);
        }
        else
        {
            for(int j=0; j<winners.size(); j++)
            {
                winnersText.get(j).setText(winners.get(j));
                winnersText.get(j).setVisible(true);
            }
        }
    }

    /**
     * This method shows and updates the Winner screen
     */
    @Override
    void show()
    {
        game= GUI.getInstance().getModel();
        window.setOnCloseRequest(e -> close());
        showWinner();
    }

    @Override
    public void close()
    {
        if(window!=null)
        {
            GUI.getInstance().closeAllWindows();
            window.close();
        }
    }

    @Override
    public void pause(){}

    @Override
    public void resume(){}
}
