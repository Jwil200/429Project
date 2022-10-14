import java.net.ServerSocket;
import java.net.Socket;

public class ServerHandler implements Runnable {
    private Chat chat;
    private ServerSocket listenSocket;
    
    private boolean run;
    
    public ServerHandler (Chat chat, ServerSocket listenSocket) {
        this.chat = chat;
        this.listenSocket = listenSocket;

        run = true;
        new Thread(this).start();
    }

    @Override
    public void run () {
        while (run) {
            try {
                Socket connectionSocket = listenSocket.accept();
                chat.getConnectedPeers().add(new Peer(chat, connectionSocket));
                System.out.println("\nConnected to Peer.");
                System.out.println(">>>");
            } catch (Exception e) {}
        }
    }

    public void close () {
        run = false;
        try {
            listenSocket.close();
        }
        catch (Exception e) {}
    }
}
