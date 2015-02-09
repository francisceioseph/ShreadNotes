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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import sample.Main;

import java.io.IOException;
import java.net.URL;
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
        System.out.println("Login");
    }

    @FXML
    public void signUp(ActionEvent actionEvent){
        System.out.println("Cadastrando...");

        Window window = this.getWindowFromEvent(actionEvent);
        this.openSignUpWindow(window);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
