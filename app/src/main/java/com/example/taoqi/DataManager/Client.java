package com.example.taoqi.DataManager;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import Http.HttpConnect;
import Http.HttpResponse;
import TypeConv.JSON;
import Variables.HttpType;
import Variables.ResEnum;
import Variables.ResString;

public abstract class Client {
//    public static final String ServerIP = "192.168.31.5" ;
//    private String ServerIP = "192.168.43.247";
    public static final String ServerIP = "10.0.2.2" ;
//    public static final String ServerIP = "10.42.0.1" ;

    public static final String ServerPort = "8000";
    public static String getServerURL() {
        return "http://"+ServerIP+":"+ServerPort;
    }

    protected Handler handler ;

    protected URL url;
    public Client() throws Exception {
        System.out.println("http://"+ServerIP+":"+ServerPort);
        this.url = new URL("http://"+ServerIP+":"+ServerPort);
    }
//    public Client(String ServerIp, String ServerPort) throws  Exception{
//        this.ServerIP = ServerIP;
//        this.ServerIP = ServerIP;
//        this.url = new URL("http://" + ServerIp + ":" + ServerPort);
//    }
    protected Thread SendJson(Map<String, Object> map, int what) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    HttpConnect conn = new HttpConnect(url, "GET", true);
                    conn.setJSON();
                    conn.Send(JSON.fromMap(map).getBytes());
                    HttpResponse response = conn.Response();
                    if(response.MsgIsNotFail()) {
                        Message msg = new Message();
                        msg.what = what;
                        msg.obj = (Object) JSONObject.parse(response.data);
                        handler.sendMessage(msg);
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
}
