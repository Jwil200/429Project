import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

public class Utils {
	// List of available commands neatly organized with descriptions for usage in Chat.java
	public static final HashMap<String, String> commandList = new HashMap<String, String>(){{
        put("help", "Displays information about the available user interace options.");
        put("myip", "Displays the IP address of the process.");
        put("myport", "Displays the port on which this process is listening for incoming connections.");
        put("connection <destination> <port no>", "Establishes a new TCp connection to the specified destination and port number.");
        put("list", "Lists all current connections with id, ip address, and port number.");
        put("terminate <connection id.>", "Closes the connection to the specified connection. Corresponds to ids shown in list.");
        put("send <connection id.> <message>", "Sends a message to the given connection. Corresponds to ids shown in list.");
        put("exit", "Closes the process and terminates all connections.");
    }};


    public static boolean portAvailable (int port) {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(port);
			return true;
		}
		catch (IOException e) {}
		finally {
			if (ss != null) {
				try {
					ss.close();
				}
				catch (IOException e) {}
			}
		}
		return false;
	}


    public static boolean isInt (String s) {
        try {
            Integer.parseInt(s);
            return true;
        }
        catch (Exception e) { return false; }
    }
}
