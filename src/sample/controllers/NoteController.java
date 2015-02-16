package sample.controllers;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import org.json.JSONObject;
import sample.Singleton;

import javax.swing.*;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * Created by francisco on 29/01/15.
 */
public class NoteController {
    public String noteUUID = null;
    public JSONObject note = null;

    @FXML
    TextField noteTitle;

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
        this.editButton.setDisable(true);
        this.saveButton.setDisable(false);
        this.noteTextField.setEditable(true);
    }

    @FXML
    public void saveNote(ActionEvent actionEvent){
        if (this.noteUUID == null){
            this.noteUUID = UUID.randomUUID().toString();

            this.note.put("ID", this.noteUUID);
            this.note.put("title", this.noteTitle.getText());
            this.note.put("creationDate", this.creationDateLabel.getText());
            this.note.put("text", this.noteTextField.getText());
            this.note.put("email", Singleton.INSTANCE.userEmail);

            try {
                Singleton.INSTANCE.remoteServer.createNote(Singleton.INSTANCE.userEmail, this.note.toString(3));
                this.updateParentListView(this.note, actionEvent);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
        else{
            this.note.put("title", this.noteTitle.getText());
            this.note.put("text", this.noteTextField.getText());

            try {
                Singleton.INSTANCE.remoteServer.updateNote(Singleton.INSTANCE.userEmail, this.note.toString(3));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }

    private void updateParentListView(final JSONObject note, ActionEvent actionEvent) {
        Stage sourceStage = this.getWindowFromActionEvent(actionEvent);
        Scene parentScene = sourceStage.getOwner().getScene();

        ListView<JSONObject> listView = (ListView<JSONObject>) parentScene.lookup("#notesListView");
        ObservableList<JSONObject> values = listView.getItems();
        values.addAll(note);
        listView.setItems(values);
    }

    private Stage getWindowFromActionEvent(ActionEvent actionEvent){
        Node node = (Node) actionEvent.getSource();
        Scene scene = node.getScene();
        Stage stage = (Stage) scene.getWindow();

        return stage;
    }

    public void initialize(){
        if (this.noteUUID == null){

            this.note = new JSONObject();
            String creationDate = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
            String creationHour = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());

            this.noteTitle.setText("New Note");
            this.creationDateLabel.setText(String.format("Creation date: %s at %s", creationDate, creationHour));

            this.editButton.setDisable(true);
            this.saveButton.setDisable(false);

            this.noteTitle.setEditable(true);
            this.noteTextField.setEditable(true);
        }
        else{
            this.noteTitle.setText(note.getString("title"));
            this.creationDateLabel.setText(note.getString("creationDate"));
            this.noteTextField.setText(note.getString("text"));
            this.noteTextField.requestFocus();
        }
    }
}
