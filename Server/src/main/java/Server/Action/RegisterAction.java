package Server.Action;

import java.util.Map;

import Server.Manager.UserManagerServer;
import Variables.ResString;

@Deprecated
public class RegisterAction implements Action{
    private UserManagerServer userManagerServer;
    public RegisterAction(UserManagerServer userManagerServer) {
        this.userManagerServer = userManagerServer;
    }
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
}
