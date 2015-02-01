package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Controller {
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
        this.openSignUpWindow();
    }

    @FXML
    public void signUp(ActionEvent actionEvent){
        System.out.println("Cadastrando...");

    }

    private void openSignUpWindow() {
        FXMLLoader loader;

        loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("sample/views/signup.fxml"));
    }
}
