package sample.controllers;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import org.json.JSONObject;
import sample.Singleton;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.UUID;

/**
 * Created by francisco on 29/01/15.
 */
public class NoteController {
    public String noteUUID = null;

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
        boolean success = false;

        JSONObject note = this.buildJSONNote();
        try {

            if (this.noteUUID == null) {
                this.noteUUID = UUID.randomUUID().toString();
                note.put("ID", noteUUID);

                success = Singleton.INSTANCE.remoteServer.createNote(Singleton.INSTANCE.userEmail, note.toString(3));
                this.updateParentListView(note, actionEvent);
            }
            else{
                success = Singleton.INSTANCE.remoteServer.updateNote(Singleton.INSTANCE.userEmail, note.toString(3));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (success){
            JOptionPane.showMessageDialog(null, "Note successfully saved...");
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

    private JSONObject buildJSONNote() {
        JSONObject note = new JSONObject();

        note.put("email", Singleton.INSTANCE.userEmail);
        note.put("title", noteTitle.getText());
        note.put("creationDate", creationDateLabel.getText());
        note.put("text", noteTextField.getText());

        return note;
    }
}
