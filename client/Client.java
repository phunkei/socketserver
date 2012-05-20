
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class Client extends Applet implements Runnable {

	Thread t1;
	Socket socket;
	String ip;
	int port;

	LinkedList<String> queue_in;
	LinkedList<String> queue_out;
	
	QueueOut out;
	Thread in;
	
	TextField tf_input;
	TextArea ta_output;
	Button btn_send;

	@Override
	public void init() {
		queue_in = new LinkedList<String>();
		queue_out = new LinkedList<String>();
		
		
		this.setLayout(new BorderLayout());

		tf_input = new TextField();
		ta_output = new TextArea();
		btn_send = new Button("Senden");
		
		btn_send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage(tf_input.getText());
				tf_input.setText("");
			}
		});

		add(tf_input, BorderLayout.PAGE_START);
		add(ta_output, BorderLayout.CENTER);
		add(btn_send, BorderLayout.PAGE_END);
	}

	@Override
	public void start() {
		if (t1 == null) {
			t1 = new Thread(this);
			t1.start();
		}
	}

	@Override
	public void stop() {
		if(t1 != null) {
			t1.interrupt();
			try {
				socket.close();
			}
			catch (IOException ex) {}
			t1 = null;
		}
	}

	@Override
	public void destroy() {
		if(t1 != null) {
			t1.interrupt();
			try {
				socket.close();
			}
			catch (IOException ex) {}
			t1 = null;
			System.out.println("Closing Cient");
		}
	}

	@Override
	public void run() {
		ip = "192.168.0.100";
		port = 27027;
		try {
			socket = new Socket(ip, port);
			queue_out.add("hallo");
			queue_out.add("welt");
			out = new QueueOut(socket);
			in = new Thread(new QueueIn(socket, queue_in));
			
			out.start();
			in.start();
			
			while(true) {
				if(!queue_in.isEmpty()) {
					ta_output.append(queue_in.getFirst());
				}
			}
		}
		catch (UnknownHostException ex) {
			System.err.println("Client: Unknown Host");
		}
		catch (IOException ex) {
			System.err.println("Client: IOException");
		}
		finally {
			System.out.println("Client: Shutting down");
			this.destroy();
		}
	}
	
	public void sendMessage(String msg) {
		this.out.add(msg);
	}
}
