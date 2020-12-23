package Server.Handler;
import com.sun.net.httpserver.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.InputStreamReader;

import java.util.Map;
import Variables.*;
import com.alibaba.fastjson.*;
import Server.Action.*;

public class Handler implements HttpHandler{
    private Action nullAction = new Action() {
        @Override
        public String action(Map<String, Object> map) {
            return null;
        }
    };
    private Action registerAction = nullAction;
    private Action loginAction = nullAction;
    // private Action addGoodAction = nullAction;
    private UserActions userActions;
    private GoodActions goodActions;
    private Action addShopAction = nullAction;

    @Deprecated
    public void setRegisterAction(Action action) {
        registerAction = action;
    };
    @Deprecated
    public void setLoginAction(Action action) {
        loginAction = action ;
    }
    public void setUserActions(UserActions userActions) {
        this.userActions = userActions;
    }
    public void setGoodActions(GoodActions goodActions) {
        this.goodActions = goodActions;
    }
    public void setAddShopAction(Action addShopAction) {
        this.addShopAction = addShopAction;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException{
        String response = "test message";
        exchange.sendResponseHeaders(200, 0);
        OutputStream os = exchange.getResponseBody();
        InputStream in = exchange.getRequestBody();
        Reader reader = new InputStreamReader(in, "UTF-8");
        String json_str = "";
        while(true ) {
            int data = reader.read();
            if(data < 0 ) break;
            json_str = json_str + (char)data;
        }
        // System.out.println(json_str);
        System.out.println(" [\033[1;33m Recv \033[0m] " + json_str);
        JSONObject json = JSONObject.parseObject(json_str);
        Map<String, Object> map = (Map<String, Object>)json;
        switch ((String)map.get("Type")) {
            case HttpType.Register -> response = userActions.RegisterAction.action(map);
            case HttpType.Login -> response = userActions.LoginAction.action(map);
            case HttpType.AddShop ->response = addShopAction.action(map);
            case HttpType.AddGood -> response = goodActions.AddGoodAction.action(map);
            case HttpType.GetAllGoods -> response = goodActions.GetAllGoodAction.action(map);
            case HttpType.GetGoodByName  -> response = goodActions.GetGoodByNameAction.action(map);
            case HttpType.BuyGood -> response = goodActions.BuyGood.action(map);
            case HttpType.GetCustomerInfoById -> response = userActions.GetCustomterInfoById.action(map);
            case HttpType.Recharge -> response = userActions.Recharge.action(map);
            case HttpType.GetPurchasedGood -> response = goodActions.GetPurchased.action(map);
            case HttpType.GetGoodByBusiness -> response = goodActions.GetGoodByBusiness.action(map);
        }
        System.out.println(" [\033[1;32m Result \033[0m] " + response);
        os.write(response.getBytes("UTF-8"));
        os.close();
    }
	public Handler() {
	}

}

