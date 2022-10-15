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
                Peer peer = new Peer(chat, connectionSocket);
                chat.getConnectedPeers().add(peer);
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
