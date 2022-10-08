package src;

import java.net.*;
import java.io.*;

public class Connection {
	private Socket clientSocket;
	private BufferedReader in;
	private PrintWriter out;
	
	private String address;
	private int port;
	
	public Connection (String address, int port) throws Exception {
		this.address = address;
		this.port = port;
		
		connect();
	}
	
	public void connect () throws Exception {
		clientSocket = new Socket(address, port);
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		out = new PrintWriter(clientSocket.getOutputStream());
		
		Thread receiver = new Thread(new Runnable() {
			String msg;
			@Override
			public void run () {
				try {
					msg = in.readLine();
					while (msg != null) {
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
		});
	}
	
	public String getIP () {
		return address;
	}
	
	public int getPort () {
		return port;
	}
}
