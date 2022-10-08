package src;

import java.net.*;
import java.io.*;

public class Connection {
	
	abstract class ClientRunnable implements Runnable {
		public ClientRunnable() {
			@SuppressWarnings("unused")
			Thread t = new Thread(this);
		}
		public abstract void close ();
	}
	
	private Socket clientSocket;
	private BufferedReader in;
	private PrintWriter out;
	
	private ClientRunnable clientThread;
	
	private String address;
	private int port;
	
	public Connection (String address, int port) throws Exception {
		this.address = address;
		this.port = port;
		
		connect();
	}
	
	public Connection (Socket clientSocket) throws Exception {
		this.clientSocket = clientSocket;
		address = clientSocket.getLocalAddress().getHostAddress();
		port = clientSocket.getLocalPort();
		
		setupStreams();
	}
	
	public void connect () throws Exception {
		clientSocket = new Socket(address, port);
		setupStreams();
	}
	
	private void setupStreams () throws Exception {
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		out = new PrintWriter(clientSocket.getOutputStream());
		
		clientThread = new ClientRunnable () {
			private boolean run = true;
			String msg;
			
			@Override
			public void run () {
				try {
					msg = in.readLine();
					while (msg != null && run) {
						System.out.println("Server : " + msg);
						msg = in.readLine();
					}
					System.out.println("Server out of service.");
					out.close();
					clientSocket.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			public void close () {
				run = false;
			}
		};
	}
	
	public String getIP () {
		return address;
	}
	
	public int getPort () {
		return port;
	}
	
	public void exit () {
		clientThread.close();
	}
}
