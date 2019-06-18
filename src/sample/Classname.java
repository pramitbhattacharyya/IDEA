package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.Iterator;

public class Classname {
    static Boolean flag=false;
    public static String display(ChoiceBox<String> choiceBox, TabPane tabpane)
    {

        Stage window=new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Create New File");
        window.setMinWidth(250);
        window.setMinHeight(100);

        Label label =new Label();
        label.setText("Write the name of the file and choose the file type.");

        TextField textField=new TextField();

        Button closeButton=new Button("Done");
//        String str=textField.getText().trim()+getExtention(choiceBox.getValue());
//        if(isPresent(tabpane,str)){
//            label.setText("Duplicate file name");
//            closeButton.setDisable(true);
//            closeButton.setVisible(false);
//        }
//        else
//        {
//            label.setText("Write the name of the file and choose the file type.");
//            closeButton.setDisable(false);
//            closeButton.setVisible(true);
//        }
        closeButton.setOnAction(event -> {
            if(!textField.getText().equals("")){
                String str=textField.getText().trim()+getExtention(choiceBox.getValue());
                System.out.println(str);
                if(!isPresent(tabpane,str))
                {
                    flag=true;
                    window.close();
                }
                else{
                    label.setText("Duplicate file name. Filename already exists.");
                }
            }
            else
                label.setText("File Name cannot be blank.");
        });


        VBox layout =new VBox(40);
        layout.setPadding(new Insets(20,20,20,20));
        layout.getChildren().addAll(label,textField,choiceBox,closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene=new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        if(flag){
            return textField.getText()+"\n"+getExtention(choiceBox.getValue());
        }
        return "";
    }
    public static Boolean isPresent(TabPane tabpane, String str){
        Iterator<Tab> tabIterator=tabpane.getTabs().iterator();
//        for(int i=0;i<tabarr.length;i++){
//            System.out.println(tabarr[i].toString());
//            if(tabarr[i].toString().equals(str))
//                return true;
//        }
        Tab temp;
        while (tabIterator.hasNext()){
            temp=tabIterator.next();
            if(temp.getText()!=null && temp.getText().equals(str))
                return true;
        }
        return  false;
    }
    public static String getExtention(String str){
//        {"Java","Python","C","C++","Shell Script"};
//        String str=getChoice(choiceBox);
        switch (str){
            case "Java":
                return (".java");
            case "Python":
                return (".py");
            case "C":
                return (".c");
            case "C++":
                return (".cpp");
            case "Shell Script":
                return (".sh");
        }
        return ".txt";
    }

}
