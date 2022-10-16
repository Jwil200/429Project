import java.net.ServerSocket;
import java.net.Socket;

public class ServerHandler implements Runnable {
    private Chat chat;
    private ServerSocket listenSocket;
    
    private boolean run;
    
    /**
	* Creates a thread to listen for incoming connections
    * on the given ServerSocket. When a connection is
    * accepted creates a Peer and adds it to the list of 
    * connected peers.
	*
	* @param  chat  a Chat object to reference to the chat service
	* @param  listenSocket  a ServerSocket to watch for incoming connections
	*/
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

    /**
	* Stops the currently running thread by changing the run
    * variable to false.
	*/
    public void close () {
        run = false;
        try {
            listenSocket.close();
        }
        catch (Exception e) {}
    }
}
