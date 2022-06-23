package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Model.Colour;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

class ChooseColourBox
{
    private static final ImageView redS = new ImageView("resizedRedStudent.png");
    private static final ImageView pinkS = new ImageView("resizedPinkStudent.png");
    private static final ImageView greenS = new ImageView("resizedGreenStudent.png");
    private static final ImageView yellowS = new ImageView("resizedYellowStudent.png");
    private static final ImageView blueS = new ImageView("resizedBlueStudent.png");
    private static Colour choice;
    private static List<Colour> multipleChoice;
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
            ChooseColourBox.close();
        }

        @Override
        public void pause() {}

        @Override
        public void resume() {}
    }
    private static FireClosing closingChecker;

    /**
     * Display a window which allow the user to choose one of the 5 student's colours
     * @param title the title of the window
     * @param message the text inside the window
     * @return the colour selected by the user
     */
    static Colour singleColour(String title, String message)
    {
        setClosingChecker();
        setImageSize();
        choice= null;
        window= new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(300);
        window.setMinHeight(200);

        Text text= new Text(message);
        text.setFont(Font.font(null, FontWeight.NORMAL, 14));
        VBox layout= new VBox(20);
        layout.setPadding(new Insets(15, 15, 15, 15));
        HBox students= new HBox(20);
        students.getChildren().addAll(redS, pinkS, greenS, yellowS, blueS);
        students.setAlignment(Pos.CENTER);
        redS.setOnMouseClicked(event -> {
            choice= Colour.red;
            window.close();
        });
        pinkS.setOnMouseClicked(event -> {
            choice= Colour.pink;
            window.close();
        });
        greenS.setOnMouseClicked(event -> {
            choice= Colour.green;
            window.close();
        });
        yellowS.setOnMouseClicked(event -> {
            choice= Colour.yellow;
            window.close();
        });
        blueS.setOnMouseClicked(event -> {
            choice= Colour.blue;
            window.close();
        });
        layout.getChildren().addAll(text, students);
        layout.setAlignment(Pos.CENTER);

        Scene scene= new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        removeClosingChecker();
        return choice;
    }

    /**
     * Display a window which allow the user to choose between the 5 student's colours multiple times
     * @param title the title of the window
     * @param message the text inside the window
     * @return a list of the colour selected
     */
    static List<Colour> multipleColours(String title, String message)
    {
        setClosingChecker();
        setImageSize();
        multipleChoice= new ArrayList<>();
        window= new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(300);
        window.setMinHeight(200);

        Text text= new Text(message);
        text.setFont(Font.font(null, FontWeight.BOLD, 14));
        VBox layout= new VBox(20);
        layout.setPadding(new Insets(15, 15, 15, 15));
        HBox students= new HBox(20);
        Label redCounter= new Label("0");
        VBox redStud= new VBox(10, redS, redCounter);
        Label pinkCounter= new Label("0");
        VBox pinkStud= new VBox(10, pinkS, pinkCounter);
        Label greenCounter= new Label("0");
        VBox greenStud= new VBox(10, greenS, greenCounter);
        Label yellowCounter= new Label("0");
        VBox yellowStud= new VBox(10, yellowS, yellowCounter);
        Label blueCounter= new Label("0");
        VBox blueStud= new VBox(10, blueS, blueCounter);
        students.getChildren().addAll(redStud, pinkStud, greenStud, yellowStud, blueStud);
        students.setAlignment(Pos.CENTER);
        redS.setOnMouseClicked(event -> {
            multipleChoice.add(Colour.red);
            redCounter.setText(Integer.toString(Integer.parseInt(redCounter.getText())+1));
        });
        pinkS.setOnMouseClicked(event -> {
            multipleChoice.add(Colour.pink);
            pinkCounter.setText(Integer.toString(Integer.parseInt(pinkCounter.getText())+1));
        });
        greenS.setOnMouseClicked(event -> {
            multipleChoice.add(Colour.green);
            greenCounter.setText(Integer.toString(Integer.parseInt(greenCounter.getText())+1));
        });
        yellowS.setOnMouseClicked(event -> {
            multipleChoice.add(Colour.yellow);
            yellowCounter.setText(Integer.toString(Integer.parseInt(yellowCounter.getText())+1));
        });
        blueS.setOnMouseClicked(event -> {
            multipleChoice.add(Colour.blue);
            blueCounter.setText(Integer.toString(Integer.parseInt(blueCounter.getText())+1));
        });

        Button button= new Button("Done");
        button.setOnAction(event -> window.close());
        layout.getChildren().addAll(text, students, button);
        layout.setAlignment(Pos.CENTER);

        Scene scene= new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        removeClosingChecker();
        return multipleChoice;
    }

    /**
     * Close the pop-up window created when was calling the methods multipleColours or singleColour
     */
    private static void close()
    {
        multipleChoice=null;
        choice=null;
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

    private static void setImageSize()
    {
        redS.setFitWidth(60);
        redS.setFitHeight(60);
        pinkS.setFitWidth(60);
        pinkS.setFitHeight(60);
        greenS.setFitWidth(60);
        greenS.setFitHeight(60);
        yellowS.setFitWidth(60);
        yellowS.setFitHeight(60);
        blueS.setFitWidth(60);
        blueS.setFitHeight(60);
    }
}
