import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageService implements Runnable {

	private final Socket client;
	private final ServerSocket serverSocket;

	MessageService(ServerSocket serverSocket, Socket client) {
		this.client = client;
		this.serverSocket = serverSocket;
	}

	@Override
	public void run() {
		PrintWriter out = null;
		BufferedReader in = null;
		String clientName = client.getInetAddress().toString();
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
			
			String line;
			System.out.println("Waiting for "+clientName);
			while((line = in.readLine()) != null) {
				System.out.println(clientName + ": " + line);
				out.println(line);
				out.flush();
			}
		}
		catch (IOException e) {
			System.out.println("Server/MessageService: IOException");
		}
		finally {
			if(!client.isClosed()) {
				System.out.println("Server: Client disconnected");
				try {
					client.close();
				}
				catch (IOException e) {}
			}
		}
	}
}
