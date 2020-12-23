package Server.Action;

import java.util.Map;

import Server.Manager.ShopManagerSever;
import Variables.ResString;

public class AddShopAction implements Action{ 
    private ShopManagerSever shopManagerSever;
    public AddShopAction(ShopManagerSever shopManagerSever) {
        this.shopManagerSever = shopManagerSever;
    }
    @Override
    public String action(Map<String, Object> map) {
        try {
            shopManagerSever.AddShop(
                Integer.parseInt((String)map.get(ResString.UserId)),
                (String)map.get(ResString.ShopName), 
                (String)map.get(ResString.ShopDesp), 
                (String)map.get(ResString.ShopAddr));        
            return ResString.Success;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return ResString.Fail;
        }
    };
    
}
