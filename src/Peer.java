
    
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Peer {

    private Integer id;
	private String host;
	private int port;
	private Socket socket;

	public Peer(int id, String host, int port) {
		this.id = id;
        this.host = host;
		this.port = port;

		try {
			socket = new Socket(host, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public Integer getId() {
		return id;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public Socket getSocket() {
		return socket;
	}

}
