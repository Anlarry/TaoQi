package Server;

import java.sql.*;
import java.io.File;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.*;

import Server.Action.*;
import Server.Handler.Handler;
import Server.Handler.PhotoHandler;
import Server.Manager.GoodManagerServer;
import Server.Manager.ShopManagerSever;
import Server.Manager.UserManagerServer;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;



public class Server {
    final static String username = "root";
    final static String password = "manjaro";
    final static String ClassName = "com.mysql.cj.jdbc.Driver";
    
    static final String DBIP = "10.62.220.218";
    static final String DBPORT = "3306";

    final static String ServerIP = "localhost";
    final static String ServerPort = "8000";

    private static Connection conn;
    public static Connection getConnection() {
        return conn;
    }
    public static Statement newStatement() throws SQLException{
        try {
            Statement stmt = conn.createStatement();
            return stmt;
        }
        catch (Exception e) {
            FlushConnect();
            Statement stmt = conn.createStatement();
            return stmt;
        }
    }
    public static void FlushConnect() {
        try {
            conn = DriverManager.getConnection(
                String.format("jdbc:mysql://%s:%s/TaoQi", DBIP, DBPORT),
                username,
                password
            );
        }       
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static public UserManagerServer userManagerServer;
    static public ShopManagerSever shopManagerSever;
    static public GoodManagerServer goodManagerServer; 

    private HttpServer httpServer;
    
    public static void main(String[] args) throws Exception {

        Server server = new Server();
        server.Start();
    }


    public Server () throws Exception {
        /* connect to mysql */ 
        Class.forName(ClassName);
        conn = DriverManager.getConnection(
            String.format("jdbc:mysql://%s:%s/TaoQi", DBIP, DBPORT),
            username,
            password
        );
        userManagerServer = new UserManagerServer(conn);
        shopManagerSever = new ShopManagerSever(conn);
        goodManagerServer = new GoodManagerServer(conn);

        httpServer = HttpServer.create(new InetSocketAddress(Integer.parseInt(ServerPort)), 0);
        Handler handler = new Handler();
        httpServer.createContext("/", handler);
        PhotoHandler handler_photo = new PhotoHandler();
        httpServer.createContext("/photo/", handler_photo);

        // handler.setRegisterAction(new RegisterAction(userManagerServer));
        // handler.setLoginAction(new LoginAction(userManagerServer));
        handler.setUserActions(new UserActions(userManagerServer));
        handler.setAddShopAction(new AddShopAction(shopManagerSever));
        // handler.setAddGoodAction(new AddGoodAction(goodManagerServer));
        handler.setGoodActions(new GoodActions(goodManagerServer));
        
    }
    
    public void Start() {
        httpServer.start();
        System.out.println("Start Listening ... ");
    }

    @Override
    protected void finalize() throws Throwable {
        conn.close();
    }
    
}

