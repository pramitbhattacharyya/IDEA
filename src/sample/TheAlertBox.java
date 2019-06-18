package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TheAlertBox {

    public static void display(String title, String message, Image image,String ButtonText)
    {
        Stage window=new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        window.setMinHeight(100);

        Label label =new Label();
        label.setText(message);

        Button closeButton=new Button(ButtonText);
        closeButton.setOnAction(event -> window.close());

        VBox layout =new VBox(40);
        layout.getChildren().addAll(label,closeButton);
        layout.setAlignment(Pos.CENTER);

        HBox hBox=new HBox(40);
        hBox.setPadding(new Insets(20,20,20,20));
        if(image!=null)
            hBox.getChildren().addAll(new ImageView(image),layout);
        else
            hBox.getChildren().addAll(layout);

        Scene scene=new Scene(hBox);
        window.setScene(scene);
        window.showAndWait();
    }
}
