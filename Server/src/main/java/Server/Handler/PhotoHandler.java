package Server.Handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.*;

public class PhotoHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("\033[1;36m [ Photo Request ] \033[0m" + exchange.getRequestURI() );
        exchange.sendResponseHeaders(200, 0);
        OutputStream os = exchange.getResponseBody();
        File photo = new File( "." + exchange.getRequestURI());
        FileInputStream fis = new FileInputStream(photo);
        os.write(fis.readAllBytes());
        os.write("test".getBytes());
        os.close();
    }
}
