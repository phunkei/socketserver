import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.LinkedList;

public class QueueIn implements Runnable {
	Socket socket;
	LinkedList<String> queue;
	BufferedReader in;
	
	public QueueIn(Socket socket, LinkedList<String> queue) {
		this.socket = socket;
		this.queue = queue;
		System.out.print("InputQueue started");
	}
	
	@Override
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String message;
			while(true) {
				System.out.println("running ipq");
				while((message = in.readLine()) != null) {
					queue.addLast(message);
				}
			}
		}
		catch (IOException ex) {}
	}
}

