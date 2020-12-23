package Server.Action;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import Server.Manager.UserManagerServer;
import Variables.ResString;

import Server.Manager.ManagerServer;

public class UserActions {
    private UserManagerServer userManagerServer;
    public UserActions(UserManagerServer userManagerServer) {
        this.userManagerServer = userManagerServer;
    }
    public Action LoginAction = new Action() {
        @Override 
        public String action(Map<String, Object> map) {
            Map<String, Object> res = userManagerServer.Login(
                (String)map.get("Email"), 
                (String)map.get("Password"), 
                (String)map.get("Customer"));
            JSONObject json = new JSONObject(res);
            return json.toString();
        }
    };
    public Action RegisterAction = new Action() {
        @Override 
        public String action(Map<String, Object> map) {
            boolean res = userManagerServer.Register(
                (String)map.get("Email"), 
                (String)map.get("UserName"),
                (String)map.get("Password"), 
                (String)map.get("Address"), 
                (String)map.get("Customer")
            );
            if (res) {
                return ResString.RegisterSuccess;
            }
            else {
                return ResString.RegisterFail;
            }
        }
    };
    
    public Action GetCustomterInfoById = new Action() {
        @Override 
        public String action(Map<String, Object> map) {
            Map<String, Object> res = userManagerServer.GetCustomerInfoById(
                (String)map.get(ResString.UserId)
            );
            // System.out.println((String)res.get(map.get(ResString.Field)));
            return (String)res.get(map.get(ResString.Field));
        }
    };

    public Action Recharge = new Action() {
        @Override
        public String action(Map<String, Object> map) {
            try {
                Map<String, Object> res = userManagerServer.GetCustomerInfoById(
                    (String)map.get(ResString.UserId)
                    );
                int balance = Integer.parseInt((String)res.get(ResString.UserBalance));
                balance +=  Integer.parseInt((String)map.get(ResString.RechargeAmount));
                userManagerServer.setCumstomerInfoById(
                (String)map.get(ResString.UserId), ManagerServer.c_balance, String.valueOf(balance));
                res = userManagerServer.GetCustomerInfoById((String)map.get(ResString.UserId));
                return (String)res.get(ResString.UserBalance);
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
                return ResString.Fail;
            }
        }
    };

}
