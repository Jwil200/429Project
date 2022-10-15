import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Chat {
	public static final int[] DEFAULT_PORTS = {5000, 4554, 4000}; // The default ports used by this program when none are provided.
	private static final int MAX_CONNECTIONS = 5;
	private static final int MAX_ATTEMPTS = 4;

	private List<Peer> connectedPeers;
	private Integer listenPort;
	private ServerSocket listenSocket;
	private ServerHandler handler;

	/**
	* Constructs an instance of the Chat client. 
	*/
	public Chat (int port) throws IOException {
		listenPort = port;
		listenSocket = new ServerSocket(port);
		connectedPeers = new ArrayList<Peer>();

		handler = new ServerHandler(this, listenSocket);
		
		// list of all clients (peers) connected to this host
	}

	public List<Peer> getConnectedPeers () {
		return connectedPeers;
	}

    public void help () {
		System.out.printf("%-40s%-20s\n", "Command", "Description");
		System.out.println(new String(new char[130]).replace("\0", "-")); // Creates line of dashes
		for (String command: Utils.commandList.keySet()) {
			System.out.printf("%-40s%-20s\n", command, Utils.commandList.get(command));
		}
	}

	private String ip() {
		try {
			return Inet4Address.getLocalHost().getHostAddress();
		}
		catch (Exception e) {
			return "127.0.0.0"; // On a failure to get post local host.
		}
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
	public void myip () {
		String ip = ip();
		if (ip.equals("127.0.0.0")) {
			System.err.println("Error: Host name cannot be resolved to an address.\nPlease check your internet connection and try again.");
			return;
		}
		System.out.println("The IP Address is: " + ip());
	}

	/**
	* Prints the port specified upon running the program.
	*/
	public void myport () {
		System.out.println("The program runs on port number: " + listenPort);
	}

	private Peer findPeer( String ip, int port) {
		for (Peer p : connectedPeers)
			if ( p.getAddress().equals(ip) && p.getPort() == port)
				return p;
		return null;
	}

	/**
	* Opens a TCP connection with a specified client.
	* If there are any issues pushes an error to the chat.
	* <p>
	* The connection is stored in an ArrayList for later use.
	*
	* @param  destination  a String containing the ip address of the other client
	* @param  destinationPort an int containing the port to connect to for the client
	*/
	public void connect(String destination, int destinationPort) {
		// check if connection limited is exceeded
		if (connectedPeers.size() >= MAX_CONNECTIONS) {
			System.out.println("Error: Connect fail, max connections reached.\nTerminate one or more connections to continue.");
			return;
		}

		// check for self/duplicate connections
		if ((destination.equals(ip()) && listenPort == destinationPort) || findPeer(destination, destinationPort) != null) {
			System.out.println("Error: Connect fail, cannot connect to same ip and port as running host.");
			return;
		}

		// all tests passed, connect to the peer
		
		Socket peerSocket = null;
		Peer peer;

		// try to connect but will stop after MAX_ATTEMPTS
		int attempts = 0;
		do {
			try {
				peerSocket = new Socket(destination, destinationPort);
			} catch (IOException e) {
				System.out.println("Error: Connection failed, trying again.");
			}
		} while (peerSocket == null && ++attempts < MAX_ATTEMPTS);
		if (attempts == MAX_ATTEMPTS) return;

		System.out.println("The connection to peer " + destination + ":" + destinationPort + " is successfully established.");

		peer = new Peer(this, peerSocket, destinationPort);
		connectedPeers.add(peer);
		peer.send("connect " + listenPort);
	}

	/**
	* Takes the current list of connections from
	* the ArrayList and prints them into a table
	* showing the id, ip, and port.
	*/
	public void list() {
		if (connectedPeers.isEmpty())
			System.out.println("No peers connected.");
		else {
			System.out.printf("%-6s%-20s%-10s%n", "id:", "IP Address", "Port No.");
			int i = 1;
			for (Peer p: connectedPeers) {
				System.out.printf("%-6s%-20s%-10d%n", i + ":", p.getAddress(), p.getPort());
				i++;
			}
		}
	}

	public int getPeer(String address, int port) {
		int i = 0;
		for (Peer p: connectedPeers) {
			if (p.getAddress().equals(address) && p.getPort() == port)
				return i;
			i++;
		}
		return -1;
	}

	public void terminate (int index) {
		if (index >= connectedPeers.size() || index < 0) {
			System.err.println("Error: Invalid id, use list to see the ids of all available connections.");
			return;
		}

		Peer peer = connectedPeers.get(index);

		connectedPeers.get(index).send("terminate");
		System.out.println("You dropped peer [ip: " + peer.getAddress() + " port: " + peer.getPort() + "]");

		connectedPeers.get(index).close();
		connectedPeers.remove(index);
	}

	public void send (int index, String message) {
		if (index >= connectedPeers.size() || index < 0) {
			System.err.println("Error: Invalid id, use list to see the ids of all available connections.");
			return;
		}

		if (message.length() >= 100) {
			System.err.println("Cannot send a message over 100 characters. Please send a shorter message.");
			return;
		}

		message = "send " + message; // Issue command as send.
		connectedPeers.get(index).send(message);
	}

	public void exit () {
		// Terminate all connections.
		while (connectedPeers.size() > 0)
			terminate(0);
		handler.close();
		System.out.println("Terminated all connections and closed server.");
	}

	public static void main (String[] args) {
		int server_port = 0;

		if (args.length == 0) {
			for (int port: DEFAULT_PORTS) {
				if (Utils.portAvailable(port)) {
					server_port = port;
					break;
				}
			}
		}
		else {
			server_port = Integer.valueOf(args[0]);
		}
		
		Chat chat = null;
		try {
			chat = new Chat(server_port);
		}
		catch (Exception e) {
			System.err.println("Error: Issue binding server port to port: " + server_port
								+ "\nPlease try again or with a different port.");
			return;
		}
		
		Scanner in = new Scanner(System.in);
		String input = "";
		
		while (!input.equals("exit")) {
			System.out.print(">>>");
			input = in.nextLine();
			String command = (input.indexOf(" ") > -1) ? input.split(" ")[0] : input;
			String[] cmdArgs;
			
			switch (command) {
				case "help":
					chat.help();
					break;
				case "myip":
					chat.myip();
					break;
				case "myport":
					chat.myport();
					break;
				case "connect":
					cmdArgs = input.split(" ", 3);
					if (cmdArgs.length < 3) {
						System.err.println("Error: Invalid number of arguments for connect.");
						break;
					}
					if (!Utils.isInt(cmdArgs[2])) {
						System.err.println("Error: Invalid port input. Please enter a number.");
						break;
					}
					chat.connect(cmdArgs[1], Integer.valueOf(cmdArgs[2]));
					break;
				case "list":
					chat.list();
					break;
				case "terminate":
					cmdArgs = input.split(" ", 2);
					if (cmdArgs.length != 2) {
						System.err.println("Error: Invalid number of inputs. Use help for more information on using terminate.");
						break;
					}
					if (!Utils.isInt(cmdArgs[1])) {
						System.err.println("Error: Invalid input, the id must be a number.");
						break;
					}
					else {
						chat.terminate(Integer.valueOf(cmdArgs[1]) - 1);
					}
					break;
				case "send":
					cmdArgs = input.split(" ", 3);
					if (cmdArgs.length != 3) {
						System.err.println("Error: Invalid number of inputs. Use help for more information on using send.");
						break;
					}
					if (!Utils.isInt(cmdArgs[1])) {
						System.err.println("Error: Invalid input, the id must be a number.");
						break;
					}
					chat.send(Integer.valueOf(cmdArgs[1]) - 1, cmdArgs[2]);
					break;
				case "exit":
					chat.exit();
					in.close();
					break;
				default:
					System.err.println("Error: Invalid command. For a list of commands use 'help'.");
			}
		}

		in.close();
	}

}