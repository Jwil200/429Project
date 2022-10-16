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
		address = socket.getRemoteSocketAddress().toString().replace("/", "");
		address = address.substring(0, address.indexOf(":"));
		port = socket.getPort();
		this.socket = socket;

		try {
			output = new PrintWriter(this.socket.getOutputStream(), true);
			handler = new PeerHandler(chat, this);
		} 
		catch (Exception e) {
			System.err.println("Error: Address or Port for connection was invalid.");
		}
	}

	/**
	* Replaces a peer's address with the given address.
	* 
	* @param  address  a String of the ip address to switch to
	*/
	public void setAddress (String address) {
		this.address = address;
	}

	/**
	* Returns the ip address of this peer.
	* 
	* @return  a String containing the ip address of this peer
	*/
	public String getAddress () {
		return address;
	}

	/**
	* Replaces a peer's port with the given address.
	* 
	* @param  port  an int of the port to switch to
	*/
	public void setPort (int port) {
		this.port = port;
	}

	/**
	* Returns the port of this peer.
	* 
	* @return  an int containing the port of this peer
	*/
	public int getPort () {
		return port;
	}

	/**
	* Returns the Socket object of this peer.
	* 
	* @return  the Socket of this peer
	*/
	public Socket getSocket () {
		return socket;
	}

	/**
	* Sends a message through this peer connection. This
	* includes command messages such as connect and terminate.
	* 
	* @param  message  a String containing the message to be sent.
	*/
	public void send (String message) {
		output.println(message);
	}

	/**
	* Stops the handler for this peer, closes its output stream,
	* and closes its socket.
	*/
	public void close () {
		handler.close();
		output.close();
		try {
			socket.close();
		}
		catch (Exception e) { e.printStackTrace(); }
	}
}