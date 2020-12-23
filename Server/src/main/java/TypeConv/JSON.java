package TypeConv;

import com.alibaba.fastjson.*;

import java.util.List;
import java.util.Map;

public abstract class JSON {
    public static String fromMap(Map obj) {
        JSONObject json = new JSONObject(obj);
        return json.toString();
    }
}
