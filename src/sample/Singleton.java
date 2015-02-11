package sample;

/**
 * Created by francisco on 07/02/15.
 */
public enum Singleton {
    INSTANCE;

    public String userCookie;
    public String userName;
    public String serverIPAddr;
    public int serverPort;

    public SharedNotesInterface remoteServer;
}
