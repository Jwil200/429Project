import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class PeerHandler implements Runnable {
		
    private Socket peerSocket;
    private BufferedReader input;
    private Chat chat;
    private String address;
    private int port;

    private boolean run;
    
    public PeerHandler (Chat chat, Socket peerSocket, String address, int port) throws IOException {
        this.chat = chat;
        this.peerSocket = peerSocket;
        this.address = address;
        this.port = port;
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
                        if (args.length < 2) break;
                        System.out.println("\nMessage recieved from IP: " + address + ":");
                        System.out.println("Message: " + args[1]);
                        System.out.print(">>>");
                        break;
                    case "terminate":
                        chat.getConnectedPeers().remove(chat.getPeer(address, port));
                        close();
                        System.out.println("\nClosed connection with IP: " + address);
                        System.out.print(">>>");
                        break;
                    default: // None
                }
            }
            catch (Exception e) {
                System.err.println("Error: Message dropped.");
            }
        }
    }

    public void close () {
        run = false;
        try {
            input.close();
        }
        catch (Exception e) {}
    }
}