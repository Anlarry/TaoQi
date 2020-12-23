package Server.Action;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import Server.Manager.UserManagerServer;

@Deprecated
public class LoginAction implements Action {
    private UserManagerServer userManagerServer;
    public LoginAction (UserManagerServer userManagerServer) {
        this.userManagerServer = userManagerServer;
    }
    @Override 
    public String action(Map<String, Object> map) {
        Map<String, Object> res = userManagerServer.Login(
            (String)map.get("Email"), 
            (String)map.get("Password"), 
            (String)map.get("Customer"));
        JSONObject json = new JSONObject(res);
        return json.toString();
    }
}
