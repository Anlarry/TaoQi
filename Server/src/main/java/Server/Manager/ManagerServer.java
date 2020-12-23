package Server.Manager;

import java.sql.Connection;

/*
 * The Manager Server is who really interact with mysqld
 * 
 * Action will call Manager to finish some task
 * 
 */

public abstract class ManagerServer {

    /*data base */
    public static class DataBase {
        public static final String Customer = "Customer";
        public static final String Good = "Good";
        public static final String Trade = "Trade";
        public static final String Shop = "Shop";
    }

    /* field name */
    public static final String c_name = "c_name";
    public static final String c_id = "c_id";
    public static final String c_email = "c_email";
    public static final String c_addr = "c_addr";
    public static final String c_balance = "c_balance";
    public static final String c_photo = "c_photo";

    public static final String g_id = "g_id";
    public static final String g_name = "g_name";
    public static final String g_desp = "g_desp";
    public static final String g_photo = "g_photo" ;
    public static final String g_price = "g_price";

    public static final String s_id = "s_id";
    public static final String b_id = "b_id";
    public static final String s_name = "s_name";
    public static final String s_addr = "s_addr";
    public static final String s_desp = "s_desp";
    public static final String s_visits = "s_visits";
    public static final String s_star = "s_star";
    public static final String s_turnover = "s_turnover";



    final protected Connection conn;
    public ManagerServer(Connection conn){
        this.conn = conn;
    }   
    protected void finalize() throws Throwable{
        // conn.close();
    }
}
