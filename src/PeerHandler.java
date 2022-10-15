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
    
    public PeerHandler (Chat chat, Peer peer, Socket peerSocket) throws IOException {
        this.chat = chat;
        this.peer = peer;
        this.peerSocket = peerSocket;
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

    public void close () {
        run = false;
        try {
            input.close();
        }
        catch (Exception e) { e.printStackTrace(); }
    }
}