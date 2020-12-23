package com.example.taoqi.DataManager;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.*;
import com.example.taoqi.user.UserInfo;

import Http.HttpConnect;
import Http.HttpResponse;
import TypeConv.JSON;
import Variables.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class UserManager extends Client {
    private  UserManagerCallBack userManagerCallBack ;

    {
        handler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                System.out.println(msg);
                System.out.println("Handle");
                if (msg.what == ResEnum.RegisterOK.ordinal()) {
                    userManagerCallBack.RegisterCallBack();
                } else if (msg.what == ResEnum.LoginOK.ordinal()) {
                    userManagerCallBack.LoginCallBack();
                } else if (msg.what == ResEnum.OK.ordinal()) {
                    userManagerCallBack.ActionSuccess( msg.obj);
                } else {
                    userManagerCallBack.ActionFail();
                }
            }
        };
    }
    private UserInfo userInfo;
    private boolean LoginStates, RegisterStatus;
    public boolean getLoginStates() {
        return LoginStates;
    }
    public boolean getRegisterStatus() {
        return  RegisterStatus;
    }
    public UserInfo getUserInfo() {
        return userInfo;
    }

    public UserManager() throws Exception{
        super();
    }
    public UserManager(UserManagerCallBack userManagerCallBack) throws  Exception {
        super();
        this.userManagerCallBack= userManagerCallBack;
    }
//    public UserManager(String SeverIP, String ServerPort) throws Exception{
//        super(SeverIP, ServerPort);
//    }
    public void setUserManagerCallBack(UserManagerCallBack userManagerCallBack) {
        this.userManagerCallBack = userManagerCallBack;
    }
    public Thread Register (String email, String username, String password, String address, boolean isCustomer) {
        Integer res;
        Thread thread = new  Thread() {
            @Override
            public void run() {
                try {
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-type", "application/json");
                    OutputStream outstream = conn.getOutputStream();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("Type", HttpType.Register);
                    map.put("Email", email);
                    map.put("UserName",username);
                    map.put("Password",Password2SHA1(password));
                    map.put("Address",address);
                    map.put("Customer",isCustomer ? "true" : "false");
                    JSONObject jsonObject = new JSONObject(map);
                    System.out.println(jsonObject.toString());
                    outstream.write(jsonObject.toString().getBytes("UTF-8"));
                    outstream.close();


                    int responseCode = conn.getResponseCode();
                    if(responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = conn.getInputStream();
                        String result = "";
                        int data = inputStream.read();
                        while(data != -1) {
                            result += (char)data;
                            data = inputStream.read();
                        }
                        inputStream.close();
                        System.out.println(result);
                        if(result.equals(ResString.RegisterSuccess)) {
                            RegisterStatus = true;
                            handler.sendEmptyMessage(ResEnum.RegisterOK.ordinal());
                        }
                        else {
                            RegisterStatus = false;
                            handler.sendEmptyMessage(ResEnum.RegisterFail.ordinal());
                        }
                    }
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        };
        thread.start();
        return thread;
    }

    public void CheckUserName() {
    }

    public Thread GetCustomerInfoById(int userId, String field ) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    HttpConnect conn = new HttpConnect(url, "GET", true);
                    conn.setJSON();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(HttpType.Type ,HttpType.GetCustomerInfoById);
                    map.put(ResString.UserId, String.valueOf(userId));
                    map.put(ResString.Field, field);
                    conn.Send(JSON.fromMap(map).getBytes());
                    HttpResponse response = conn.Response();
                    if(response.MsgIsNotFail()) {
                        Message msg = new Message();
                        msg.what = ResEnum.OK.ordinal();
//                        JSONObject json = JSONObject.parseObject(new String(response.data));
//                        msg.obj = json.get(field);
                        msg.obj = new String(response.data);
                        handler.sendMessage(msg);
                    }
                }
                catch (Exception e) {
                    System.err.println(e.getMessage());
                    handler.sendEmptyMessage(ResEnum.Fail.ordinal());
                }
            }
        };
        thread.start();
        return thread;
    }

    public Thread Login(String email, String password, boolean Customer) {
         Thread thread =  new Thread() {
            @Override
            public void run() {
                try{
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-type", "application/json");
                    OutputStream outstream = conn.getOutputStream();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("Type", HttpType.Login);
                    map.put("Customer" , Customer ? "true" : "false");
                    map.put("Email", email);
                    map.put("Password", Password2SHA1(password));
                    JSONObject jsonObject = new JSONObject(map);
                    System.out.println(jsonObject.toString());
                    outstream.write(jsonObject.toString().getBytes("UTF-8"));
                    outstream.close();

                    int responseCode = conn.getResponseCode();
                    if(responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = conn.getInputStream();
                        String result = "";
                        int data = inputStream.read();
                        while(data != -1) {
                            result += (char)data;
                            data = inputStream.read();
                        }
                        inputStream.close();
                        System.out.println(result);
                        Map<String, Object> resMap = (Map<String, Object>)JSONObject.parseObject(result);
                        if(resMap.get(ResString.LoginStatus).equals(ResString.LoginSuccess)) {
                            LoginStates = true;
                            userInfo = new UserInfo(
                                    Integer.parseInt((String)resMap.get(ResString.UserId)),
                                    (String)resMap.get(ResString.UserName),
                                    (String)resMap.get(ResString.Email),
                                    Boolean.parseBoolean((String)resMap.get(ResString.Customer)));
                            handler.sendEmptyMessage(ResEnum.LoginOK.ordinal());
                        }
                        else {
                            LoginStates = false ;
                            handler.sendEmptyMessage(ResEnum.LoginFail.ordinal());
                        }
                    }
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        };
        thread.start();
        return thread;
    }

    public Thread Recharge(int userId, int rechargeAmount) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    HttpConnect conn = new HttpConnect(url, "POST", true);
                    conn.setJSON();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(HttpType.Type, HttpType.Recharge);
                    map.put(ResString.UserId, String.valueOf(userId)) ;
                    map.put(ResString.RechargeAmount, String.valueOf(rechargeAmount));
                    conn.Send(JSON.fromMap(map).getBytes());
                    HttpResponse response = conn.Response();
                    if(response.MsgIsNotFail()) {
                        Message msg = new Message();
                        msg.what = ResEnum.OK.ordinal();
                        msg.obj = new String(response.data);
                        handler.sendMessage(msg);
                    }
                    else {
                        handler.sendEmptyMessage(ResEnum.Fail.ordinal());
                    }
                }
                catch (Exception e ) {
                    System.out.print(e.getMessage());
                    handler.sendEmptyMessage(ResEnum.Fail.ordinal());
                }
            }
        };
        thread.start();
        return thread;
    }
    public Thread setCusomterField(int userId, String field, Object newValue) {
        // TODO
        Thread thread =  new Thread() {
            @Override
            public void run() {

            }
        };
        thread.start();
        return thread;
    }

    private String Password2SHA1(String password) throws Exception{
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        byte[] result = messageDigest.digest(password.getBytes());
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
