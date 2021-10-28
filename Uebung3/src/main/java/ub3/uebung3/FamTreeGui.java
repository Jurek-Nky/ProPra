package ub3.uebung3;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FamTreeGui extends Application {


    public static void main(String[] args) {
        launch();
    }

    private FamilyTree familyTree;
    private Pane buttonPane;
    private TextArea textArea;

    private TextField inputName;
    private TextField inputFather;
    private TextField inputMother;

    @Override
    public void start(Stage stage) {
        familyTree = new FamilyTree();

        stage.setTitle("Family Tree Gui");

        Button newFamilyMember = new Button("Add Member/Relation");
        Button save = new Button("Save");
        Button load = new Button("load");

        ComboBox<String> genderSelect = new ComboBox<>();
        genderSelect.getItems().addAll("Male", "Female");
        genderSelect.setPromptText("Gender");
        inputName = new TextField();
        inputName.setPromptText("Name");
        inputMother = new TextField();
        inputMother.setPromptText("Mother");
        inputFather = new TextField();
        inputFather.setPromptText("Father");


        textArea = new TextArea();

        StackPane centerArea = new StackPane();

        buttonPane = new Pane();
        ScrollPane buttonLayerScroll = new ScrollPane();
        buttonLayerScroll.setContent(buttonPane);
        centerArea.getChildren().addAll(buttonLayerScroll);

        BorderPane root = new BorderPane();
        GridPane topGrid = new GridPane();
        topGrid.addRow(2, save, load);
        topGrid.addRow(1, inputName, inputMother, inputFather, genderSelect, newFamilyMember);
        topGrid.setHgap(10);

        root.setTop(topGrid);
        root.setCenter(centerArea);

        stage.setScene(new Scene(root, 1000, 800));
        stage.show();
        root.setBottom(textArea);
        //actionlistener

        save.setOnAction(actionEvent -> {
            try {
                saveButton(stage);
            } catch (IOException e) {
                textArea.setText(e.getLocalizedMessage());
            }
        });
        load.setOnAction(actionEvent -> loadButton(stage));
        newFamilyMember.setOnAction(actionEvent -> {
            switch (genderSelect.getValue()) {
                case "Male" -> {
                    if (inputFather.getText().isEmpty() || inputMother.getText().isEmpty()) {
                        newMemberButton(inputName.getText(), "m");
                    } else {
                        try {
                            newMemberButton(inputName.getText(), inputMother.getText(), inputFather.getText(), "m");
                        } catch (PersonNotFound | RelationNotPossible e) {
                            textArea.setText(e.getLocalizedMessage());
                        }
                    }
                }
                case "Female" -> {
                    if (inputFather.getText().isEmpty() || inputMother.getText().isEmpty()) {
                        newMemberButton(inputName.getText(), "f");
                    } else {
                        try {
                            newMemberButton(inputName.getText(), inputMother.getText(), inputFather.getText(), "f");
                        } catch (PersonNotFound | RelationNotPossible e) {
                            textArea.setText(e.getLocalizedMessage());
                        }
                    }
                }
                default -> {

                }
            }
            generateTree();

        });

        generateTree();

    }

    private void saveButton(Stage stage) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select destination");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            familyTree.serialize(file.getPath());
        }
    }

    private void loadButton(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Family Tree file");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            familyTree.deserialize(file.getPath());
        }
    }

    private void newMemberButton(String name, String mother, String father, String gender) throws PersonNotFound, RelationNotPossible {
        familyTree.addPerson(name, gender);
        familyTree.addRelation(familyTree.findPerson(name), familyTree.findPerson(mother), familyTree.findPerson(father));
        textArea.setText(String.format("%s got added to the system.", name));
        System.out.println("added");
    }

    private void newMemberButton(String name, String gender) {
        familyTree.addPerson(name, gender);
        textArea.setText(String.format("%s got added to the system.", name));
        System.out.println("added");
    }

    private void generateTree() {
        buttonPane.getChildren().clear();
        alreadyGenerated = new ArrayList<>();
        for (Person p : familyTree.getFamList()) {
            if (p.getGeneration() == 0 && p.getGender().equals("m")) {
                generateTreeRec(p, 0, 1);
                break;
            }
        }
        for (int i = 0; i < familyTree.getFamList().size(); i++) {
            if (listContainsNot(alreadyGenerated, familyTree.getFamList().get(i).getName())) {
                insertPersonWithoutRelation(familyTree.getFamList().get(i), i);
            }
        }
    }

    ArrayList<Person> alreadyGenerated = new ArrayList<>();

    private void insertPersonWithoutRelation(Person person, int pos) {
        Button btn = new Button(person.getName());
        btn.setOnAction(actionEvent -> System.out.println(person.getName()));
        btn.setPrefSize(100, 30);
        btn.setLayoutX(pos * 100);
        btn.setLayoutY(0);
        buttonPane.getChildren().add(btn);
        alreadyGenerated.add(person);
    }

    private Button generateTreeRec(Person person, int generation, int column) {

        Button btn = new Button(person.getName());
        btn.setOnAction(actionEvent -> System.out.println(person.getName() + " : " + column));
        System.out.println(btn.getText() + " : " + generation);
        btn.setPrefSize(100, 30);
        btn.setLayoutX(column * 100);
        btn.setLayoutY(generation * 80);
        buttonPane.getChildren().add(btn);
        alreadyGenerated.add(person);

        // add partner
        if (person.getGender().equals("m")) {
            if (!person.getChildren().isEmpty() && listContainsNot(alreadyGenerated, person.getChildren().get(0).getMother().getName())) {
                Person wife = person.getChildren().get(0).getMother();
                int wfColumn = column + 1;
                generateTreeRec(wife, generation, wfColumn);
            }
        } else if (person.getGender().equals("f")) {
            if (!person.getChildren().isEmpty() && listContainsNot(alreadyGenerated, person.getChildren().get(0).getFather().getName())) {
                Person husband = person.getChildren().get(0).getFather();
                int hsbColumn = column + 1;
                generateTreeRec(husband, generation, hsbColumn);
            }
        }

        //add parents
        if (person.getFather() != null && listContainsNot(alreadyGenerated, person.getFather().getName())) {
            Button btnTmp = generateTreeRec(person.getFather(), generation - 1, column + familyTree.getAllChildrenCount(person.getFather()));
            Line line = new Line(btnTmp.getLayoutX() + 100, btnTmp.getLayoutY() + 30, btn.getLayoutX() + 50, btn.getLayoutY());
            line.setStrokeWidth(2);
            buttonPane.getChildren().add(line);
            line.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    System.out.printf("start %s:%s  end %s:%s%n", line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
                }
            });
        }
        if (person.getMother() != null && listContainsNot(alreadyGenerated, person.getMother().getName())) {
            Button btnTmp = generateTreeRec(person.getMother(), generation - 1, column + familyTree.getAllChildrenCount(person.getMother()));
            Line line = new Line(btnTmp.getLayoutX() + 100, btnTmp.getLayoutY() + 30, btn.getLayoutX() + 50, btn.getLayoutY());
            line.setStrokeWidth(2);
            line.setSmooth(true);
            buttonPane.getChildren().add(line);
            line.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    System.out.printf("start %s:%s  end %s:%s%n", line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
                }
            });
        }

        //add children
        for (int i = 0; i < person.getChildren().size(); i++) {
            Person child = person.getChildren().get(i);
            int chColumn = column + (familyTree.getAllChildrenCount(child) / (person.getChildren().size() / (i + 1)));
            if (listContainsNot(alreadyGenerated, child.getName())) {
                double Xoffset = 0;
                double Yoffset = 30;
                Button btnTmp = generateTreeRec(child, generation + 1, chColumn);
                Line line = new Line(btn.getLayoutX() + Xoffset, btn.getLayoutY() + Yoffset, btnTmp.getLayoutX() + 50, btnTmp.getLayoutY());
                line.setStrokeWidth(2);
                buttonPane.getChildren().add(line);
                line.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        System.out.printf("start %s:%s  end %s:%s%n", line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
                    }
                });
            }
        }
        return btn;
    }

    private boolean listContainsNot(ArrayList<Person> list, String name) {
        for (Person person : list) {
            if (person.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }
}