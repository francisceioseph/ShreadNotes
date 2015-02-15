package sample.controllers;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by francisco on 29/01/15.
 */
public class MainWindowController implements Initializable{

    @FXML
    Label usernameLabel;

    @FXML
    Label emailAddressLabel;

    @FXML
    public void createNewNote(ActionEvent actionEvent){
        Window currentWindow = this.getWindowFromEvent(actionEvent);
        this.openNewNoteWindow(currentWindow);
    }

    @FXML
    public void logout(ActionEvent actionEvent){
        //TODO call a method to logout of the system
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TODO initialize a notes list
        //TODO render a note list in the style noteName with subtitle creation date
        //TODO put a image to imageview
    }

    private Window getWindowFromEvent(Event event){
        Node sourceNode = (Node) event.getSource();
        Scene sourceScene = sourceNode.getScene();
        return sourceScene.getWindow();
    }

    private void openNewNoteWindow(Window window) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/note.fxml"));
        Parent root = null;

        try {
            root = (Parent) fxmlLoader.load();
            Stage stage = this.makeWindow("New Note", root);
            stage.initOwner(window);

            Scene scene = stage.getScene();
            TextField noteTitle = (TextField) scene.lookup("#noteTitle");
            Label dateField = (Label) scene.lookup("#creationDateLabel");
            Button edit = (Button) scene.lookup("#editButton");
            Button save = (Button) scene.lookup("#saveButton");
            TextArea textArea = (TextArea) scene.lookup("#noteTextField");

            noteTitle.setText("New Note");



            //TODO initialize notes fields. Use scene.lookup

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Stage makeWindow(String windowTitle, Parent root) {
        Stage stage = new Stage();
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(root));

        return stage;
    }
}
