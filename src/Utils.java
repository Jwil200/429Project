import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

public class Utils {

	/**
	* List of available commands neatly organized with descriptions for usage in Chat.java
	*/
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

	/**
	* Given a port checks if it is in use or not. Does so by
	* opening a Socket on the port, returns true on a successful
	* connection. With the finally block allows for disconnecting
	* the socket if it is open or not.
	* 
	* @param  port  an int containing the port number to check on
	* 
	* @return  a boolean, true if the socket is available, false if not
	*/
    public static boolean portAvailable (int port) {
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(port);
			return true;
		}
		catch (IOException e) { /* Nothing */ }
		finally {
			if (socket != null) {
				try {
					socket.close();
				}
				catch (IOException e) { /* Nothing */ }
			}
		}
		return false;
	}

	/**
	* Given a String determines if it is an Integer by
	* attemptint to convert it into one.
	* 
	* @param  port  an int containing the port number to check on
	* 
	* @return  a boolean, true if the socket is available, false if not
	*/
    public static boolean isInt (String s) {
        try {
            Integer.valueOf(s);
            return true;
        }
        catch (Exception e) { }
		return false;
    }
}
