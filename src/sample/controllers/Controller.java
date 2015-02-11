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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import org.json.JSONObject;
import sample.Main;
import sample.Singleton;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    @FXML
    TextField emailAddressTextField;

    @FXML
    PasswordField passwordField;

    @FXML
    Button loginButton;

    @FXML
    Button signUpButton;

    @FXML
    public void login(ActionEvent actionEvent){
        String email = this.emailAddressTextField.getText();
        String password = this.passwordField.getText();

        try {
            Singleton.INSTANCE.userCookie = Singleton.INSTANCE.remoteServer.authenticate(email, password);

            this.closeWindowFromActionEvent(actionEvent);
            this.openMainWindow(email, password);

        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void signUp(ActionEvent actionEvent){
        Window window = this.getWindowFromEvent(actionEvent);
        this.openSignUpWindow(window);
    }

    private void closeWindowFromActionEvent(ActionEvent actionEvent){
        Stage stage = (Stage) this.getWindowFromEvent(actionEvent);
        stage.close();
    }

    private Window getWindowFromEvent(Event event){
        Node sourceNode = (Node) event.getSource();
        Scene sourceScene = sourceNode.getScene();
        return sourceScene.getWindow();
    }

    private void openSignUpWindow(Window window) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/signup.fxml"));
        Parent root = null;

        try {
            root = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = new Stage();
        stage.setTitle("Sign Up");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(window);
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void openMainWindow(String email, String password){
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/mainWindow.fxml"));
        Parent root = null;
        String userPublicInformation = null;

        try{
            root = (Parent) fxmlLoader.load();
            userPublicInformation = Singleton.INSTANCE.remoteServer.retrievePublicUserInformation(email,password);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (userPublicInformation != null) {
            Stage stage = new Stage();
            stage.setTitle("Shared Notes");
            Scene scene = new Scene(root);

            Label usernameLabel = (Label) scene.lookup("#usernameLabel");
            Label emailAddressLabel = (Label) scene.lookup("#emailAddressLabel");
            JSONObject userInfo = new JSONObject(userPublicInformation);

            usernameLabel.setText(userInfo.getString("name"));
            emailAddressLabel.setText(userInfo.getString("email"));

            stage.setScene(scene);
            stage.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
