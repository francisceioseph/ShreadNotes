package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("views/login.fxml"));
        primaryStage.setTitle("Shared Notes");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        String serviceURL;

        configureNameServerAddress();
        serviceURL = makeServiceURL();

        try {

            Registry registry = LocateRegistry.getRegistry(Singleton.INSTANCE.serverIPAddr, Singleton.INSTANCE.serverPort);
            Singleton.INSTANCE.remoteServer = (SharedNotesInterface) registry.lookup(serviceURL);

            launch(args);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }


    }

    private static void configureNameServerAddress(){
        Singleton.INSTANCE.serverIPAddr = "localhost";
        Singleton.INSTANCE.serverPort = 56789;
    }

    private static String makeServiceURL(){

         return String.format("rmi://%s:%d/SharedNotes",
                Singleton.INSTANCE.serverIPAddr,
                Singleton.INSTANCE.serverPort);


    }
}
