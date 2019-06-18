package sample;
//
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.nio.file.Path;
import java.util.Iterator;

public class Main extends Application {

//    BorderPane layout;
//    Button btn;
    static int total_num_checkbox=0;
//    static int tab_Count=0;
    Stage window;
    ChoiceBox<String> choiceBox;
    TextArea textArea;
//    TreeItem<String>treenode[];
    ComboBox<String> comboBox;
    TabPane srcfile_list;
    TreeView<String>diretory_struct;
    TreeItem<String>sample;
    String path=System.getProperty("user.home")+"/CrazyDevelopersProjects/src/sample/";
    String inputfileset=path+"inputfile.txt";
//    File output=new File(path+"output.txt");
    Label inputfilename;
    TextArea execArea;
    int TIMELIMIT=5000;
    @Override
    public void start(Stage primaryStage) throws Exception{
        window=primaryStage;
        Parent proot = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("CRAZY DEVELOPERS IDEA");
        primaryStage.setOnCloseRequest(
                event -> {
                    event.consume();
                    if(closeWindow())
                        primaryStage.close();
                });
        String []Choice={"Java","Python","C","C++","Shell Script","text"};
        choiceBox= createChoiceBox(Choice,0);
        MenuBar menuBar=createMenuBar();
        String []executables={"Main.java"};
        comboBox=createComboBox(executables);
        String []htabs_title={"Main.java"};
        String []htabs_content={"// Created a new Java file."};
        Label pathlabel=new Label(path);
        textArea=new TextArea();
        //check this out functions of textfield.
        textArea.setMinSize(550,550);
//        textArea.setPrefSize(200,600);
//        ScrollPane scrollPane=new ScrollPane();
//        scrollPane.setMinSize(200,500);
//        scrollPane.setContent(textArea);
        textArea.setEditable(true);
        ImageView icon=new ImageView(new Image(getClass().getResourceAsStream("/Icons/Play.jpg")));
        icon.setFitWidth(20); icon.setFitHeight(20);

        inputfilename=new Label(inputfileset);
        TextArea inputArea=new TextArea();
        //check this out functions of textfield.
        inputArea.setMinSize(550,400);
        inputArea.setText("Here the output will be displayed.");
        inputArea.setEditable(true);
        Button chooseInputfile=new Button("Choose Input File");
        chooseInputfile.setOnAction(event -> setInputFile());
        Button startProcess=new Button("Start");// button click event added below.
        VBox input_vBox=new VBox(10);
        input_vBox.getChildren().addAll(inputArea,startProcess,chooseInputfile,inputfilename);
//        StackPane run_layout=new StackPane(run_vBox,goBack);
        input_vBox.setAlignment(Pos.CENTER);
        Scene input_scene=new Scene(input_vBox,1200,500);

        execArea=new TextArea();
        //check this out functions of textfield.
        execArea.setMinSize(550,400);
        execArea.setText("Here the output will be displayed.");
        execArea.setEditable(true);

        Button checkAns=new Button("Check My Answer");
        checkAns.setOnAction(event -> checkAnswer());
        Button goBack=new Button("Go Back");// button click event added below.
        VBox run_vBox=new VBox(10);
        run_vBox.getChildren().addAll(execArea,goBack,checkAns);
//        StackPane run_layout=new StackPane(run_vBox,goBack);
        run_vBox.setAlignment(Pos.CENTER);
        Scene run_scene=new Scene(run_vBox,1200,500);
        icon.setOnMouseClicked(event ->  {
            saveContent(srcfile_list.getSelectionModel().getSelectedItem());
            inputArea.setText(readFile(inputfileset));
            primaryStage.setScene(input_scene);
        });

//        String []nodename={".idea","out","src","production","sample","Main.class","Main.java"};
        TreeItem<String> root;
        root=new TreeItem<>("IDEA");
        root.setExpanded(true);
        diretory_struct=new TreeView<>(root);
        diretory_struct.getSelectionModel().selectedItemProperty()
                .addListener((v,oldval,newval) ->{
                    if(newval!=null && newval.isLeaf())
                        selectTab(newval,newval.getValue());
                });
        TreeItem<String> src=makeBranch("src",root);
        src.setExpanded(true);
        sample=makeBranch("sample",src);
        sample.setExpanded(true);
        File dir=new File(path);
        String javastr=getContent(".java");
        File inputfile=new File(path+"inputfile.txt");
        File outorg=new File(path+"output_original.txt");
        File mainFile=new File(path+"Main.java");
        if(!(dir.exists()&&dir.isDirectory())){
            if(!dir.mkdirs())
            {
                createAlertDialogBox("PERMISSION DENIED","The required home directory cannot be created.","PermissionDenied.png");
                System.exit(0);
            }
//            String []nodename={"src","sample","Main.java"};
//            int []nodenumber={1,1,1};
            writeFile(inputfile,"Hello Crazy Developers");
            writeFile(outorg,"Hello Crazy Developers");
            writeFile(mainFile,javastr);
            listFilesForFolder(dir, sample);
//            treenode=new TreeItem[7];
//            diretory_struct=createTree("IDEA",treenode,nodename,nodenumber);
            textArea.setText(javastr);
            srcfile_list=createTabs(htabs_title,htabs_content,1,textArea,false,true);
        }
        else {
//            String []nodename={"src","sample"};
//            int []nodenumber={1,1,0};
//            treenode=new TreeItem[7];
//            diretory_struct=createTree("IDEA",treenode,nodename,nodenumber);
            if(!(inputfile.exists()&&inputfile.isFile()&&inputfile.canRead())) {
                writeFile(inputfile,"Hello Crazy Developers");
            }
            if(!(outorg.exists()&&outorg.isFile()&&outorg.canRead())) {
                writeFile(outorg,"Hello Crazy Developers");
            }
            if(mainFile.exists()&&mainFile.isFile()&&mainFile.canRead())
            {
                try {
//                    BufferedReader readmainfile = new BufferedReader(new InputStreamReader(new FileInputStream(mainFile)));
//                    String line;
//                    StringBuilder sb=new StringBuilder() ;
//                    while ((line=readmainfile.readLine())!=null)
//                        sb.append(line+"\n");
                    textArea.setText(readFile(path+"Main.java"));
                    srcfile_list=createTabs(htabs_title,htabs_content,1,textArea,false,true);
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Error: "+e);
                }
            }
            else {
                writeFile(mainFile,javastr);
                textArea.setText(javastr);
                srcfile_list = createTabs(htabs_title, htabs_content, 1, textArea, false, true);
            }
            listFilesForFolder(dir, sample);
        }
        String []vtabs_title={"1:Project","2:Favourites","Learn","7:Structures"};
        String []vtabs_content={"Project Tabs.","Favourites Tabs.","Learn Tabs","Structures Tabs"};
        TabPane easeaccess_list=createTabs(vtabs_title,vtabs_content,4,diretory_struct,false,false);
        easeaccess_list.setSide(Side.LEFT);
        String []vtabs_title_right={"Ant Build","Maven","Mongo Explorer"};
        String []vtabs_content_right={"","",""};
        TabPane config_list=createTabs(vtabs_title_right,vtabs_content_right,3,null,false,false);
        config_list.setSide(Side.RIGHT);
        String []bottombar_title={"4:Run","6:TODO","Event Log","0:Messages","Terminal"};
        String []bottombar_content={"","","","",""};
        TabPane bottombar=createTabs(bottombar_title,bottombar_content,5,null,true,false);
//        comboBox.setLayoutX(300);

//        name.setStyle("-fx-text-inner-color : red;");

//        CheckBox checkBox[]=new CheckBox[2];
//        checkBox[0]=getCheckBox("Book",false);
//        checkBox[1]=getCheckBox("Food",true);
//        ListView<String> listView=createList(Choice);
//        TableView<Product> table=createTable();
//        btn=new Button("Click Me");
//        btn.setOnAction(event ->
//                createAlertDialogBox("Congratulations..!","All Correct","HappyFace.png")
////                buttonClicked(listView)
////                getChoice(choiceBox)
////                addItemtoComboBox(comboBox,"Need For Speed")
////                handleOptions(checkBox)
//                );
//        TextField Menuname=new TextField();
//        Menuname.setPromptText("Give Name of Menu Item");

//        HBox rightpos=new HBox(2);
//        rightpos.getChildren().add(comboBox);
//        HBox execbar=new HBox(2);
//        execbar.getChildren().addAll(choiceBox,comboBox);
//        execbar.setAlignment(Pos.TOP_RIGHT);
//        execbar.setMinWidth(1366);
//        VBox disp=new VBox(2);
//        disp.getChildren().addAll(execbar);
//        disp.setPadding(new Insets(0,10,10,10));

        GridPane grid=new GridPane();
        grid.setPadding(new Insets(2,0,10,0));
        grid.setVgap(5);
        grid.setHgap(5);

//        GridPane.setConstraints(choiceBox,1,1);
        GridPane.setConstraints(comboBox,200,0,30,1);
//        GridPane.setConstraints(btn,100,0,10,1);
        GridPane.setConstraints(icon,231,0,1,1);
        GridPane.setConstraints(srcfile_list,53,1,199,124);
        GridPane.setConstraints(easeaccess_list,0,1,52,124);
        GridPane.setConstraints(config_list,249,1,10,124);
        GridPane.setConstraints(bottombar,5,124,260,10);
        GridPane.setConstraints(pathlabel,1,0,50,1);
//        GridPane.setConstraints(choiceBox,100,0,30,1);
//        GridPane.setConstraints(listView,5,5);
//        GridPane.setConstraints(table,5,5);
//        GridPane.setConstraints(diretory_struct,5,5);
        grid.getChildren().addAll(comboBox,srcfile_list,easeaccess_list,config_list,bottombar,icon,pathlabel);
//        grid.getChildren().addAll(table);
//        grid.getChildren().addAll(diretory_struct);

        VBox vBox=new VBox(2);
        vBox.getChildren().addAll(menuBar,grid);

//        for (CheckBox chk:checkBox)
//            vBox.getChildren().add(chk);

        BorderPane layout=new BorderPane();
        layout.setTop(vBox);
//        layout.setCenter(btn);
        Scene scene=new Scene(layout, 1366, 768);
        primaryStage.setScene(scene);
        startProcess.setOnAction(event -> {
            if(inputfilename.getText().equals(path+"inputfile.txt"))
                writeFile(inputfile,inputArea.getText());
            long time=System.currentTimeMillis();
            MyThread thread=new MyThread();
            primaryStage.setScene(run_scene);
            try {
//                Thread.sleep(TIMELIMIT);
                while (System.currentTimeMillis()-time<=TIMELIMIT&&thread.t.isAlive());
                if(thread.t.isAlive())
                {
                    thread.t.interrupt();
                    System.out.println("Thread is killed");
                    createAlertDialogBox("RUNTIME ERROR","TIME LIMIT EXCEEDED","Runtime.png");
//                    execArea.setText("\nTIME LIMIT EXCEEDED");
                }
            } catch (Exception e) {
                System.out.println("Caught "+e);
                e.printStackTrace();
            }
//            execArea.setText(execute(false));
        });
        goBack.setOnAction(event -> primaryStage.setScene(scene));
        primaryStage.show();
    }

    public void checkAnswer() {
//        writeFile(output,execArea.getText());
        String orig=readFile(path+"output_original.txt");
        System.out.println("\""+orig+"\"");
        System.out.println("\""+execArea.getText()+"\"");

        if(orig.equals(execArea.getText())){
            System.out.println("Right Answer");
            createAlertDialogBox("Congratulations..!","All Correct","HappyFace.png");
        }
        else{
            System.out.println("Wrong Answer");
            createAlertDialogBox("Better Luck Next Time...!","Wrong Answer","SadFace.png");
        }
    }

    public void setInputFile() {
        FileChooser fileChooser=new FileChooser();
        fileChooser.setInitialDirectory(new File(path));
//        fileChooser.getExtensionFilters().addAll(
//                new FileChooser.ExtensionFilter("JAVA Files","*.java"));
        File selectedfile=fileChooser.showOpenDialog(null);
        if(selectedfile!=null) {
            inputfileset = selectedfile.getAbsolutePath();
            inputfilename.setText(inputfileset);
        }
    }

    public  MenuBar createMenuBar(){
        Menu filemenu=new Menu("File");
        //Menu items
        MenuItem newFile=new MenuItem("New...");
        newFile.setOnAction(event -> {
            System.out.println("Creating a new file.");
            createNew();
        });
        filemenu.getItems().add(newFile);

        MenuItem openItem=putinMenu("Open...","Open a previously created file",filemenu);
        openItem.setOnAction(event -> openFile());
        MenuItem saveItem=putinMenu("Save...","Save the current file",filemenu);
        saveItem.setOnAction(event -> saveContent(srcfile_list.getSelectionModel().getSelectedItem()));
        MenuItem saveAsItem=putinMenu("Save as...","Save the current file",filemenu);
        saveAsItem.setOnAction(event -> saveAs());
        filemenu.getItems().add(new SeparatorMenuItem());
        MenuItem settings=putinMenu("Settings...","Open Settings",filemenu);
        filemenu.getItems().add(new SeparatorMenuItem());
        MenuItem exit=putinMenu("Exit...","Exit",filemenu);
        exit.setOnAction(event -> {
            if(closeWindow())
                window.close();
        });

        //Main menu bar'
        // Edit Menu
        Menu editMenu=new Menu("_Edit");// _ is given before the first letter
//        so that the letter appears underlined in the menu and can be accessed using short-cut.
        MenuItem []editItems=new MenuItem[3];
        editItems[0]=putinMenu("Cut","Cut the selected portion",editMenu);
        editItems[1]=putinMenu("Copy","Copy the selected portion",editMenu);
        editItems[2]=putinMenu("Paste","Paste the item in clipboard",editMenu);
        editItems[2].setDisable(true);// to disable a menu item

        //Help Menu
        Menu helpMenu=new Menu("Help");
        CheckMenuItem showLines=new CheckMenuItem("Show Line Numbers");
        showLines.setOnAction(event -> {
            if(showLines.isSelected())
                System.out.println("Program will now show line numbers");
            else
                System.out.println("Hiding line numbers");
        });

//        Difficulty RadioMenuItems
        Menu difficultyMenu=new Menu("Difficulty");
        ToggleGroup diffToggle=new ToggleGroup();
        RadioMenuItem easy=new RadioMenuItem("Easy");
        RadioMenuItem medium=new RadioMenuItem("Medium");
        RadioMenuItem hard=new RadioMenuItem("Hard");
        easy.setToggleGroup(diffToggle);
        medium.setToggleGroup(diffToggle);
        medium.setSelected(true);
        hard.setToggleGroup(diffToggle);

        difficultyMenu.getItems().addAll(easy,medium,hard);

        CheckMenuItem autosave=new CheckMenuItem("Enable Autosave");
        autosave.setSelected(true);
        helpMenu.getItems().addAll(showLines,autosave);

        MenuBar menuBar=new MenuBar();
        menuBar.getMenus().addAll(filemenu,editMenu,helpMenu,difficultyMenu);
        return  menuBar;
    }

    public void saveAs() {
//        SaveAs.display(choiceBox,((TextArea)srcfile_list.getSelectionModel().getSelectedItem().getContent()).getText());
        DirectoryChooser dc=new DirectoryChooser();
        dc.setInitialDirectory(new File(path));
        File chosenDir=dc.showDialog(null);
        if(chosenDir!=null){
            String filepath=srcfile_list.getSelectionModel().getSelectedItem().getText();
            File file=new File(chosenDir.getAbsolutePath()+"/"+filepath.substring(filepath.lastIndexOf("/")+1));
            if(file.exists()&&file.isFile()) {
                if (ConfirmBox.display("OVERWRITE ALERT", "File Already Exists. Press Yes To Overwrite.")) {
                    if (file.canWrite())
                        writeFile(file, ((TextArea) srcfile_list.getSelectionModel().getSelectedItem().getContent()).getText());
                    else
                        createAlertDialogBox("PERMISSION DENIED","The required home directory cannot be created.","PermissionDenied.png");
                }
                else
                    System.out.println("File not written");
            }
            else
                writeFile(file, ((TextArea) srcfile_list.getSelectionModel().getSelectedItem().getContent()).getText());
        }
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
    public  MenuItem putinMenu(String name,String task,Menu menu) {
        MenuItem menuItem=new MenuItem(name);
        menuItem.setOnAction(event -> System.out.println(task));
        menu.getItems().add(menuItem);
        return menuItem;
    }
    public   boolean closeWindow(){
        Boolean ans=ConfirmBox.display("Confirm Exit","Are you sure u want to exit?");
        saveContent(srcfile_list.getSelectionModel().getSelectedItem());
        System.out.println("File is saved.");
        return ans;
    }
    public void createAlertDialogBox(String title, String message, String icon){
//        Class<?> clazz = this.getClass();
//        System.out.println(getClass().getResourceAsStream("/Icons/"+icon));
        TheAlertBox.display(title,message,new Image(getClass().getResourceAsStream("/Icons/"+icon)),"OK");
    }
    public ComboBox<String> createComboBox(String[] movies){
        ComboBox<String> comboBox=new ComboBox<>();
//        StringBuffer movies=new StringBuffer
//                ("Good Will Hunting, St. Vincent, Blackhat");
        comboBox.getItems().addAll(movies);
        comboBox.setValue(movies.length>0?movies[0]:"NULL"); // Default value.
//        comboBox.setPromptText("What is your favourite movie ?");
        comboBox.setOnAction(event -> {
            System.out.println
                    ("User selected : "+comboBox.getValue());
//            printMovie(comboBox,movies);
        });
//        comboBox.setEditable(true);
        return comboBox;
    }

    public  ComboBox<String> addItemtoComboBox(ComboBox<String> comboBox, String name){
        comboBox.getItems().add(name);
        return comboBox;
    }

    public void printMovie(ComboBox<String> comboBox,StringBuffer movies){
        String val=comboBox.getValue();// getValue()
//        System.out.println(val);
        Boolean present=false;
        String []movarr=movies.toString().split(",");
        for(int i=0;i<movarr.length;i++)
            if(movarr[i].equals(val))
            {
                present=true;
                break;
            }
        if(!present)
        {
            movies.append(","+val);
            comboBox.getItems().add(val);
        }
    }

    public  CheckBox getCheckBox(String name, Boolean setSelected){
        CheckBox box=new CheckBox(name);
        box.setSelected(setSelected);
        box.setPadding(new Insets(20,20,0,20));
        total_num_checkbox++;
        return box;
    }

    public  void  handleOptions(CheckBox[]addnew) {
        String message="users order: ";
        for(int i=0;i<total_num_checkbox;i++)
        {
            if(addnew[i]==null)
                break;
            if(addnew[i].isSelected())
                message+=(((message.equals("users order: "))?"":", ")+addnew[i].getText());
        }
            System.out.println(message);
    }

    public ChoiceBox<String> createChoiceBox(String []choices,int setnum){
        ChoiceBox<String> choiceBox=new ChoiceBox<>();
        choiceBox.getItems().addAll(choices);
        choiceBox.getSelectionModel().selectedItemProperty().addListener((v,oldval,newval)->System.out.println(newval));
        if(setnum!=-1)
            choiceBox.setValue(choices[Math.min(Math.max(setnum,0),choices.length-1)]);
        choiceBox.setPadding(new Insets(2,2,0,2));
        return choiceBox;
    }

    public String getChoice(ChoiceBox<String> choiceBox){
        String comp=choiceBox.getValue();
//        System.out.println(comp);
        return comp;
    }

    public TabPane createTabs(String []title, String []content, int number, Node node, Boolean setIcon, Boolean createnew){
        TabPane tabpane =new TabPane();
        HBox cont;
        ImageView icon=new ImageView(new Image(getClass().getResourceAsStream("/Icons/Terminal.png")));
        icon.setFitWidth(20); icon.setFitHeight(20);
//        tabpane.setMinSize(500,500);
//        Tab tab;
        Label label;
        for(int i=0;i<number;i++) {
            label = new Label(i < title.length ? title[i] : "Untitled");
            if(setIcon){
                final Tab tab = new Tab();
                cont=new HBox(2);
                cont.getChildren().addAll(icon);
                cont.getChildren().addAll(label);
                tab.setGraphic(cont);
                if(node!=null&&i==0) {
                    tab.setContent(node);
//                try {
////                    System.out.println(diretory_struct.getSelectionModel());
//                    tab.setOnSelectionChanged(event -> {
//                                if(diretory_struct.getSelectionModel()!=null)
//                                    diretory_struct.getSelectionModel().select(diretory_struct.getRoot());
//                            });
//                }catch (Exception e){
//                    System.out.println("ERROR:"+e);
//                }
                    tab.setOnCloseRequest(event -> {
                        if(tabpane.getTabs().size()==2)
                            event.consume();
                        else
                            comboBox.getItems().removeAll(tab.getText());
                        saveContent(tab);
//                        execute(true);
                    });
                }
                else {
                    label = new Label(i < content.length ? content[i] : "Empty document.");
                    tab.setContent(label);
                }
                tabpane.getTabs().add(tab);
            }
            else {
                final Tab tab = new Tab(i < title.length ? title[i] : "Untitled");
                if(node!=null&&i==0) {
                    tab.setContent(node);
//                try {
////                    System.out.println(diretory_struct.getSelectionModel());
//                    tab.setOnSelectionChanged(event -> {
//                                if(diretory_struct.getSelectionModel()!=null)
//                                    diretory_struct.getSelectionModel().select(diretory_struct.getRoot());
//                            });
//                }catch (Exception e){
//                    System.out.println("ERROR:"+e);
//                }
                    tab.setOnCloseRequest(event -> {
                        if(tabpane.getTabs().size()==2)
                            event.consume();
                        else
                            comboBox.getItems().removeAll(tab.getText());
                        saveContent(tab);
//                        execute(true);
                    });
                }
                else {
                    label = new Label(i < content.length ? content[i] : "Empty document.");
                    tab.setContent(label);
                }
                tabpane.getTabs().add(tab);
            }

        }
        if(createnew) {
            Tab newtab = new Tab("+");
            EventHandler<Event> event = new EventHandler<Event>() {
                public void handle(Event e) {
                    if (newtab.isSelected()) {
//                        Classname clsname=new Classname();
                        String name="";
//                        while ((name=clsname.display(choiceBox)).startsWith("\n"))
                        name=Classname.display(choiceBox,tabpane);
                        if(!name.equals("")) {
//                        Tab tab = new Tab("Untitled" + (tab_Count == 0 ? "" : "_" + tab_Count) + getExtention());
                            String arr[] = name.split("\n");
                            String filename=arr[0].trim();
                            String extension=arr[1].trim();
                            Tab tab = new Tab( filename+ extension);
                            makeBranch(filename+extension, sample);
                            if(isExtentionCorrect(extension)) {
                                comboBox.getItems().addAll(filename + extension);
                                comboBox.setValue(filename + extension);
                            }
//                        tab_Count++;
                            if (node == null) {
                                Label label = new Label("This is new Tab");
                                tab.setContent(label);
                            } else {
                                textArea = new TextArea();
                                //check this out functions of textfield.
                                textArea.setMinSize(550, 550);
                                //        textArea.setPrefSize(200,600);
                                //        ScrollPane scrollPane=new ScrollPane();
                                //        scrollPane.setMinSize(200,500);
                                //        scrollPane.setContent(textArea);
                                textArea.setEditable(true);
                                textArea.setText(getContent(extension));
                                tab.setContent(textArea);
                                tab.setOnCloseRequest(event -> {
                                    if(tabpane.getTabs().size()==2)
                                        event.consume();
                                    else
                                        comboBox.getItems().removeAll(tab.getText());
                                    saveContent(tab);
//                                    execute(true);
                                });
                            }
//                            tab.setOnSelectionChanged(event -> {
//                                if(diretory_struct.getSelectionModel()!=null)
//                                    diretory_struct.getSelectionModel().select(diretory_struct.getRoot());
//                            });
                            tabpane.getTabs().add(
                                    tabpane.getTabs().size() - 1, tab);
                        }
                        // select the last tab
                        tabpane.getSelectionModel().select(
                                tabpane.getTabs().size() - 2);
                    }
                }
            };
            newtab.setOnSelectionChanged(event);
            tabpane.getTabs().add(newtab);
        }
        tabpane.getSelectionModel().selectedItemProperty().addListener((v,oldval,newval) ->{
            if(oldval!=null) {
                saveContent(oldval);
//                execute(true);
            }
            if(newval!=null&&isExtentionCorrect(newval.getText())) {
                comboBox.setValue(newval.getText());
//                addItemtoComboBox(comboBox,newval.getText());
            }
        });
        return tabpane;
    }

    public void saveContent(Tab oldval) {
//        System.out.println(oldval.getText());
        try {
            if (oldval != null) {
                // Some Error is being thrown  because of class casting but output is not getting hampered.
                TextArea textArea = (TextArea) oldval.getContent();
                if (textArea != null) {
//                    System.out.println("Content: \n" + textArea.getText());
                    File file;
                    if(oldval.getText().trim().startsWith("/"))
                        file=new File(oldval.getText().trim());
                    else
                        file=new File(path+oldval.getText());
                    BufferedWriter bwr=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
                    bwr.write(textArea.getText());
                    bwr.flush();
                    bwr.close();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Error: "+ e);
        }
    }

    public static int isPresent(TabPane tabpane, String str){
        Iterator<Tab> tabIterator=tabpane.getTabs().iterator();
        Tab temp;
        int j=0;
        while (tabIterator.hasNext()){
            temp=tabIterator.next();
            if(temp.getText()!=null && temp.getText().equals(str))
                return j;
            j++;
        }
        return  -1;
    }

    public static String getCompiled(String str){
//        {"Java","Python","C","C++","Shell Script"};
//        String str=getChoice(choiceBox);
        String  chkstr;
        if(str.contains(".")) {
           chkstr = str.substring(str.lastIndexOf(".")).trim();
            switch (chkstr) {
                case ".java":
                    return ("javac "+str);
                case ".py":
                    return "";
                case ".c":
                    return ("gcc "+str+" -o "+str.substring(0,str.lastIndexOf(".")));
                case ".cpp":
                    return ("g++ "+str+" -o "+str.substring(0,str.lastIndexOf(".")));
                case ".sh":
                    return ("chmod +x "+str);
            }
        }
        return "NULL";
    }
    public static String getRun(String str){
//        {"Java","Python","C","C++","Shell Script"};
//        String str=getChoice(choiceBox);
        String  chkstr;
        if(str.contains(".")) {
            chkstr = str.substring(str.lastIndexOf(".")).trim();
            switch (chkstr) {
                case ".java":
                    return ("java "+str.substring(0,str.lastIndexOf(".")));
                case ".py":
                    return ("python3 "+str);
                case ".c":
                case ".cpp":
                    return ("./"+str.substring(0,str.lastIndexOf(".")));
                case ".sh":
                    return ("./"+str);
            }
        }
        return "NULL";
    }
    public Boolean isExtentionCorrect(String str){
//        {"Java","Python","C","C++","Shell Script"};
//        String str=getChoice(choiceBox);
        if(str.contains(".")) {
            str=str.substring(str.lastIndexOf(".")).trim();
            switch (str) {
                case ".java":
                    return (true);
                case ".py":
                    return (true);
                case ".c":
                    return (true);
                case ".cpp":
                    return (true);
                case ".sh":
                    return (true);
            }
        }
        return false;
    }
    public String getContent(String str){
//        {"Java","Python","C","C++","Shell Script"};
//        String str=getChoice(choiceBox);
        BufferedReader br;
        String filename="Text.txt";
        switch (str) {
            case ".java":
                filename = "Java.txt";
                break;
            case ".py":
                filename = "Python.txt";
                break;
            case ".c":
                filename = "C.txt";
                break;
            case ".cpp":
                filename = "C++.txt";
                break;
            case ".sh":
                filename = "ShellScript.txt";
        }
        StringBuffer sbr=new StringBuffer("");
            try {
                br=new BufferedReader(new InputStreamReader(
                            getClass().getResourceAsStream("/Templates/"+filename)));
                while((str=br.readLine())!=null){
                    sbr.append(str+"\n");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
//        System.out.println(sbr.toString());
        return sbr.toString();
    }
    public ListView<String> createList(String []name){
        ListView<String> listview;
        listview=new ListView<String>();

//        listview.getItems().addAll
//                ("Iron Man","Titanic","Contact","Surrogates");
        listview.getItems().addAll(name);
        listview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);//
//        listview.setOrientation(Orientation.HORIZONTAL);
        return  listview;
    }
    public void buttonClicked(ListView<String> listview){
        String message="";
        ObservableList<String> movies;
        movies=listview.getSelectionModel().getSelectedItems();

        for (String m: movies)
            message+=m+"\n";

        System.out.println(message);
    }

    public TableView<Product> createTable(){
        //Name column
        TableColumn<Product,String> nameColumn=new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        //Price column
        TableColumn<Product,String> priceColumn=new TableColumn<>("Price");
        priceColumn.setMinWidth(100);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Quantity column
        TableColumn<Product,String> quantityColumn=new TableColumn<>("Quantity");
        quantityColumn.setMinWidth(100);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableView<Product> table;
        table=new TableView<>();
        table.setItems(getProduct());
        table.getColumns().addAll(nameColumn,priceColumn,quantityColumn);
        return table;
    }

    public ObservableList<Product> getProduct(){
        ObservableList<Product> products= FXCollections.observableArrayList();
        products.add(new Product("Laptop",859.00,20));
        products.add(new Product("bouncy Ball",2.49,198));
        products.add(new Product("Toilet",99.00,74));
        products.add(new Product("The Notebook DVD",19.99,12));
        products.add(new Product("Corn",1.49,856));
        return  products;
    }

    public TreeView<String> createTree(String rootname,TreeItem<String>node[],String []nodename,int []nodecount){
        TreeView<String>tree;
        TreeItem<String> root;
        root=new TreeItem<>(rootname);
        root.setExpanded(true);
//        int len=Math.min(parent.length,child.length);
        int k=0;
        for(int i=0;i<nodecount.length;i++)
            for(int j=0;j<nodecount[i];j++) {
                node[k] = makeBranch(k < nodename.length ? nodename[k] : "Unknown Child",
                        (i >0 && node[i-1] != null) ? node[i-1] : root);
                node[k++].setExpanded(true);
            }
        tree=new TreeView<>(root);
//        tree.setShowRoot(false);
//        if(tree.getSelectionModel()!=null&&tree.getSelectionModel().getSelectedItem()!=null)
//        tree.getSelectionModel().selectedItemProperty().addListener(event->{
//            tree.getSelectionModel().getSelectedItem().addEventHandler(MouseEvent.MOUSE_CLICKED,event1 -> {
//                final TreeItem<String> newval;
//                if((newval=tree.getSelectionModel().getSelectedItem()).isLeaf())
//                    selectTab(newval,newval.getValue());
//            });
//            final TreeItem<String> newval;
//            if((newval=tree.getSelectionModel().getSelectedItem()).isLeaf())
//                selectTab(newval,newval.getValue());
//        });
        tree.getSelectionModel().selectedItemProperty()
                .addListener((v,oldval,newval) ->{
                    if(newval!=null && newval.isLeaf())
                        selectTab(newval,newval.getValue());
                });
        return tree;
    }

    public void selectTab(TreeItem<String>treeItem,String value) {
        StringBuffer rev=new StringBuffer();
        StringBuilder dirs_path=new StringBuilder();
        String curdir;
        while(!(curdir=treeItem.getParent().getValue()).equals("sample")){
            rev.append(curdir+"/");
            dirs_path.append(rev.reverse().toString().trim());
            rev.delete(0,rev.length());
            treeItem=treeItem.getParent();
        }
        dirs_path=dirs_path.reverse();
        System.out.println(dirs_path+value);
        int pos=isPresent(srcfile_list,dirs_path+value);
        if(pos!=-1){
            srcfile_list.getSelectionModel().select(pos);
            if(isExtentionCorrect(dirs_path+value)) {
                comboBox.setValue(dirs_path + value);
//                addItemtoComboBox(comboBox,dirs_path + value);
            }
        }
        else{
//            StringBuffer rev=new StringBuffer();
//            StringBuilder dirs_path=new StringBuilder();
//            String curdir;
//            while(!(curdir=treeItem.getParent().getValue()).equals("sample")){
//                rev.append(curdir+"/");
//                dirs_path.append(rev.reverse().toString().trim());
//                rev.delete(0,rev.length());
//                treeItem=treeItem.getParent();
//            }
//            dirs_path=dirs_path.reverse();
            File file=new File(path+dirs_path+value);
//            System.out.println(path+dirs_path);
            if(file.exists()&&file.isFile()&&file.canRead()){
                try {
                    BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                    rev.delete(0,rev.length());
                    while((curdir=br.readLine())!=null){
                        rev.append(curdir+"\n");
                    }
                    Tab tab=new Tab(dirs_path+value);
                    textArea = new TextArea();
                    //check this out functions of textfield.
                    textArea.setMinSize(550, 550);
                    //        textArea.setPrefSize(200,600);
                    //        ScrollPane scrollPane=new ScrollPane();
                    //        scrollPane.setMinSize(200,500);
                    //        scrollPane.setContent(textArea);
                    textArea.setEditable(true);
                    textArea.setText(rev.toString());
                    tab.setContent(textArea);
                    tab.setOnCloseRequest(event -> {
                        if(srcfile_list.getTabs().size()==2)
                            event.consume();
                        else
                            comboBox.getItems().removeAll(tab.getText());
                        saveContent(tab);
//                        execute(true);
                    });
//                    tab.setOnSelectionChanged(event -> {
//                        if(diretory_struct.getSelectionModel()!=null)
//                            diretory_struct.getSelectionModel().select(diretory_struct.getRoot());
//                    });
                    srcfile_list.getTabs().add(
                            srcfile_list.getTabs().size() - 1, tab);
                    // select the last tab
                    srcfile_list.getSelectionModel().select(
                            srcfile_list.getTabs().size() - 2);
                    if(isExtentionCorrect(value)) {
                        comboBox.setValue(tab.getText());
                        comboBox.getItems().addAll(tab.getText());
                    }

                } catch (FileNotFoundException e) {
                    System.out.println("Error"+e);
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println("Error"+e);
                    e.printStackTrace();
                }
                catch (Exception e){
                    System.out.println("Error"+e);
                }
            }
        }
    }
    public TreeItem<String> makeBranch(String name, TreeItem<String> parent) {

        TreeItem<String> item=new TreeItem<>(name);
//        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }
    public void listFilesForFolder(final File folder,TreeItem<String> parent) {
        int dircnt=0;
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
//                sbr.append(listFilesForFolder(fileEntry).toString()+"\n");
                dircnt++;
            } else {
//                System.out.println(fileEntry.getName());
                makeBranch(fileEntry.getName(),parent);
            }
        }
//        TreeItem<String> dirtree[]=new TreeItem[dircnt];
        TreeItem<String> dirtree;
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
//                sbr.append(listFilesForFolder(fileEntry).toString()+"\n");
                dirtree=makeBranch(fileEntry.getName(),parent);
                listFilesForFolder(fileEntry,dirtree);
                dircnt--;
            }
        }
    }
    public void createNew(){
//        Classname clsname=new Classname();
        String name="";
//                        while ((name=clsname.display(choiceBox)).startsWith("\n"))
        name=Classname.display(choiceBox,srcfile_list);
        if(!name.equals("")) {
//                        Tab tab = new Tab("Untitled" + (tab_Count == 0 ? "" : "_" + tab_Count) + getExtention());
            String arr[] = name.split("\n");
            String filename=arr[0].trim();
            String extension=arr[1].trim();
            Tab tab = new Tab( filename+ extension);
            makeBranch(filename+extension, sample);
            if(isExtentionCorrect(extension)) {
                comboBox.getItems().addAll(filename + extension);
                comboBox.setValue(filename + extension);
            }
//                        tab_Count++;

                textArea = new TextArea();
                //check this out functions of textfield.
                textArea.setMinSize(550, 550);
                //        textArea.setPrefSize(200,600);
                //        ScrollPane scrollPane=new ScrollPane();
                //        scrollPane.setMinSize(200,500);
                //        scrollPane.setContent(textArea);
                textArea.setEditable(true);
                textArea.setText(getContent(extension));
                tab.setContent(textArea);
                tab.setOnCloseRequest(event -> {
                    if(srcfile_list.getTabs().size()==2)
                        event.consume();
                    else
                        comboBox.getItems().removeAll(tab.getText());
                    saveContent(tab);
//                    execute(true);
                });
//                            tab.setOnSelectionChanged(event -> {
//                                if(diretory_struct.getSelectionModel()!=null)
//                                    diretory_struct.getSelectionModel().select(diretory_struct.getRoot());
//                            });
            srcfile_list.getTabs().add(
                    srcfile_list.getTabs().size() - 1, tab);
        }
        // select the last tab
        srcfile_list.getSelectionModel().select(
                srcfile_list.getTabs().size() - 2);
    }
    public String execute(Boolean onlycompile){
        Process p;
        StringBuffer sbr;
        try {
            String addPath=comboBox.getValue();
            String filename=addPath.substring(addPath.lastIndexOf("/")+1);
            String comp=getCompiled(filename);
            System.out.println(comp);
            if(!comp.equals("NULL")) {
                System.out.println(getClass().getResource("compile_n_run.sh").getPath());
                String[] cmd = {"/bin/sh", "-c", "chmod +x "+getClass().getResource("compile_n_run.sh").getPath()
                };
                p = Runtime.getRuntime().exec(cmd);
                p.waitFor();
                int z=p.exitValue();
                System.out.println("z= "+z);
                if(addPath.startsWith("/"))
                    cmd [2]=getClass().getResource("compile_n_run.sh").getPath()+" "+addPath.substring(0, addPath.lastIndexOf("/") + 1)+" \""+comp+"\"";
                else
                    cmd [2]=getClass().getResource("compile_n_run.sh").getPath()+" "+path + addPath.substring(0, addPath.lastIndexOf("/") + 1)+" \""+comp+"\"";
                System.out.println(cmd[2]);
                p = Runtime.getRuntime().exec(cmd);
                p.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        p.getInputStream()));
                z=p.exitValue();
                System.out.println("z= "+z);
                String line;
                sbr = new StringBuffer("");
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    sbr.append(line + "\n");
                }
                System.out.println(sbr.toString());
                if(z!=0)
                    return sbr.toString();
                comp=getRun(filename);
                System.out.println(comp);
                if(onlycompile)
                    return ""+z;
                if(!comp.equals("NULL")) {
                    if(addPath.startsWith("/"))
                        cmd [2]=getClass().getResource("compile_n_run.sh").getPath()+" "+addPath.substring(0, addPath.lastIndexOf("/") + 1)+" \""+comp+"\" "+inputfileset;
                    else
                        cmd [2]=getClass().getResource("compile_n_run.sh").getPath()+" "+path + addPath.substring(0, addPath.lastIndexOf("/") + 1)+" \""+comp+"\" "+inputfileset;
                    System.out.println(cmd[2]);
                    p = Runtime.getRuntime().exec(cmd);
                    try {
                        p.waitFor();
                    }catch (InterruptedException e){
                        return "TIME LIMIT EXCEEDED";
                    }
                    z=p.exitValue();
                    System.out.println("z= "+z);
                    sbr.delete(0,sbr.length());
                    reader = new BufferedReader(new InputStreamReader(
                            p.getInputStream()));
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                        sbr.append(line + "\n");
                    }
                    System.out.println("Output: \n"+sbr.toString());
                    return sbr.toString();
                }
            }
            else{
                System.out.println("Sorry! The file type is unknown and cannot be compiled.");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "-1";
    }
    public void openFile(){
        FileChooser fileChooser=new FileChooser();
        fileChooser.setInitialDirectory(new File(path));
//        fileChooser.getExtensionFilters().addAll(
//                new FileChooser.ExtensionFilter("JAVA Files","*.java"));
        File selectedfile=fileChooser.showOpenDialog(null);
        if(selectedfile!=null){
            String openfilepath=selectedfile.getAbsolutePath();
            System.out.println(openfilepath);
            Tab tab=new Tab(openfilepath);
            textArea = new TextArea();
            textArea.setMinSize(550, 550);
            textArea.setEditable(true);
            textArea.setText(readFile(openfilepath));
            tab.setContent(textArea);
            tab.setOnCloseRequest(event -> {
                if(srcfile_list.getTabs().size()==2)
                    event.consume();
                else
                    comboBox.getItems().removeAll(tab.getText());
                saveContent(tab);
//                        execute(true);
            });
//                    tab.setOnSelectionChanged(event -> {
//                        if(diretory_struct.getSelectionModel()!=null)
//                            diretory_struct.getSelectionModel().select(diretory_struct.getRoot());
//                    });
            srcfile_list.getTabs().add(
                    srcfile_list.getTabs().size() - 1, tab);
            // select the last tab
            srcfile_list.getSelectionModel().select(
                    srcfile_list.getTabs().size() - 2);
            if(isExtentionCorrect(openfilepath)){
                comboBox.setValue(tab.getText());
                comboBox.getItems().addAll(tab.getText());
            }

        }
    }
    public String readFile(String filepath){
        StringBuilder sbr=new StringBuilder("");
        String line;
        try {
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(new File(filepath))));
            while ((line=bufferedReader.readLine())!=null)
                sbr.append(line+"\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  sbr.toString();
    }
    public static void main(String[] args) {
        launch(args);
    }
    class MyThread implements Runnable {

        Thread t;

        MyThread()
        {
            t = new Thread(this);
            System.out.println("New thread: " + t);
            t.start(); // Starting the thread
        }
        // execution of thread starts from run() method
        public void run()
        {
//        while (!Thread.interrupted()) {
//            System.out.println("Thread is running");
//        }
            try {
                execArea.setText(execute(false));
                if (t.interrupted()) {
                    System.out.println("I'm killed.");
                    return;
                }
            }catch (Exception eintr){
                System.out.println(eintr);
                eintr.printStackTrace();
            }
            System.out.println("Thread has stopped.");
        }
    }
}
