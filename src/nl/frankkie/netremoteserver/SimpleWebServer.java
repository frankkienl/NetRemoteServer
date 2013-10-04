package nl.frankkie.netremoteserver;

import fi.iki.elonen.NanoHTTPD;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

public class SimpleWebServer extends NanoHTTPD {

    private final boolean quiet;

    public SimpleWebServer(String host, int port, boolean quiet) {
        super(port); //no host
        this.quiet = quiet;
    }

    /**
     * URL-encodes everything between "/"-characters. Encodes spaces as '%20'
     * instead of '+'.
     */
    private String encodeUri(String uri) {
        String newUri = "";
        StringTokenizer st = new StringTokenizer(uri, "/ ", true);
        while (st.hasMoreTokens()) {
            String tok = st.nextToken();
            if (tok.equals("/")) {
                newUri += "/";
            } else if (tok.equals(" ")) {
                newUri += "%20";
            } else {
                try {
                    newUri += URLEncoder.encode(tok, "UTF-8");
                } catch (UnsupportedEncodingException ignored) {
                }
            }
        }
        return newUri;
    }

    public void keyDownAndUp(int key) {
        try {
            Robot r = new Robot();
            r.keyPress(key);
            r.delay(25);
            r.keyRelease(key);
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
    }

    public void keysDownAndUp(int key1, int key2) {
        try {
            Robot r = new Robot();
            r.keyPress(key1);
            r.keyPress(key2);
            r.delay(25);
            r.keyRelease(key1);
            r.keyRelease(key2);
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Here is where the magic happens
     *
     * @param uri
     * @param header
     * @param parms get/post parameters
     * @return
     */
    Response serveFile(String uri, Map<String, String> header, Map<String, String> parms) {
        String text = "??";
        if (uri.equalsIgnoreCase("/cmd")) {
            if (parms.containsKey("cmd")) {
                text = "Command: " + parms.get("cmd");
                switch (parms.get("cmd")) {

                    case "play": {
                        keyDownAndUp(KeyEvent.VK_PAGE_UP);
                        break;
                    }
                    case "pause": {
                        keyDownAndUp(KeyEvent.VK_PAGE_DOWN);
                        break;
                    }

                    case "vol_up": {
                        keyDownAndUp(KeyEvent.VK_UP);
                        break;
                    }
                    case "vol_down": {
                        keyDownAndUp(KeyEvent.VK_DOWN);
                        break;
                    }

                    case "mute": {
                        keyDownAndUp(KeyEvent.VK_M);
                        break;
                    }

                    case "fullscreen": {
                        keyDownAndUp(KeyEvent.VK_F);
                        break;
                    }

                    case "exit_fullscreen": {
                        keyDownAndUp(KeyEvent.VK_ESCAPE);
                        break;
                    }

                    case "rewind": {
                        keysDownAndUp(KeyEvent.VK_SHIFT, KeyEvent.VK_LEFT);
                        break;
                    }

                    case "fast_forward": {
                        keysDownAndUp(KeyEvent.VK_SHIFT, KeyEvent.VK_RIGHT);
                        break;
                    }

                    default: {
                        text = "Command not found";
                        break;
                    }
                }
            } else {
                text = "no command";
            }
        } else if (uri.equalsIgnoreCase("/pair")) {
            //Show Pair Page
            String serverUri = "http%3A%2F%2F" + getIPAddress(true) + "%3A" + port + "%2F";
            String imageUri = "http://chart.apis.google.com/chart?cht=qr&chs=256x256&chld=L&choe=UTF-8&chl=" + serverUri;
            text = "<html><head><title>NetRemote Server</title><meta name=\"viewport\" content=\"width=device-width, user-scalable=false;\"></head><body>\n"
                    + "<h2>NetRemote Server</h2>\n"
                    + "<a href=\"/\">Back to remote control</a><br /><br />\n"
                    + "Please scan the QR Code to pair the Smartphone to this server.<br />\n"
                    + "Note: The Smartphone and the server must be on the same (wifi) network!!<br />\n"
                    + "<img src=\"" + imageUri + "\" alt=\"QR Code\"/><br />\n"
                    + "Or enter this information:<br />\n"
                    + "IP: " + getIPAddress(true) + "<br />\n"
                    + "PORT: " + port + "<br />\n"
                    + "<br /><br />\nMore Information: <a href=\"http://frankkie.nl/android/\">http://frankkie.nl/android/</a>\n"
                    + "</body></html>";
        } else {
            //Show Main Page
            text = "<html><head><title>NetRemote Server</title>\n"
                    + "<meta name=\"viewport\" content=\"width=device-width, user-scalable=false;\">\n"
                    + "<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\">\n"
                    + "</script>\n"
                    + "<script>\n"
                    + "$(document).ready(function(){\n"
                    + "  $(\"#btnPlay\").click(function(){\n"
                    + "    $(\"#result\").load(\"/cmd?cmd=play\");\n"
                    + "  });\n"
                    + "});\n"
                    + "$(document).ready(function(){\n"
                    + "  $(\"#btnPause\").click(function(){\n"
                    + "    $(\"#result\").load(\"/cmd?cmd=pause\");\n"
                    + "  });\n"
                    + "});\n"
                    + "$(document).ready(function(){\n"
                    + "  $(\"#btnFullscreen\").click(function(){\n"
                    + "    $(\"#result\").load(\"/cmd?cmd=fullscreen\");\n"
                    + "  });\n"
                    + "});\n"
                    + "$(document).ready(function(){\n"
                    + "  $(\"#btnExitFullscreen\").click(function(){\n"
                    + "    $(\"#result\").load(\"/cmd?cmd=exit_fullscreen\");\n"
                    + "  });\n"
                    + "});\n"
                    + "$(document).ready(function(){\n"
                    + "  $(\"#btnRewind\").click(function(){\n"
                    + "    $(\"#result\").load(\"/cmd?cmd=rewind\");\n"
                    + "  });\n"
                    + "});\n"
                    + "$(document).ready(function(){\n"
                    + "  $(\"#btnFastForward\").click(function(){\n"
                    + "    $(\"#result\").load(\"/cmd?cmd=fast_forward\");\n"
                    + "  });\n"
                    + "});\n"
                    + "$(document).ready(function(){\n"
                    + "  $(\"#btnVolUp\").click(function(){\n"
                    + "    $(\"#result\").load(\"/cmd?cmd=vol_up\");\n"
                    + "  });\n"
                    + "});\n"
                    + "$(document).ready(function(){\n"
                    + "  $(\"#btnVolDown\").click(function(){\n"
                    + "    $(\"#result\").load(\"/cmd?cmd=vol_down\");\n"
                    + "  });\n"
                    + "});\n"
                    + "$(document).ready(function(){\n"
                    + "  $(\"#btnMute\").click(function(){\n"
                    + "    $(\"#result\").load(\"/cmd?cmd=mute\");\n"
                    + "  });\n"
                    + "});\n"
                    + "</script></head><body>\n"
                    + "<h2>NetRemote Server</h2>\n"
                    + "<a href=\"/pair\">Pair Smartphone</a><br />\n"
                    + "\n"
                    + "<button id=\"btnPlay\">Play</button> <button id=\"btnPause\">Pause</button> <br />\n"
                    + "<button id=\"btnMute\">Mute</button> <br />\n"
                    + "<button id=\"btnVolUp\">Vol Up</button> <button id=\"btnVolDown\">Vol Down</button> <br />\n"
                    + "<button id=\"btnRewind\">Rewind</button> <button id=\"btnFastForward\">Fast Forward</button> <br />\n"
                    + "<button id=\"btnFullscreen\">Fullscreen</button> <button id=\"btnExitFullscreen\">Exit Fullscreen</button> <br />\n"
                    + "\n"
                    + "\n"
                    + "<div id=\"result\" style=\"display:none\"></div></body></html>";
        }
        return new Response(Response.Status.OK, "text/html", text);
        /*
         http://markwarren.wordpress.com/2010/03/24/netflix-movie-player-keyboard-shortcuts/
         Space – Toggle Play/Pause
         Enter – Toggle Play/Pause
         PgUp – Play
         PgDn – Pause
         F – Full-screen
         Esc – Exit full-screen
         Shift+Left arrow – Rewind
         Shift+Right arrow – Fast Forward
         Up arrow - Volume Up
         Down arrow - Volume Down
         M – Mute toggle
         */
    }

    public String getIPAddress(boolean yes) {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR:" + e;
        }
    }

    @Override
    public Response serve(String uri, Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files) {
        if (!quiet) {
            System.out.println(method + " '" + uri + "' ");

            Iterator<String> e = header.keySet().iterator();
            while (e.hasNext()) {
                String value = e.next();
                System.out.println("  HDR: '" + value + "' = '" + header.get(value) + "'");
            }
            e = parms.keySet().iterator();
            while (e.hasNext()) {
                String value = e.next();
                System.out.println("  PRM: '" + value + "' = '" + parms.get(value) + "'");
            }
            e = files.keySet().iterator();
            while (e.hasNext()) {
                String value = e.next();
                System.out.println("  UPLOADED: '" + value + "' = '" + files.get(value) + "'");
            }
        }
        return serveFile(uri, header, parms);
    }

    /**
     * Starts as a standalone file server and waits for Enter.
     */
    public static void main(String[] args) {
        // Defaults
        port = 12345;
        String host = "127.0.0.1";
        boolean quiet = true;

        // Parse command-line, with short and long versions of the options.
        for (int i = 0; i < args.length; ++i) {
            if (args[i].equalsIgnoreCase("-h") || args[i].equalsIgnoreCase("--host")) {
                host = args[i + 1];
            } else if (args[i].equalsIgnoreCase("-p") || args[i].equalsIgnoreCase("--port")) {
                port = Integer.parseInt(args[i + 1]);
            } else if (args[i].equalsIgnoreCase("-v") || args[i].equalsIgnoreCase("--verbose")) {
                quiet = false;
            } else if (args[i].equalsIgnoreCase("-?") || args[i].equalsIgnoreCase("--help")) {
                System.out.println(helpText);
                break;
            }
        }

        ServerRunner.executeInstance(new SimpleWebServer(host, port, quiet));
    }
    public static int port = 12345;
    public static String helpText =
            "NetRemote Server\n"
            + "-h --host [host-ip]\t\tChange the host-ip. Default: 127.0.0.1\n"
            + "-p --port [port]\t\tChange the port. Default: 12345\n"
            + "-v --verbose \t\tEnable logging\n"
            + "-? --help\t\tDisplay this helptext\n"
            + "\nFor more help: http://frankkie.nl/android/";
}
