/*    */

/*    */
/*    */ import java.io.DataInputStream;
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.EOFException;
/*    */ import java.io.IOException;
/*    */ import java.net.Socket;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;

/**
 * 
 * @author Fadwa Ezzat
 * 
 */
/*    */ class ServerConnection extends Thread
/*    */ {
	/*    */ private static final long RESPONSE_WAIT_TIME = 1000L;
	/*    */ private DataInputStream input;
	/*    */ private DataOutputStream output;
	/*    */ private Socket clientSocket;
	/*    */ private String[] availableServices;

	/*    */
	/*    */ public ServerConnection(Socket aClientSocket, String name, String[] services)
	/*    */ {
		/*    */ try
		/*    */ {
			/* 32 */ this.clientSocket = aClientSocket;
			/* 33 */ this.input = new DataInputStream(this.clientSocket.getInputStream());
			/* 34 */ this.output = new DataOutputStream(this.clientSocket.getOutputStream());
			/* 35 */ this.availableServices = services;
		} catch (IOException e) {
			/* 38 */ System.out.println("Connection:" + e.getMessage());
			/*    */ }
		/*    */ }

	/*    */
	/*    */ @Override
	public void run() {
		/*    */ try {
			/* 44 */ int len = this.input.readInt();
			/* 45 */ byte[] inputBytes = new byte[len];
			/* 46 */ for (int i = 0; i < len; i++) {
				/* 47 */ inputBytes[i] = this.input.readByte();
				/*    */ }
			/* 49 */ String requestedService = new String(inputBytes);
			/*    */ byte[] data;
			/* 56 */ if (requestedService.equals("list")) {
				/* 57 */ StringBuilder builder = new StringBuilder();
				/* 58 */ for (String srv : this.availableServices) {
					/* 59 */ builder.append(srv + ",");
					/*    */ }
				/* 61 */ data = builder.toString().getBytes();
				/*    */ }
			/*    */ else {
				/* 64 */ boolean found = false;
				/* 65 */ for (String srv : this.availableServices)
					/* 66 */ if (srv.contains(requestedService)) {
						/* 67 */ found = true;
						/* 68 */ break;
						/*    */ }
				/* 72 */ if (found) {
					/* 73 */ String filePath = "C://" + requestedService + ".txt";
					/* 74 */ Path path = Paths.get(filePath, new String[0]);
					/* 75 */ data = Files.readAllBytes(path);
					/*    */ }
				/*    */ else {
					/* 78 */ data = new String(requestedService + " Not Available").getBytes();
					/*    */ }
				/*    */
				/*    */ }
			/*    */
			/* 86 */ Thread.sleep(RESPONSE_WAIT_TIME);
			/*    */
			/* 88 */ this.output.writeInt(data.length);
			/* 89 */ this.output.write(data);
			/*    */
			/* 91 */ this.output.flush();
			/*    */ }
		/*    */ catch (EOFException e) {
			/* 94 */ System.out.println("EOF:" + e.getMessage());
			/*    */ try
			/*    */ {
				/* 103 */ this.clientSocket.close();
				/*    */ }
			/*    */ catch (IOException localIOException1)
			/*    */ {
				/*    */ }
			/*    */ }
		/*    */ catch (IOException e)
		/*    */ {
			/* 96 */ System.out.println("IO:" + e.getMessage());
			/*    */ try
			/*    */ {
				/* 103 */ this.clientSocket.close();
				/*    */ }
			/*    */ catch (IOException localIOException2)
			/*    */ {
				/*    */ }
			/*    */ }
		/*    */ catch (InterruptedException e)
		/*    */ {
			/* 98 */ e.printStackTrace();
			/*    */ try
			/*    */ {
				/* 103 */ this.clientSocket.close();
			} catch (IOException localIOException3) {
			}
		} finally {
			try {
				this.clientSocket.close();
			}
			/*    */ catch (IOException localIOException4)
			/*    */ {
				/*    */ }
			/*    */ }
		/*    */ }
	/*    */ }