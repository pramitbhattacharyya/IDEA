package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox
{
    static boolean ans;

    public static boolean display(String title, String message)
    {
        Stage window=new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(350);
        window.setMinHeight(250);

        Label label =new Label();
        label.setText(message);

        Button yesbtn=new Button("Yes");
        Button nobtn=new Button("No");

        yesbtn.setOnAction(event -> {
            ans=true;
            window.close();
        });

        nobtn.setOnAction(event -> {
            ans=false;
            window.close();
        });

        HBox hBox=new HBox(40);
        hBox.getChildren().addAll(yesbtn,nobtn);
        hBox.setAlignment(Pos.CENTER);
        VBox layout =new VBox(30);
        layout.getChildren().addAll(label,hBox);
        layout.setAlignment(Pos.CENTER);

        Scene scene=new Scene(layout,350,250);
        window.setScene(scene);
        window.showAndWait();

        return ans;
    }
}
