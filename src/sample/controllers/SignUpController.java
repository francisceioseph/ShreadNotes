package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.Singleton;

import java.rmi.RemoteException;

/**
 * Created by francisco on 29/01/15.
 */
public class SignUpController{
    @FXML
    public TextField nameTextField;

    @FXML
    public TextField emailTextField;

    @FXML
    public PasswordField passwordTextField;

    @FXML
    public PasswordField confirmationPasswordTextField;

    @FXML
    public void makeRegistration(ActionEvent actionEvent){

        if (this.validateFields())
        {
            try {

                Singleton.INSTANCE.remoteServer.createUser(this.nameTextField.getText(),
                        this.emailTextField.getText(),
                        this.passwordTextField.getText());

                this.populateFieldsOfPreviousWindow(actionEvent);

                this.closeWindowFromActionEvent(actionEvent);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }

    private boolean validateFields() {
        boolean isAllDuty = false;
        boolean isEqualsPasswords = false;

        //Testa se ninguém é vazio
        isAllDuty = !(this.nameTextField.getText().isEmpty() ||
                this.emailTextField.getText().isEmpty() ||
                this.passwordTextField.getText().isEmpty() ||
                this.confirmationPasswordTextField.getText().isEmpty());

        //Testa se temos um email válido.
        if (isAllDuty){
            String password = this.passwordTextField.getText();
            String confirmationPassword = this.confirmationPasswordTextField.getText();

            isEqualsPasswords = password.equals(confirmationPassword);
        }

        return isEqualsPasswords && isAllDuty;
    }

    private void populateFieldsOfPreviousWindow(ActionEvent actionEvent) {

        Stage currentStage = this.getWindowFromActionEvent(actionEvent);
        Scene previousWindowScene = currentStage.getOwner().getScene();
        TextField emailField = (TextField) previousWindowScene.lookup("#emailAddressTextField");
        PasswordField passwordField = (PasswordField) previousWindowScene.lookup("#passwordField");

        emailField.setText(this.emailTextField.getText());
        passwordField.setText(this.passwordTextField.getText());
    }

    private void closeWindowFromActionEvent(ActionEvent actionEvent){
        this.getWindowFromActionEvent(actionEvent).close();
    }

    private Stage getWindowFromActionEvent(ActionEvent actionEvent){
        Node node = (Node) actionEvent.getSource();
        Scene scene = node.getScene();
        Stage stage = (Stage) scene.getWindow();

        return stage;
    }

}
