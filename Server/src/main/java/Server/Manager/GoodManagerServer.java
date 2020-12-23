package Server.Manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import Server.Server;
import Variables.ResString;
import net.coobird.thumbnailator.Thumbnails;
import TypeConv.InStreamByte;
import com.alibaba.fastjson.JSON;

import java.lang.Object;
public class GoodManagerServer extends ManagerServer {
    public GoodManagerServer(Connection conn) throws Exception{
        super(conn);
    }
    public void AddGood
    (String userId, String goodName, String goodKind, String goodDesp, String goodPhoto, String goodPrice)  
        throws Exception
    {
        /* the photo will save at httpserve, for databse, it will only save the photo path */
        String sql = "call AddGood(%s, '%s', '%s', '%s', %s)";
        String exec_sql = String.format(
            sql, userId, goodName, goodKind, goodDesp, goodPrice
        );
        System.out.println(exec_sql);
        Statement stmt;
        try {
            stmt = conn.createStatement();
        }
        catch (Exception e) {
            Server.FlushConnect();
            stmt = Server.getConnection().createStatement();
        } 
        stmt.execute(exec_sql);

        String query_shop_name = String.format(
            "select s_name from Shop where b_id = %s", userId);
        ResultSet res = stmt.executeQuery(query_shop_name);
        res.next();
        String shopName = res.getString(1);
        // String photoPath = "./photo/" + shopName + "_" + goodName + ".jpg";
        String photoPath = shopName + "_" + goodName + ".jpg";
        byte[] photo = Base64.getDecoder().decode(goodPhoto.getBytes());
        File file = new File("./photo/" + photoPath);
        OutputStream out = new FileOutputStream(file);
        out.write(photo);
        out.close();
        stmt.close();
        /* compress the photo */
        Thumbnails.of(new File("./photo/" + photoPath))
            .size(512, 512)
            .toFile(new File("./sphoto/" + photoPath));
    }
    public String GetAllGood() throws Exception{
        String sql = "select g_id, g_name, g_desp,g_photo,g_price from Good;";
        Statement stmt = Server.newStatement();
        ResultSet res =  stmt.executeQuery(sql);
        return GoodSetResult2String(res);
    }
    public String GetGoodByName(String name) throws Exception {
        String sql = "select g_id, g_name, g_desp,g_photo,g_price from Good where g_name like '%%%s%%'";
        String exec_sql = String.format(sql, name);
        System.out.println(exec_sql);
        Statement stmt = Server.newStatement();
        ResultSet res =  stmt.executeQuery(exec_sql);
        return GoodSetResult2String(res);
    }
    public String BuyGood(String userId, String goodId) throws SQLException {
        String sql = "call BuyGood(%s, %s)";
        Statement stmt = Server.newStatement();
        ResultSet res = stmt.executeQuery(
            String.format(sql, userId, goodId)
        );
        res.next();
        return res.getString(1);
    }

    
    public String GetGoodByField(String field, String fieldValue) throws Exception {
        String sql = "select * from %s g where %s = %s";
        String exec_sql = String.format(sql, 
            DataBase.Good, 
            field, fieldValue
        );
        System.out.println(exec_sql);
        Statement stmt = Server.newStatement();
        ResultSet res = stmt.executeQuery(exec_sql);
        return GoodSetResult2String(res);
    }

    public String GetGoodById(List<String> goodId) throws SQLException {
        // String sql = 
        // TODO
        return null;
    } 
    public String GetPurchasedGood(String userId) throws Exception {
        String sql = "select %s, %s, %s, %s, %s from %s g where g_id in "
            + "(select %s from %s t where %s = %s)";
        String exec_sql = String.format(sql,
            g_id, g_name, g_desp, g_photo, g_price, DataBase.Good, 
            g_id, DataBase.Trade, 
            c_id, userId
        );
        System.out.println(exec_sql);
        Statement stmt = Server.newStatement();
        ResultSet res = stmt.executeQuery(exec_sql);
        return GoodSetResult2String(res);
    }   

    /* g_name, g_desp, g_photo, g_price */
    private String GoodSetResult2String(ResultSet res) throws Exception {
        List<Object> json = new ArrayList<Object>();
        while(res.next()) {
            HashMap<String, String> good = new HashMap<String, String>();
            good.put(ResString.GoodId, res.getString("g_id"));
            good.put(ResString.GoodName, res.getString("g_name"));
            good.put(ResString.GoodDesp, res.getString("g_desp"));
            good.put(ResString.GoodPrice, res.getString("g_price"));
            good.put(ResString.GoodPhotoPath, res.getString("g_photo"));
            // String photoPath = res.getString("g_photo");
            // File file = new File("./photo/" + photoPath);
            // byte[] photo = InStreamByte.InStream2Byte(new FileInputStream(file));
            // good.put(ResString.GoodPhoto, 
            //     Base64.getEncoder().encodeToString(photo)
            // );
            json.add((Object)good);
        }
        return JSON.toJSONString(json);
    }

    
}
