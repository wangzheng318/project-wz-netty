package bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class BIOClient {

	private Socket socket;

	public BIOClient(String hostname, int port) throws IOException {
		socket = new Socket();
		socket.connect(new InetSocketAddress(hostname, port));
		System.out.println("BIOClient init success");
	}

	private void sentMessageToServer() throws IOException {
		// 1.向服务端发送消息
		OutputStream out = socket.getOutputStream();
		PrintWriter pw = new PrintWriter(out);
		pw.write("hello client1");
		pw.write("hello client2");
		pw.flush();
		socket.shutdownOutput();
		System.out.println("send:"+"hello server");
		// 2.接收服务端响应消息
		InputStream in = socket.getInputStream();
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(isr);
		String message = null;
		while (null != (message = br.readLine())) {
			System.out.println("receive"+message);
		}
	}

	public static void main(String[] args) throws IOException {
		mulityThreadClient();
	}

	private static void singleCall() throws IOException {
		BIOClient c = new BIOClient("127.0.0.1", 8082);
		c.sentMessageToServer();
	}

	private static void mulityThreadClient() {
		for (int i = 0; i < 100; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						singleCall();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

}
