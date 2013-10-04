package nl.frankkie.netremoteserver;

import java.util.Scanner;

/**
 *
 * @author FrankkieNL
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SimpleWebServer.main(args);
        
        Scanner s = new Scanner(System.in);
        s.nextLine();
    }
}
