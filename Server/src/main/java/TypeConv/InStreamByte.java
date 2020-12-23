package TypeConv;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;


public abstract class InStreamByte {
    public static byte[] InStream2Byte(InputStream is) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len = -1;
        while((len = is.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }
        baos.close();
        is.close();
        return baos.toByteArray();
    }
}
