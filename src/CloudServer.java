
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

/**
 * 
 * @author Fadwa Ezzat This class creates a Thread for each cloud
 */
public class CloudServer extends Thread {
	private String name;
	private Integer port;
	private String[] services;
	private boolean running;
	private ServerSocket listenSocket = null;

	public CloudServer(String name, Integer port, String[] services) {
		/* 24 */ this.name = name;
		/* 25 */ this.port = port;
		/* 26 */ this.services = services;
		/* 27 */ this.running = true;
	}

	public synchronized void terminate() {
		/* 31 */ this.running = false;
		/* 32 */ if (this.listenSocket != null)
			try {
				/* 34 */ this.listenSocket.close();
			} catch (IOException e) {
				/* 36 */ e.printStackTrace();
			}
	}

	@Override
	public void run() {
		try {
			/* 43 */ this.listenSocket = new ServerSocket(this.port.intValue());
			/* 44 */ System.out.println(
					this.name + " started on port: " + this.port + ", for services: " + Arrays.toString(this.services));

			/* 46 */ while (this.running) {
				/* 47 */ Socket clientSocket = this.listenSocket.accept();
				/* 48 */ ServerConnection c = new ServerConnection(clientSocket, this.name, this.services);
				/* 49 */ c.start();
			}
		} catch (IOException localIOException) {
			/* 54 */ if (this.listenSocket != null)
				try {
					/* 56 */ this.listenSocket.close();
				} catch (IOException localIOException1) {
				}
		} finally {
			/* 54 */ if (this.listenSocket != null)
				try {
					/* 56 */ this.listenSocket.close();
				} catch (IOException localIOException2) {
				}
		}
	}
}