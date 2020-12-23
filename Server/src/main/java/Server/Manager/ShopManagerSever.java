package Server.Manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import Server.Server;
import TypeConv.JSON;
import Variables.ResString;

public class ShopManagerSever extends ManagerServer {
    public ShopManagerSever(Connection conn) {
        super(conn);
    }
    public void AddShop(int userId, String shopName, String shopDesp, String shopAddr) throws Exception{
        String sql = "insert into Shop(b_id, s_name, s_addr, s_desp) " 
            + "values(%d, '%s', '%s', '%s')";
        // Statement stmt = conn.createStatement();
        Statement stmt = Server.newStatement();
        String exec_sql = String.format(
            sql, userId, shopName, shopAddr, shopDesp
        );
        System.out.println(exec_sql);
        stmt.executeUpdate(exec_sql);
        stmt.close();
    }
    public String GetShopByField(String field, String fieldValue) throws Exception {
        // TODO
        return null;
        // String sql = "select * from "
    }
    public Map<String, String> GetOneShopByField(String field, String fieldValue) throws Exception {
        String sql = "select * from %s where %s = %s";
        String exec_sql = String.format(sql,
            DataBase.Shop,
            field, fieldValue
        );
        System.out.println(exec_sql);
        Statement stmt = Server.newStatement();
        ResultSet res = stmt.executeQuery(exec_sql);
        return parseOneResultSet(res);
    }

    private Map<String, String> parseOneResultSet(ResultSet res) throws Exception {
        res.next();
        Map<String, String> map = new HashMap<String, String>();
        map.put(ResString.ShopId, res.getString(s_id));
        map.put(ResString.ShopName, res.getString(s_name));
        map.put(ResString.ShopAddr, res.getString(s_addr));
        map.put(ResString.ShopTurnOver, res.getString(s_turnover));
        return map;
    } 
}
