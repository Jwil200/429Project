import java.io.PrintWriter;
import java.net.Socket;

public class Peer {

	private String address;
	private int port;
	private Socket socket;
	private PrintWriter output;
	private PeerHandler handler;

	public Peer(Chat chat, Socket socket, int port) {
		this.address = socket.getLocalAddress().getHostAddress();
		this.port = port;
		this.socket = socket;

		try {
			output = new PrintWriter(this.socket.getOutputStream(), true);
			handler = new PeerHandler(chat, this.socket, address, port);
		} 
		catch (Exception e) {
			System.err.println("Error: Address or Port for connection was invalid.");
		}
	}

	public String getAddress () {
		return address;
	}

	public void setPort (int port) {
		this.port = port;
	}

	public int getPort () {
		return port;
	}

	public Socket getSocket () {
		return socket;
	}

	public void send (String message) {
		output.println(message);
	}

	public void close () {
		handler.close();
		output.close();
		try {
			socket.close();
		}
		catch (Exception e) {e.printStackTrace();}
	}
}