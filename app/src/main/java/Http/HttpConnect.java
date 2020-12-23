package Http;

import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import Variables.HttpType;
import Variables.ResString;

public class HttpConnect {


    private HttpURLConnection conn;
    public HttpConnect(URL url , String method, boolean doOutput) throws Exception {
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setDoOutput(doOutput);
    }
    public HttpConnect(String url) throws  Exception {
        URL _url = new URL(url);
        conn = (HttpURLConnection) _url.openConnection();
    }
    public void Send(byte[] bytes ) throws Exception{
        OutputStream out = conn.getOutputStream();
        out.write(bytes);
        out.close();
    }
    public HttpResponse Response() throws Exception{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        HttpResponse response = new HttpResponse();
        response.code =  conn.getResponseCode();
        InputStream is = conn.getInputStream();
        int len = -1;
        while((len = is.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }
        baos.close();
        is.close();
        response.data = baos.toByteArray();
        return response;
    }
    public void setJSON() {
        conn.setRequestProperty("Content-type", "application/json");
    }
    public void setByte() {
        conn.setRequestProperty("Content-type", "bytes");
    }
    public void setGetTypeAndSend(String Type) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(HttpType.Type, Type);
        JSONObject json = new JSONObject(map);
        Send(json.toString().getBytes());
    }


}
