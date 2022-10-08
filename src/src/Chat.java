package src;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class Chat {
	
	abstract class ServerRunnable implements Runnable {
		ServerSocket serverSocket;
		public ServerRunnable(ServerSocket serverSocket) {
			this.serverSocket = serverSocket;
		}
	}
	
	private static int PORT = 0;
	private static final String CHECK_IP_URL = "http://checkip.amazonaws.com/";
	
	private ArrayList<Connection> connections;
	
	// Server
	private ServerSocket serverSocket;
	
	
	/**
	* Constructs an instance of the Chat client. 
	*/
	public Chat () {
		connections = new ArrayList<Connection>();
		try {
			serverSocket = new ServerSocket(PORT);
			
			Thread t = new Thread(new ServerRunnable (serverSocket) {
				private Socket serverClientSocket;
				@Override
				public void run () {
					try {
						serverClientSocket = serverSocket.accept();
					} catch (IOException e) { }
				}
			});
			t.start();
		}
		catch (Exception e) {}
	}
	
	/**
	* Gets the public address of the current Chat instance.
	* This contacts the address specified in CHECK_IP_URL
	* to get the public address.
	* <p>
	* Will print this to the chat or "Cannot retrieve IP." 
	* if unable to.
	*
	* @return 		String containing the public address of the client.
	*/
	public String myip () {		
		String ip = "";
		try {
			ip = new BufferedReader(new InputStreamReader(new URL(CHECK_IP_URL).openStream())).readLine().trim();
		}
		catch (Exception e) {
			ip = "Cannot retrieve IP.";
		}
		System.out.println("Public IP Address: " + ip);
		return ip;
	}
	
	/**
	* Prints the port specified upon running the program.
	*/
	public void myport () {
		System.out.println("Running on Port: " + PORT);
	}
	
	/**
	* Opens a TCP connection with a specified client.
	* If there are any issues pushes an error to the chat.
	* <p>
	* The connection is stored in an ArrayList for later use.
	*
	* @param  address  a String containing the ip address of the other client
	* @param  port an int containing the port to connect to for the client
	*/
	public void connect (String address, int port) {
		// Client
		try {
			connections.add(new Connection(address, port));
		}
		catch (Exception e) {
			System.err.println("Issue establishing connection.");
			return;
		}
		System.out.println("Established connection to " + address + ":" + port);
	}
	
	/**
	* Takes the current list of connections from
	* the ArrayList and prints them into a neat table
	* showing the num, ip, and port.
	*/
	public void list () {
		System.out.printf("%-4s%-20s%-10s%n", "id", "ip", "port");
		int i = 1;
		for (Connection c: connections) {
			System.out.printf("%-4d%-20s%-10d%n", i, c.getIP(), c.getPort());
			i++;
		}
	}
	
	public static void main (String args[]) {
		PORT = new Integer(args[0]); // Always need to setup this var based on args
		
		Chat c = new Chat();
		
		String s = c.myip();
		c.myport();
		c.connect("127.0.0.1", PORT);
		c.list();
	}
}
