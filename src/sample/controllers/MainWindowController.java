package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import org.json.JSONObject;
import sample.Main;
import sample.Singleton;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Created by francisco on 29/01/15.
 */
public class MainWindowController implements Initializable{
    public JSONObject userNotes;

    @FXML
    ListView<JSONObject> notesListView;

    public ObservableList<JSONObject> notes;

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
        try {
            String allNotesJSONString = Singleton.INSTANCE.remoteServer.listAllNotes(Singleton.INSTANCE.userEmail);

            if (allNotesJSONString != null) {
                this.userNotes = new JSONObject(allNotesJSONString);
                this.populateNotes();
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        this.notesListView.setItems(this.notes);
        this.notesListView.setCellFactory(new Callback<ListView<JSONObject>, ListCell<JSONObject>>() {
            @Override
            public ListCell<JSONObject> call(ListView<JSONObject> jsonObjectListView) {
                return new CellItem();
            }
        });
    }

    public void populateNotes() {
        Set<String> keySet = this.userNotes.keySet();
        this.notes = FXCollections.observableArrayList();

        for (String key : keySet){
            this.notes.add(this.userNotes.getJSONObject(key));
        }
    }
    /*
     *
     * Helper methods for internal class use. :)
     */

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
            this.configureNewNoteWindow(stage.getScene());
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

    private void configureNewNoteWindow(Scene scene){
        TextField noteTitle = (TextField) scene.lookup("#noteTitle");
        Label dateField = (Label) scene.lookup("#creationDateLabel");
        Button edit = (Button) scene.lookup("#editButton");
        Button save = (Button) scene.lookup("#saveButton");
        TextArea textArea = (TextArea) scene.lookup("#noteTextField");
        String creationDate = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
        String creationHour = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
        noteTitle.setText("New Note");
        noteTitle.setEditable(true);
        dateField.setText(String.format("Creation date: %s at %s", creationDate, creationHour));
        edit.setDisable(true);
        save.setDisable(false);
        textArea.setEditable(true);
    }

    static class CellItem extends ListCell<JSONObject> {
        @Override
        public void updateItem(JSONObject item, boolean empty) {
            super.updateItem(item, empty);
            Label labelTitle = new Label();
            Label labelSubtitle = new Label();
            VBox layout = new VBox();

            if (item != null){
                labelTitle.setText(item.getString("title"));
                labelTitle.setFont(new Font(15.0));
                labelSubtitle.setText(item.getString("creationDate"));
                labelSubtitle.setFont(new Font(10.0));
                layout.getChildren().addAll(labelTitle, labelSubtitle);
                setGraphic(layout);
            }
        }
    }
}
