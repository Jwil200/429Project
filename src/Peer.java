import java.io.PrintWriter;
import java.net.Socket;

public class Peer {

	private String address;
	private int port;
	private Socket socket;
	private PrintWriter output;
	private PeerHandler handler;

	/**
	* The Peer stores information regarding a peer connection,
	* such as its socket, address, and port. The address and port
	* will always point to the opposite connection's address and
	* open service port.
	*
	* @param  chat  a Chat object to reference to the chat service
	* @param  socket  a Socket this peer is connected through
	* @param  boolean  a boolean indicating if this Peer is established through the ServerHandler
	*/
	public Peer(Chat chat, Socket socket) {
		address = socket.getLocalAddress().toString().replace("/", "");
		port = socket.getLocalPort();
		this.socket = socket;

		try {
			output = new PrintWriter(this.socket.getOutputStream(), true);
			handler = new PeerHandler(chat, this, this.socket);
		} 
		catch (Exception e) {
			System.err.println("Error: Address or Port for connection was invalid.");
		}
	}

	public void setAddress (String address) {
		this.address = address;
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
		catch (Exception e) { e.printStackTrace(); }
	}
}