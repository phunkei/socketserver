
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueOut extends Thread {
	Socket socket;
	public ConcurrentLinkedQueue<String> queue;
	PrintWriter out;
	
	public QueueOut(Socket socket) {
		super();
		this.socket = socket;
		this.queue = new ConcurrentLinkedQueue<String>();
		System.out.print("OutputQueue started");
	}
	
	@Override
	public void start() {
		try {
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			System.out.println("Running outputqueue");
			while(true) {
				if(this.queue.size() > 0) {
					String message = this.queue.poll();
					System.out.println("Sending "+message);
					out.println(message+"\n");
				}
			}
		}
		catch (IOException ex) {
			System.out.println("Outputqueue: IOException");
		}
	}
	
	public synchronized void add(String msg) {
		this.queue.add(msg);
	}
}
