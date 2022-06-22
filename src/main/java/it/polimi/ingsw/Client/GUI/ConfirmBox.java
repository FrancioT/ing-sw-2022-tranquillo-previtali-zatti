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

    /**
     * Display a window which asks a choice between yes and no, stops the calling window until this one is closed
     * @param title title of the window
     * @param message text inside the window
     * @return the answer of the user: true if yes, false if no
     */
    static boolean display(String title, String message)
    {
        answer=false;
        Stage window;
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
        return answer;
    }
}
