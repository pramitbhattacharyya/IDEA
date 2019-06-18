package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.*;

public class SaveAs {
    static Boolean flag=false;
    public static void display(ChoiceBox<String> choiceBox, String content)
    {

        Stage window=new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Save As File");
        window.setMinWidth(250);
        window.setMinHeight(100);

        Label label =new Label();
        label.setText("Write the path where you want to save the file");

        TextField textField=new TextField();

        TextField path=new TextField();
        path.setPromptText("Set the absolute path");

        Button closeButton=new Button("Save");
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
            if(!(textField.getText().equals("")||path.getText().equals(""))){
                String str=path.getText().trim();
                str+=str.endsWith("/")?"":"/";
                File filepath=new File(str);
                str+=textField.getText().trim()+getExtention(choiceBox.getValue());
                System.out.println(str);
                File file =new File(str);
                if(filepath.exists()) {
                    if (file.exists() && file.isFile()) {
                        if (!(label.getText().equals("File Already Exists. Press the button again if you want to overwrite it."))) {
                            label.setText("File Already Exists. Press the button again if you want to overwrite it.");
                            TheAlertBox.display("OVERWRITE ALERT", "File Already Exists. Press Save Again To Overwrite.", null, "OK");
                        } else if (file.canWrite()) {
                            writeFile(file, content);
                            window.close();
                        } else {
                            TheAlertBox.display("PERMISSION DENIED", "File Writting Permission NOT Granted", null, "OK");
                        }
                    } else {
                        writeFile(file, content);
                        window.close();
                    }
                }
                else
                    TheAlertBox.display("DIRECTORY MISSING", "No such path exists.", null, "OK");
            }
            else
                label.setText("File Name cannot be blank.");
        });

        VBox layout =new VBox(40);
        layout.setPadding(new Insets(20,20,20,20));
        layout.getChildren().addAll(label,path,textField,choiceBox,closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene=new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public static void writeFile(File file, String content) {
        try {
            FileWriter fileWriter=new FileWriter(file);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
