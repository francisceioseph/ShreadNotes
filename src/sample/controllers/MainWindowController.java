package sample.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
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
        boolean success = false;
        try {
            success = Singleton.INSTANCE.remoteServer.logout(Singleton.INSTANCE.userEmail);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (success){
            Singleton.INSTANCE.userEmail = null;
            Stage currentWindow = (Stage) this.getWindowFromEvent(actionEvent);
            currentWindow.close();

            this.openLoginWindow();
        }
    }

    @FXML
    public void closeMainWindow(ActionEvent actionEvent){

        try {
            Singleton.INSTANCE.remoteServer.logout(Singleton.INSTANCE.userEmail);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Singleton.INSTANCE.userEmail = null;
        Stage stage = (Stage) notesListView.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void deleteAccount(ActionEvent actionEvent){
        try {
            Singleton.INSTANCE.remoteServer.deleteUser(Singleton.INSTANCE.userEmail);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Singleton.INSTANCE.userEmail = null;
        Stage currentWindow = (Stage) notesListView.getScene().getWindow();
        currentWindow.close();

        this.openLoginWindow();
    }

    @FXML
    public void aboutSharedNotes (ActionEvent actionEvent){
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/about.fxml"));
        Parent root = null;

        try {
            root = (Parent) fxmlLoader.load();

            Stage stage = this.makeWindow("Shared Notes", root);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
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



        this.notesListView.setContextMenu(this.makeListViewContextMenu());
    }


    /*
     *
     * Helper methods for internal class use. :)
     */

    private ContextMenu makeListViewContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem openNoteItem = new MenuItem("Open Note");
        MenuItem deleteNoteItem = new MenuItem("Delete Note");

        openNoteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                Window window = notesListView.getScene().getWindow();
                JSONObject selectedItem = notesListView.getSelectionModel().getSelectedItem();
                openNote(window, selectedItem);
            }
        });

        deleteNoteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                JSONObject selectedItem = notesListView.getSelectionModel().getSelectedItem();
                notesListView.getSelectionModel().clearSelection();

                System.out.print(selectedItem.toString(3));

                boolean success = false;

                try {
                    success = Singleton.INSTANCE.remoteServer.deleteNote(Singleton.INSTANCE.userEmail, selectedItem.getString("ID"));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                if (success){
                    notes.removeAll(selectedItem);
                }

            }
        });

        contextMenu.getItems().addAll(openNoteItem, deleteNoteItem);
        return contextMenu;
    }

    public void populateNotes() {
        Set<String> keySet = this.userNotes.keySet();
        this.notes = FXCollections.observableArrayList();

        for (String key : keySet){
            this.notes.add(this.userNotes.getJSONObject(key));
        }
    }

    private Window getWindowFromEvent(Event event){
        Node sourceNode = (Node) event.getSource();
        Scene sourceScene = sourceNode.getScene();
        return sourceScene.getWindow();
    }

    private void openLoginWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/login.fxml"));
        Parent root = null;

        try {
            root = (Parent) fxmlLoader.load();

            Stage stage = this.makeWindow("Shared Notes", root);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openNewNoteWindow(Window window) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/note.fxml"));
        Parent root = null;

        try {
            root = (Parent) fxmlLoader.load();

            NoteController noteController = fxmlLoader.<NoteController>getController();
            noteController.initialize();

            Stage stage = this.makeWindow("New Note", root);
            stage.initOwner(window);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openNote(Window window, JSONObject note){
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/note.fxml"));
        Parent root = null;

        try {
            root = (Parent) fxmlLoader.load();

            NoteController noteController = fxmlLoader.<NoteController>getController();
            noteController.noteUUID = note.getString("ID");
            String encodedNote = null;

            try {
                encodedNote = Singleton.INSTANCE.remoteServer.retrieveNote(Singleton.INSTANCE.userEmail, noteController.noteUUID);
            }
            catch (RemoteException e){
                e.printStackTrace();
            }

            noteController.note = new JSONObject(encodedNote);
            noteController.initialize();

            Stage stage = this.makeWindow("Note", root);
            stage.initOwner(window);
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

    class CellItem extends ListCell<JSONObject> {
        @Override
        public void updateItem(final JSONObject item, boolean empty) {
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

                layout.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
                            Window window = getWindowFromEvent(mouseEvent);
                            openNote(window, item);
                        }
                    }
                });

                setGraphic(layout);
            }
        }

    }
}
