package nl.frankkie.netremoteserver;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 *
 * @author FrankkieNL
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SimpleWebServer.mane(args);

        if (SystemTray.isSupported()) {
            SystemTray systemTray = SystemTray.getSystemTray();
            //create image
            BufferedImage bi = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = (Graphics2D) bi.getGraphics();
            graphics.setColor(Color.RED);
            graphics.fillRect(0, 0, 16, 16);
            graphics.setColor(Color.WHITE);
            graphics.drawString("N", 4, 12);
            //create popup-menu
            PopupMenu popupMenu = new PopupMenu("NetRemote Server");
            MenuItem pair = new MenuItem("Pair");
            pair.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        openWebpage(new URL("http://127.0.0.1:12345/pair"));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            popupMenu.add(pair);
            MenuItem exit = new MenuItem("Exit");
            exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ServerRunner.stopServer();
                }
            });
            popupMenu.add(exit);
            TrayIcon trayIcon = new TrayIcon(bi, "NetRemote Server", popupMenu);
            try {
                systemTray.add(trayIcon);
            } catch (AWTException ex) {
                ex.printStackTrace();
            }
        }

        //To keep the program running
        //Scanner s = new Scanner(System.in);
        //s.nextLine();
    }

    public static void openWebpage(URI uri) {
        //http://stackoverflow.com/questions/10967451/open-a-link-in-browser-with-java-button
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void openWebpage(URL url) {
        //http://stackoverflow.com/questions/10967451/open-a-link-in-browser-with-java-button
        try {
            openWebpage(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
