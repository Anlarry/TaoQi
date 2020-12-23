package Server.Action;

import java.security.cert.TrustAnchor;
import java.util.Map;

import Server.Server;
import Server.Manager.GoodManagerServer;
import Server.Manager.ShopManagerSever;
import Variables.ResString;
import Server.Manager.ManagerServer;
/**
 * GoodActions
 */
public class GoodActions {
    private GoodManagerServer goodManagerServer;

    public GoodActions(GoodManagerServer goodManagerServer) {
        this.goodManagerServer = goodManagerServer;
    }
    public Action AddGoodAction = new Action() {
        @Override
        public String action(Map<String, Object> map) {
            try {
                goodManagerServer.AddGood(
                    (String)map.get(ResString.UserId), 
                    (String)map.get(ResString.GoodName), 
                    (String)map.get(ResString.GoodKind), 
                    (String)map.get(ResString.GoodDesp), 
                    (String)map.get(ResString.GoodPhoto), 
                    (String)map.get(ResString.GoodPrice));
                return ResString.Success;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return ResString.Fail;
            }
        }
    };
    public Action GetAllGoodAction = new Action() {
        @Override
        public String action(Map<String, Object> map) {
            try {
                return goodManagerServer.GetAllGood();
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                return ResString.Fail;
            }
        }
    };
    public Action GetGoodByNameAction = new Action() {
        @Override
        public String action(Map<String, Object> map) {
            try {   
                return goodManagerServer.GetGoodByName((String)map.get(ResString.GoodName));
            }
            catch (Exception e ) {
                System.err.println(e.getMessage());
                return ResString.Fail;
            }
        }
    };
    public Action BuyGood = new Action () {
        @Override
        public String action(Map<String, Object> map) {
            try {
                return goodManagerServer.BuyGood((String)map.get(ResString.UserId), (String)map.get(ResString.GoodId));
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
                return ResString.Fail;
            }
        }
    };

    public Action GetPurchased = new Action () {
        @Override
        public String action(Map<String, Object> map) {
            try {
                return goodManagerServer.GetPurchasedGood(
                    (String)map.get(ResString.UserId)
                );
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
                return ResString.Fail;
            }
        }
    };

    public Action GetGoodByBusiness = new Action () {
        @Override
        public String action(Map<String, Object> map) {
            try {
                String userId = (String)map.get(ResString.UserId);
                String shopId =  Server.shopManagerSever.GetOneShopByField(ManagerServer.b_id, userId)
                    .get(ResString.ShopId);
                return goodManagerServer.GetGoodByField(ManagerServer.s_id, shopId);
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
                return ResString.Fail;
            }
        }
    };
}