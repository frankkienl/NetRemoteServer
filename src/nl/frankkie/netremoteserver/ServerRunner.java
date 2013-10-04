package nl.frankkie.netremoteserver;

import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;

public class ServerRunner {
 
    public static NanoHTTPD server;
    
    public static void executeInstance(NanoHTTPD server) {
        try {
            server.start();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
            System.exit(-1);
        }

        System.out.println("Server started");
    }
    
    public static void stopServer(){
        if (server != null){
            server.stop();
            System.out.println("Server stopped.\n");
        }
        System.exit(0);
    }
}
