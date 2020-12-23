package com.example.taoqi.DataManager;

import android.os.Handler;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import Http.HttpConnect;
import TypeConv.JSON;
import Variables.HttpType;
import Variables.ResEnum;
import Variables.ResString;

public class ShopManager extends Client {
    public static class  CallBacks  extends BaseCallBacks {
        @Deprecated
        void AddShopAction() {} ;
    }
    public CallBacks callBacks;

    {
         handler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                System.out.println(msg);
                if (msg.what == ResEnum.OK.ordinal()) {
                    callBacks.ActionSuccess(msg.obj);
                }
            }
        };
    }
    public ShopManager (CallBacks callBacks) throws  Exception{
        super();
        this.callBacks = callBacks;
    }

    public Thread AddShop(int userId, String shopName, String shopAddr, String shopDesp) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    HttpConnect conn = new HttpConnect(url, "POST", true);
                    conn.setJSON();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(HttpType.Type, HttpType.AddShop);
                    map.put(ResString.UserId, String.valueOf(userId));
                    map.put(ResString.ShopName, shopName);
                    map.put(ResString.ShopAddr, shopAddr);
                    map.put(ResString.ShopDesp, shopDesp);
                    conn.Send(JSON.fromMap(map).getBytes("UTF-8"));
                    if(conn.Response().code == HttpURLConnection.HTTP_OK) {
                        handler.sendEmptyMessage(ResEnum.OK.ordinal());
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        };
        thread.start();
        return thread;
    }

    public  Thread GetShopByUserId(String userId) {
        return SendJson(
            new HashMap<String, Object>() {
                {
                    put(HttpType.Type, HttpType.GetShopByUserId);
                    put(ResString.UserId, userId);
                }
            },
            ResEnum.OK.ordinal()
        );
    }
}
