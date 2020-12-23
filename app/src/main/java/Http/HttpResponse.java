package Http;

import com.alibaba.fastjson.JSONObject;
import com.example.taoqi.user.Result;

import java.util.Map;

import Variables.ResString;

public class HttpResponse {
    public int code;
    public byte[] data;

    public boolean MsgIsNotFail() {
        String msg = new String(data);
        try {
            JSONObject json = JSONObject.parseObject(msg);
            Map<String, Object> map = (Map<String, Object>)json;
            String Msg = (String)map.get(ResString.MSG);
            if(Msg == null) {
                return code == 200 && !msg.equals(ResString.Fail);
            }
            else {
                return code == 200 && !Msg.equals(ResString.Fail);
            }
        }
        catch (Exception e) {
            return code == 200 && !msg.equals(ResString.Fail);
        }

    }
}
