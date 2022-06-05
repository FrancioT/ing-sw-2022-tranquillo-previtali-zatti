package it.polimi.ingsw.Client.GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

class AlertBox
{
    static void display(String title, String message)
    {
        Stage window;
        try {
            window= new Stage();
        }catch(IllegalStateException e) { return; }
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        window.setMinHeight(200);

        Text text= new Text(message);
        text.setFont(Font.font(null, FontWeight.BOLD, 14));
        Button button= new Button("Ok");
        button.setOnAction(event -> window.close());
        VBox layout= new VBox(20);
        layout.setPadding(new Insets(15, 15, 15, 15));
        layout.getChildren().addAll(text, button);
        layout.setAlignment(Pos.CENTER);

        Scene scene= new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
