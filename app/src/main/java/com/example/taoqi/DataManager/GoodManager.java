package com.example.taoqi.DataManager;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ThemedSpinnerAdapter;

import java.net.HttpURLConnection;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import Http.HttpResponse;

import com.alibaba.fastjson.JSONObject;
import com.example.taoqi.TypeConv.BitmapByte;
import TypeConv.JSON;
import Variables.HttpType;
import Variables.ResEnum;
import Variables.ResString;
import Http.HttpConnect;


public class GoodManager extends Client {
    public static abstract class CallBacks extends BaseCallBacks{
        public void AddGooadAction(){} ;
        public void AddGoodFailAction() {};
        public void GetGoodAction(Object data) {};
    }
    public CallBacks callBacks;

    {
        handler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                System.out.println(msg);
                if (msg.what == ResEnum.AddGoodOK.ordinal()) {
                    callBacks.AddGooadAction();
                } else if (msg.what == ResEnum.GetGoodOK.ordinal()) {
                    callBacks.GetGoodAction(msg.obj);
                } else if (msg.what == ResEnum.OK.ordinal()) {
                    callBacks.ActionSuccess(msg.obj);
                } else if (msg.what == ResEnum.Fail.ordinal()) {
                    callBacks.ActionFail();
                } else {
                    callBacks.ActionFail();
                }
            }
        };
    }

    public GoodManager(CallBacks callBacks) throws  Exception {
        super();
        this.callBacks = callBacks;
    }
    public Thread AddGood(int userId, String goodName, String goodKind, String description, Bitmap photo, int goodPrice) {
        Thread thread = new Thread() {
            @Override
            public void run () {
                try {
                    HttpConnect conn = new HttpConnect(url, "POST", true);
                    conn.setJSON();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(HttpType.Type, HttpType.AddGood);
                    map.put(ResString.UserId, String.valueOf(userId));
                    map.put(ResString.GoodName,goodName);
                    map.put(ResString.GoodKind, goodKind);
                    map.put(ResString.GoodDesp, description);
                    map.put(ResString.GoodPrice, String.valueOf(goodPrice));
                    String encoded = Base64.getEncoder().encodeToString(BitmapByte.toByte(photo));
                    map.put(ResString.GoodPhoto, encoded);
//                    map.put(ResString.GoodPhoto, new String(BitmapByte.toByte(photo))); // Error conv !!!

//                    String encoded = Base64.getEncoder().encodeToString(BitmapByte.toByte(photo));
//                    map.put(ResString.GoodPhoto, encoded);
                    conn.Send(JSON.fromMap(map).getBytes());
                    HttpResponse response = conn.Response();
                    String msg = new String(response.data);
                    System.out.print(msg);
                    if(response.code == HttpURLConnection.HTTP_OK && !msg.equals(ResString.Fail)) {
                        handler.sendEmptyMessage(ResEnum.AddGoodOK.ordinal());
                    }
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                    handler.sendEmptyMessage(ResEnum.AddGoodFail.ordinal());
                }
            }
        };
        thread.start();
        return thread;
    }

    public Thread GetAllGood() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    HttpConnect conn = new HttpConnect(url, "GET", true);
                    conn.setJSON();
                    conn.setGetTypeAndSend(HttpType.GetAllGoods);
                    HttpResponse response = conn.Response();
                    String msg = new String(response.data);
                    System.out.print(msg);
                    if(response.code == 200 && !msg.equals(ResString.Fail)) {
                        Message msg_resp = new Message();
                        msg_resp.what = ResEnum.GetGoodOK.ordinal();
                        msg_resp.obj = JSONObject.parse(response.data);
                        handler.sendMessage(msg_resp);
                    }
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                    handler.sendEmptyMessage(ResEnum.GetAllGoodFail.ordinal());
                }
            }
        };
        thread.start();
        return thread;
    }

    public Thread GetGoodByName (String name) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    HttpConnect conn = new HttpConnect(url, "GET", true);
                    conn.setJSON();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(HttpType.Type ,HttpType.GetGoodByName);
                    map.put(ResString.GoodName, name);
                    conn.Send(JSON.fromMap(map).getBytes());
                    HttpResponse response = conn.Response();
                    String msg = new String(response.data);
                    System.out.print(msg);
                    if(response.code == 200 && !msg.equals(ResString.Fail)) {
                        Message msg_resp = new Message();
                        msg_resp.what = ResEnum.GetGoodOK.ordinal();
                        msg_resp.obj = JSONObject.parse(response.data);
                        handler.sendMessage(msg_resp);
                    }
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                    handler.sendEmptyMessage(ResEnum.GetAllGoodFail.ordinal());
                }
            }
        };
        thread.start();
        return thread;
    }

    public Thread GetPurchasedGood(int userId) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    HttpConnect conn = new HttpConnect(url, "GET", true);
                    conn.setJSON();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(HttpType.Type ,HttpType.GetPurchasedGood);
                    map.put(ResString.UserId, String.valueOf(userId));
                    conn.Send(JSON.fromMap(map).getBytes());
                    HttpResponse response = conn.Response();
                    String msg = new String(response.data);
                    System.out.print(msg);
                    if(response.code == 200 && !msg.equals(ResString.Fail)) {
                        Message msg_resp = new Message();
                        msg_resp.what = ResEnum.GetGoodOK.ordinal();
                        msg_resp.obj = JSONObject.parse(response.data);
                        handler.sendMessage(msg_resp);
                    }
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                    handler.sendEmptyMessage(ResEnum.GetAllGoodFail.ordinal());
                }
            }
        };
        thread.start();
        return thread;
    }

    public Thread BuyGood(String userId, String goodId) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    HttpConnect conn = new HttpConnect(url, "POST", true);
                    conn.setJSON();
                    Map<String, String> map = new HashMap<String, String> ();
                    map.put(HttpType.Type, HttpType.BuyGood);
                    map.put(ResString.UserId, userId);
                    map.put(ResString.GoodId, goodId);
                    conn.Send(JSON.fromMap(map).getBytes());
                    HttpResponse response = conn.Response();
                    if(response.MsgIsNotFail()) {
                        Message msg = new Message();
                        msg.what = ResEnum.OK.ordinal();
                        msg.obj = (Object) new String(response.data);
                        handler.sendMessage(msg);
//                        handler.sendEmptyMessage(ResEnum.OK.ordinal());
                    }
                    else {
                        handler.sendEmptyMessage(ResEnum.Fail.ordinal());
                    }
                }
                catch (Exception e) {
                    handler.sendEmptyMessage(ResEnum.Fail.ordinal());
                }
            }
        };
        thread.start();
        return thread;
    }

    public Thread GetGoodByBusiness(int userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(HttpType.Type, HttpType.GetGoodByBusiness);
        map.put(ResString.UserId, String.valueOf(userId));
        return SendJson(map, ResEnum.GetGoodOK.ordinal());
    }

    public Thread UpdateGoodById(String goodId, String goodName, String goodKind, String description, Bitmap photo, String goodPrice) {
        String photo_base64;
        try {
             photo_base64 = Base64.getEncoder().encodeToString(BitmapByte.toByte(photo));
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
        return SendJson(
            new HashMap<String, Object>()
            {
                {
                    put(HttpType.Type, HttpType.UpdateGoodById);
                    put(ResString.GoodId, goodId);
                    put(ResString.GoodName, goodName);
                    put(ResString.GoodKind, goodKind);
                    put(ResString.GoodDesp, description);
                    put(ResString.GoodPhoto, photo_base64);
                    put(ResString.GoodPrice, goodPrice);
                }
            },
            ResEnum.OK.ordinal()
        );
    }
}
