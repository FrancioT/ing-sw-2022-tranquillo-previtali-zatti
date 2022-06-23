package it.polimi.ingsw.Client.GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

class ConfirmBox
{
    private static boolean answer;
    private static Stage window;

    /**
     * Private class used only as a showable object in order to close the ChooseColourBox when the game is closing
     */
    private static class FireClosing extends Showable
    {
        @Override
        public void show(){}

        @Override
        public void close()
        {
            ConfirmBox.close();
        }

        @Override
        public void pause() {}

        @Override
        public void resume() {}
    }
    private static FireClosing closingChecker;

    /**
     * Display a window which asks a choice between yes and no, stops the calling window until this one is closed
     * @param title title of the window
     * @param message text inside the window
     * @return the answer of the user: true if yes, false if no
     */
    static boolean display(String title, String message)
    {
        setClosingChecker();
        answer=false;
        window= new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(350);
        window.setMinHeight(200);

        Text text= new Text(message);
        text.setFont(Font.font(null, FontWeight.NORMAL, 14));
        Button yesButton= new Button("yes");
        yesButton.setOnAction(event -> {
            window.close();
            answer=true;
        });
        Button noButton= new Button("no");
        noButton.setOnAction(event -> {
            window.close();
            answer=false;
        });
        VBox layout= new VBox(20);
        layout.setPadding(new Insets(15, 15, 15, 15));
        HBox buttons= new HBox(20);
        buttons.getChildren().addAll(yesButton, noButton);
        buttons.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(text, buttons);
        layout.setAlignment(Pos.CENTER);

        Scene scene= new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        removeClosingChecker();
        return answer;
    }

    /**
     * Close the pop-up window created when was calling the methods multipleColours or singleColour
     */
    private static void close()
    {
        if(window!=null)
            window.close();
    }

    /**
     * Creates a Showable object and adds it to the showable list of the GUI instance
     */
    private static void setClosingChecker()
    {
        closingChecker= new FireClosing();
        GUI.getInstance().addShowableStage(closingChecker);
    }

    /**
     * Removes the Showable object created and added in the GUI instance
     */
    private static void removeClosingChecker()
    {
        if(closingChecker!=null)
        {
            GUI.getInstance().removeShowableStage(closingChecker);
            closingChecker=null;
        }
    }
}
