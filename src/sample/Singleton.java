package sample;

/**
 * Created by francisco on 07/02/15.
 */
public enum Singleton {
    INSTANCE;

    public String userToken;
    public String serverIPAddr;
    public int serverPort;

    public SharedNotesInterface remoteServer;
}
