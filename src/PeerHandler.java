import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class PeerHandler implements Runnable {
		
    private Socket peerSocket;
    private BufferedReader input;
    private Chat chat;
    private Peer peer;

    private boolean run;
    
    /**
	* Creates a PeerHandler for the given Peer, using a reference
    * to Chat to allow it to terminate the Peer when the command
    * is received.
    * <p>
    * Runs itself through a thread, can be stopped using the run
    * variable, through close().
	*
	* @param  chat  a Chat object to reference to the chat service
	* @param  peer  a Peer object of the peer that this is handling
	*/
    public PeerHandler (Chat chat, Peer peer) throws IOException {
        this.chat = chat;
        this.peer = peer;
        this.peerSocket = peer.getSocket();
        input = new BufferedReader(new InputStreamReader(this.peerSocket.getInputStream()));

        run = true;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (run) {
            try {
                String in = input.readLine();
                if (in == null) continue;

                String[] args = in.split(" ", 2);
                switch (args[0]) {
                    case "connect":
                        args = in.split(" ", 3);
                        if (args.length != 3) break;
                        if (!Utils.isInt(args[2])) break;
                        peer.setAddress(args[1]);
                        peer.setPort(Integer.valueOf(args[2]));
                        System.out.println("\nThe connection to peer " + peer.getAddress() + ":" + peer.getPort() + " is successfully established.");
                        System.out.print(">>>");
                        break;
                    case "send":
                        if (args.length != 2) break;
                        System.out.println("\nMessage recieved from IP: " + peer.getAddress() + ":");
                        System.out.println("Message: " + args[1]);
                        System.out.print(">>>");
                        break;
                    case "terminate":
                        chat.getConnectedPeers().remove(chat.getPeer(peer.getAddress(), peer.getPort()));
                        close();
                        System.out.println("\nPeer " + peer.getAddress() + " terminates the connection");
                        System.out.print(">>>");
                        break;
                    default: // Ignore unknown command.
                }
            }
            catch (Exception e) {
                System.err.println("Error: Message dropped. Status: " + peerSocket.isConnected());
            }
        }
    }

    /**
	* Stops the currently running thread by changing the run
    * variable to false. Also closes the input stream from
    * the socket.
	*/
    public void close () {
        run = false;
        try {
            input.close();
        }
        catch (Exception e) { e.printStackTrace(); }
    }
}