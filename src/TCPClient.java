/*    */

/*    */
/*    */ import java.io.DataInputStream;
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.EOFException;
/*    */ import java.io.IOException;
/*    */ import java.net.Socket;
/*    */ import java.net.UnknownHostException;

/**
 * 
 * @author Fadwa Ezzat This class is used to simulate the connection request to
 *         a cloud
 */
/*    */ public class TCPClient
/*    */ {
	/*    */ public static void connectToCloud(int port, String service)
	/*    */ {
		/* 24 */ Socket socket = null;
		/*    */ try {
			/* 26 */ int serverPort = port;
			/* 27 */ String ip = "localhost";
			/* 28 */ String data = service;
			/*    */
			/* 30 */ socket = new Socket(ip, serverPort);
			/* 31 */ DataInputStream input = new DataInputStream(socket.getInputStream());
			/* 32 */ DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			/*    */
			/* 34 */ output.writeInt(data.length());
			/* 35 */ output.writeBytes(data);
			/* 36 */ output.flush();
			/*    */
			/* 38 */ int len = input.readInt();
			/* 39 */ byte[] inputBytes = new byte[len];
			/* 40 */ for (int i = 0; i < len; i++) {
				/* 41 */ inputBytes[i] = input.readByte();
				/*    */ }
			/* 43 */ String response = new String(inputBytes);
			/* 44 */ System.out.println("Received: " + response);
			/*    */ }
		/*    */ catch (UnknownHostException e) {
			/* 47 */ System.out.println("Sock:" + e.getMessage());
			/*    */
			/* 53 */ if (socket != null)
				/*    */ try {
					/* 55 */ socket.close();
					/*    */ }
				/*    */ catch (IOException localIOException1)
				/*    */ {
					/*    */ }
			/*    */ }
		/*    */ catch (EOFException e)
		/*    */ {
			/* 49 */ System.out.println("EOF:" + e.getMessage());
			/*    */
			/* 53 */ if (socket != null)
				/*    */ try {
					/* 55 */ socket.close();
					/*    */ }
				/*    */ catch (IOException localIOException2)
				/*    */ {
					/*    */ }
			/*    */ }
		/*    */ catch (IOException e)
		/*    */ {
			/* 51 */ System.out.println("IO:" + e.getMessage());
			/*    */
			/* 53 */ if (socket != null)
				/*    */ try {
					/* 55 */ socket.close();
					/*    */ }
				/*    */ catch (IOException localIOException3)
				/*    */ {
					/*    */ }
			/*    */ }
		/*    */ finally
		/*    */ {
			/* 53 */ if (socket != null)
				/*    */ try {
					/* 55 */ socket.close();
					/*    */ }
				/*    */ catch (IOException localIOException4)
				/*    */ {
					/*    */ }
			/*    */ }
		/*    */ }
	/*    */ }
