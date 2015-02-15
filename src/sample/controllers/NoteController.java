package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * Created by francisco on 29/01/15.
 */
public class NoteController {
    @FXML
    Label noteTitleLabel;

    @FXML
    Label creationDateLabel;

    @FXML
    Button editButton;

    @FXML
    Button saveButton;

    @FXML
    TextArea noteTextField;

    @FXML
    public void enableEditMode(ActionEvent actionEvent){

    }

    @FXML
    public void saveNote(ActionEvent actionEvent){

    }
}
