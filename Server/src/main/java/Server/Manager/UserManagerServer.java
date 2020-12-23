
package Server.Manager;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import Server.Server;
import Variables.ResString;

/**
 * UserManagerServer
 */


public class UserManagerServer extends ManagerServer {

    public UserManagerServer(Connection conn){
        super(conn);
    }   
    boolean CheckUserName(String username) {
        String sql = "select count(*) from Customer where c_name = %s";
        try {
            Statement stmt = conn.createStatement();
            ResultSet res =  stmt.executeQuery(String.format(sql, username));
            boolean findUserName = (res.getInt(0) != 0) ? true : false;
            stmt.close();
            return findUserName;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    } 
    
    public boolean Register(String email, String username, String password, String address, String Customer) {
        String exec_sql;
        if(Customer.equals("true")) {
            String sql = "insert into Customer(c_name, c_passwd, c_email, c_addr) "
                + " values('%s', '%s', '%s', '%s')";
            exec_sql = String.format(
                sql,username, password, email, address
            );
        }
        else {
            String sql = "insert into Business(b_name, b_passwd, b_email)"
                + " values('%s', '%s', '%s')";
            exec_sql = String.format(
                sql, username, password, email
            );
        }
        try {
            // Statement stmt = conn.createStatement();
            Statement stmt = Server.newStatement();
            stmt.executeUpdate(exec_sql);
            stmt.close();
            return true;
        }
        catch (SQLException e) {
            System.out.print(e.getMessage());
            return false ;
        }
    }

    public Map<String, Object> Login(String email, String password, String isCustomer) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(ResString.LoginStatus, ResString.LoginFail);
        String exec_sql = null;
        boolean Customer = Boolean.parseBoolean(isCustomer);
        if(Customer) {
            String customer_sql = "select c_id, c_name from Customer where c_email = '%s' and c_passwd = '%s'";
            exec_sql = String.format(customer_sql, email, password);
        }
        else {
            String bussiness_sql =  "select b_id, b_name from Business where b_email = '%s' and b_passwd = '%s'";
            exec_sql = String.format(bussiness_sql ,email, password);
        }
        System.out.println(exec_sql);
        try {
            // Statement stmt = conn.createStatement();
            Statement stmt = Server.newStatement();
            ResultSet res = stmt.executeQuery(
                exec_sql
            );
            res.next();
            int userId = res.getInt(1);
            String username = res.getString(2);
            if(username != null && !username.equals(""))   {
                System.out.printf("%s Login Successful\n", email);
                map.put(ResString.LoginStatus, ResString.LoginSuccess);
                map.put(ResString.UserId, String.valueOf(userId));
                map.put(ResString.UserName, username);
                map.put(ResString.Email, email);
                map.put(ResString.Customer, isCustomer);
            } 
            res.close();
            stmt.close();
            return map;
        }
        catch (SQLException e) {
            System.out.print(e.getMessage());
            return map;
        }
    }   

    public Map<String, Object> GetCustomerInfoById(String userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        String sql = "select * from Customer where c_id = %s";
        // System.out.println(String.format(sql, userId));
        try {
            Statement stmt = Server.newStatement();
            ResultSet res = stmt.executeQuery(
                String.format(sql, userId)
            );
            res.next();
            map.put(ResString.UserName, res.getString(c_name));
            map.put(ResString.UserId, res.getString(c_id));
            map.put(ResString.UserBalance, res.getString(c_balance));
            map.put(ResString.UserAddress, res.getString(c_addr));
            map.put(ResString.Email, res.getString(c_email));
            res.close();
            stmt.close();
            return map;
        }
        catch (SQLException e) {
            System.err.print(e.getMessage());
            return map;
        }
        
    };

    public void setCumstomerInfoById(String userId, String field, String newValue) {
        String sql = "update %s set %s = %s where %s = %s";
        String exce_sql = String.format(sql, 
            DataBase.Customer, field, newValue, c_id, userId
        );
        try{
            Statement stmt = Server.newStatement();
            stmt.executeUpdate(exce_sql);
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(exce_sql);
        }        
    }
}