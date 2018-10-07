package bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class BIOServer2_UseThread {
	private ServerSocket serverSocket;

	private int callCount = 0;

	public BIOServer2_UseThread(int port) throws IOException {
		super();
		serverSocket = new ServerSocket();
		serverSocket.bind(new InetSocketAddress(port));
		System.out.println("BIOServer_useThread init ok");
	}

	public void listen() throws IOException {
		while (true) {
			Socket socket = serverSocket.accept();
			if (null != socket) {
				new Thread(new ServiceHandler(socket)).start();
			}
		}
	}

	private void handerService(Socket socket) {
		InputStream in = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		OutputStream out = null;
		PrintWriter pw = null;
		try {
			// 1.读取客户端消息
			in = socket.getInputStream();
			isr = new InputStreamReader(in);
			br = new BufferedReader(isr);
			StringBuffer sb = new StringBuffer();
			String message = null;
			while (null != (message = br.readLine())) {
				// System.out.println(message);
				sb.append(message);
			}
			System.out.println("request:" + sb.toString());

			// 2.返回处理结果
			String sendMessage = "Hello client_" + callCount++;
			out = socket.getOutputStream();
			pw = new PrintWriter(out);
			pw.write(sendMessage);
			System.out.println("response:" + sendMessage);

			// 关闭IO
			pw.close();
			out.close();
			br.close();
			isr.close();
			in.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class ServiceHandler implements Runnable {

		private Socket socket;

		public ServiceHandler(Socket socket) {
			super();
			this.socket = socket;
		}

		@Override
		public void run() {
			handerService(socket);
		}

	}

	public static void main(String[] args) throws IOException {
		BIOServer2_UseThread bioServer = new BIOServer2_UseThread(8082);
		bioServer.listen();
	}

}
