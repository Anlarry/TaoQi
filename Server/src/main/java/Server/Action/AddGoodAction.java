package Server.Action;

import java.util.Map;

import javax.swing.text.StyledEditorKit;

import Server.Manager.GoodManagerServer;
import Variables.ResString;

@Deprecated
public class AddGoodAction implements Action {
    private GoodManagerServer goodManagerServer;

    public AddGoodAction(GoodManagerServer goodManagerServer) {
        this.goodManagerServer = goodManagerServer;
    }

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
}
