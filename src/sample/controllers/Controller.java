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

import javax.swing.*;
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
        boolean loginSuccess = false;

        if (!email.isEmpty() && !password.isEmpty()){
            try {
                loginSuccess = Singleton.INSTANCE.remoteServer.login(email, password);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        if (loginSuccess){
            Singleton.INSTANCE.userEmail = email;
            this.closeWindowFromActionEvent(actionEvent);
            this.openMainWindow(email, password);
        }
        else{
            JOptionPane.showMessageDialog(null, "Erro de Login, tente novamente.");
            this.emailAddressTextField.clear();
            this.passwordField.clear();
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

            Stage stage = this.makeWindow("Sign Up", root);
            stage.initModality(Modality.APPLICATION_MODAL);
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

    private void openMainWindow(String email, String password){
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/mainWindow.fxml"));
        Parent root = null;
        String userPublicInformation = null;

        try{
            root = (Parent) fxmlLoader.load();
            userPublicInformation = Singleton.INSTANCE.remoteServer.retrieveUser(email);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (userPublicInformation != null) {
            JSONObject userInfo = new JSONObject(userPublicInformation);

            Stage stage = this.makeWindow("Shared Notes", root);
            Scene scene = stage.getScene();

            Label usernameLabel = (Label) scene.lookup("#usernameLabel");
            Label emailAddressLabel = (Label) scene.lookup("#emailAddressLabel");

            usernameLabel.setText(userInfo.getString("name"));
            emailAddressLabel.setText(userInfo.getString("email"));
            stage.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
