package sample;

// Java program to create multiple tabs and
// add it to the tabPane and also create a
// tab which on selected will create new tabs
import javafx.application.Application;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.event.Event;
import javafx.event.EventHandler;

public class Tabpane extends Application {

    // counter of tabs
    int counter = 0;

    // launch the application
    public void start(Stage stage)
    {

        // set title for the stage
        stage.setTitle("Creating Tab");

        // create a tabpane
        TabPane tabpane = new TabPane();

        for (int i = 0; i < 5; i++) {

            // create Tab
            Tab tab = new Tab("Tab_" + (int)(counter + 1));

            // create a label
            Label label = new Label("This is Tab: "
                    + (int)(counter + 1));

            counter++;

            // add label to the tab
            tab.setContent(label);

            // add tab
            tabpane.getTabs().add(tab);
        }

        // create a tab which
        // when pressed creates a new tab
        Tab newtab = new Tab();

        // action event
        EventHandler<Event> event =
                new EventHandler<Event>() {

                    public void handle(Event e)
                    {
                        if (newtab.isSelected())
                        {

                            // create Tab
                            Tab tab = new Tab("Tab_" + (int)(counter + 1));

                            // create a label
                            Label label = new Label("This is Tab: "
                                    + (int)(counter + 1));

                            counter++;

                            // add label to the tab
                            tab.setContent(label);

                            // add tab
                            tabpane.getTabs().add(
                                    tabpane.getTabs().size() - 1, tab);

                            // select the last tab
                            tabpane.getSelectionModel().select(
                                    tabpane.getTabs().size() - 2);
                        }
                    }
                };

        // set event handler to the tab
        newtab.setOnSelectionChanged(event);

        // add newtab
        tabpane.getTabs().add(newtab);
//        tabpane.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
//        tabpane.setSide(Side.LEFT);

        // create a scene
        Scene scene = new Scene(tabpane, 600, 500);

        // set the scene
        stage.setScene(scene);

        stage.show();
    }

    // Main Method
    public static void main(String args[])
    {

        // launch the application
        launch(args);
    }
}