
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Chat {
	

	List<Peer> connectedPeers;
	Integer listenPort;
	String myIP;
	ServerSocket listenSocket;
	private final int MAX_CONNECTIONS = 3;
	BufferedReader input;
	private Map<Peer, DataOutputStream> peerOutputMap;
	
	
	Integer id;

	public Chat() throws IOException {
	    
		
	   myIP = Inet4Address.getLocalHost().getHostAddress();
	  
		// list of all clients (peers) connected to this host
		connectedPeers = new ArrayList<Peer>();

		input = new BufferedReader(new InputStreamReader(System.in));

		// map a peer to an output stream
		peerOutputMap = new HashMap<Peer, DataOutputStream>();
	}

    public void Help() {
		for (int i = 0; i < 40; i++)
			System.out.print("-"); 
		System.out.println("\nchat <port number>\t Run program");
		System.out.println("\nhelp\t Display commands");
		System.out.println("\nmyip\t Display IP address");
		System.out.println("\nmyport\t Display the port on which this process is listening for incoming connections");
		System.out.println("\nconnect\t <destination> <port no> This command connect to another peer");
		System.out.println("\nlist\t Display a list of all the peers you are connected to");
		System.out.println("\nterminate <connection id>\t Terminate the connection ");
		System.out.println("\nsend <connection id.> <message>\t Send a message to a peer");
		System.out.println("\nexit\t Close all connections and terminate this process.");
		for (int i = 0; i < 40; i++)
			System.out.print("-"); 
		System.out.println("\n");
	}

	public void Start(String choice) throws IOException {
		int port = Integer.valueOf(choice.split(" ")[1]);
		
		try {
			listenSocket = new ServerSocket(port);
		} catch (Exception e){
			System.out.println("Error reading port number");
		}

		if (listenSocket != null) {
			listenPort = listenSocket.getLocalPort();
			myIP = Inet4Address.getLocalHost().getHostAddress();
			startServer();
		}
	}

	private void startServer() throws IOException {

		//  each peer on a separate thread
		new Thread(() -> {
			while (true) {
				try {
					// wait for a peer to connect
					Socket connectionSocket = listenSocket.accept();

					// once there is a connection, serve them on thread
					new Thread(new PeerHandler(connectionSocket)).start();

				} catch (IOException e) {
					
				}
			}
		}).start();
	}

	// open an IO stream for each peer connected to the host
	
	private class PeerHandler implements Runnable {
		
		private Socket peerSocket;
		Integer porT;
		String iP;
		public PeerHandler(Socket socket) {
			this.peerSocket = socket;
		}

		public void run() {
			
			try {
				BufferedReader input = new BufferedReader(new InputStreamReader(peerSocket.getInputStream()));
				//BufferedReader input1 = new BufferedReader(new InputStreamReader(peerSocket.getInputStream()));
				
				
				
				while (true) {
					String Str = input.readLine();
					

					
					if (Str == null) {
						return;
					}
					

					String type =  Str.split(" ")[0];
					
					if(type.equals("connect")){
					iP = Str.split(" ")[1];
					 porT = Integer.valueOf(Str.split(" ")[2]);
					} 

					
					
					
					
					

						
					switch(type){
						case "connect":
						
							PrintConnectSuccess(Str);
							break;
				
						case"send":

							PrintMessage(iP,porT,Str);
							break;
							


						case"terminate":
						 TerminateMessage(iP,porT); 
							
						

						//terminate conection
						terminateConnection(findPeer(iP, porT));
						
						//remove from list
						removePeer(findPeer(iP, porT));
						input.close();	
							return;
					
					
					

					}


					
				}
			} catch (IOException e) {
				System.out.println("Message: Connection drop");
			}
		}
	}

	private void terminateConnection(Peer peer) {
		try {
			peer.getSocket().close();
			peerOutputMap.get(peer).close();
		} catch (IOException e) {
			
		}
	}

	private void removePeer(Peer peer) {
		connectedPeers.remove(peer);
		peerOutputMap.remove(peer);
	}

	private Peer findPeer( String ip, int port) {
		for (Peer p : connectedPeers)
			if ( p.getHost().equals(ip) && p.getPort() == port)
				return p;
		return null;
	}

	public void List() {
		if (connectedPeers.isEmpty())
			System.out.println("No peers connected.");
		else {
			System.out.println("id:   IP Address     Port No.");
			for (int i = 0; i < connectedPeers.size(); i++) {
				Peer peer = connectedPeers.get(i);
				System.out.println(peer.getId() +"    " + peer.getHost() + "     " + peer.getPort());
			}
			System.out.println("Total Peers: " + connectedPeers.size());
		}
	}

	public void Connect(String userInput) throws IOException {
		String[] args = userInput.split(" ");
		String ip;
		int port;


		ip = args[1];
		port = Integer.valueOf(args[2]);

		// check if connection limited is exceeded
		if (connectedPeers.size() >= MAX_CONNECTIONS) {
			System.out.println("connect fail: max connection");
			return;
		}

		// check for self/duplicate connections
		if (!DuplicateConnection(ip, port)) {
			System.out.println("connect fail: no self or duplicate connection");
			return;
		}

		// all tests passed, connect to the peer
		
		Socket peerSocket = null;
		Peer peer;

		// try to connect but will stop after MAX_ATTEMPTS
		do {
			try {
				peerSocket = new Socket(ip, port);
			} catch (IOException e) {

				System.out.println("connection failed");
				
			}
		} while (peerSocket == null );

		
			System.out.println("The connection to peer " + ip + " is successfully established");
			if (connectedPeers.isEmpty()){
				id=1;
				 peer = new Peer(id,ip, port);
				connectedPeers.add(peer);
			}

			else{
				id = connectedPeers.size()+1;
				 peer = new Peer(id,ip, port);
				connectedPeers.add(peer);
			}
			// map this peer to an output stream
			peerOutputMap.put(peer, new DataOutputStream(peerSocket.getOutputStream()));

			// tell the peer your host address and port number
			// tell the peer to connect to you
			sendMessage(peer,"connect " + myIP +" "+ listenPort);
			

		}

		public boolean DuplicateConnection(String ip, int port) {
			return !SelfConnection(ip, port) && UniquePeer(ip, port);
		}
	
		private boolean SelfConnection(String ip, int port) {
			return ip.equals(myIP) && listenPort == port;
		}

		private boolean UniquePeer(String ip, int port) {
			return findPeer(ip, port) == null;
		}

	private void sendMessage(Peer peer, String Str) {
		try {
			// "\r\n" so when readLine() is called,
			// it knows when to stop reading
			peerOutputMap.get(peer).writeBytes(Str + "\r\n");

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void PrintConnectSuccess(String Str) throws IOException{
		//int id = Integer.valueOf(Str.split(" ")[1]);
		String ip = Str.split(" ")[1];
		int port = Integer.valueOf(Str.split(" ")[2]);
		System.out.println("\nThe connection to peer " + ip + " is successfully established");
		System.out.print(">>> ");
		// save peer's info, used for a lot of other stuff
		Peer peer;
		if (connectedPeers.isEmpty()){
			id=1;
			 peer = new Peer(id,ip, port);
			connectedPeers.add(peer);
		}

		else{
			id = connectedPeers.size()+1;
			 peer = new Peer(id,ip, port);
			connectedPeers.add(peer);
		}
		
		peerOutputMap.put(peer, new DataOutputStream(peer.getSocket().getOutputStream()));


	}

	public void Send(String userInput) {
		String[] args = userInput.split(" ");
		if (args.length >= 3) {
			try {
				int id = Integer.valueOf(args[1])-1;
				
				String Str = "";
					for (int i = 0; i < args.length; i++){
						Str += args[i] + " ";
					}
					sendMessage(connectedPeers.get(id), Str);
				
			} catch (NumberFormatException e) {
				System.out.println("Error: Second argument should be a integer.");
			}
		} else {
			System.out.println("Error: Invalid format for 'send' command. See 'help' for details.");
		}
	}

	private void PrintMessage(String ip, Integer port,String Str) {
		String msg="";
		String[] args = Str.split(" ");
		//int id = Integer.valueOf(args[1]);
		for (int i = 2; i < args.length; i++){
			msg += args[i] + " ";
			}
			
		System.out.println("\nMessage received from IP: " + ip +" :");
		System.out.println("Message: " + msg);

		
		System.out.print(">>> ");
	}

	public void Terminate(String userInput) {
		String[] args = userInput.split(" ");
		if (args.length == 2) {
			try {
				int id = Integer.valueOf(args[1]) - 1;
				if (id >= 0 && id <connectedPeers.size()) {
					// notify peer that connection will be drop
					Peer peer = connectedPeers.get(id);
					sendMessage(peer, "terminate "+ id+" "  + myIP +" "+ listenPort);
					System.out.println("You dropped peer [ip: " + peer.getHost() + " port: " + peer.getPort() + "]");
					try {
						peer.getSocket().close();
					} catch (IOException e1) {
						
						e1.printStackTrace();
					}
					try {
						peerOutputMap.get(peer).close();
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					//remove Peer
					connectedPeers.remove(peer);
					peerOutputMap.remove(peer);
				} else {
					System.out.println("Error: Please select a valid peer id from the list command.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Error: Second argument should be a integer.");
			}
		} else {
			System.out.println("Error: Invalid format for 'terminate' command. See 'help' for details.");
		}

}

private void TerminateMessage(String ip,Integer port) {
	System.out.println();
	System.out.println("Peer [ " + ip +  " ] terminates the connection");
	System.out.print(">>> ");
}

}
    

